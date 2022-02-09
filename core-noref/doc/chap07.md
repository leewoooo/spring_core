의존관계 자동 주입
===

의존관계 주입(의존성 주입) 방법에는 총 4가지가 있다.

1. 생성자 주입
2. 수정자 주입 (setter 주입)
3. 필드 주입
4. 일반 메서드 주입

## 생성자 주입

생성자 주입은 객체가 생성되는 시점에 **딱 1번 호출 되는 것을 보장**해준다. 또한 **불변, 필수** 의존 관계를 사용할 수 있다.

지금 것 코드를 작성해왔던 방법이 생성자 주입이다.

```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

생성자 주입을 통해 의존관계를 주입받을 때 `@Autowired`를 생략할 수 있다.

## 수정자 주입(setter 주입)

말 그대로 setter를 통해 주입을 받는 것이다. 주입의 대상이 되는 인스턴스를 `setter를 통해 주입받는다.`

```java
public class MemberService{
    private MemberRepository memberRepository;
    
    @Autowired
    public void setMemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
}
```

## 필드 주입

`@Autowired`를 주입 대상이 되는 필드 위에 부여하는 것이다.

```java
public class MemberService{
    @Autowired
    private MemberRepository memberRepository;
}
```

필드 주입은 **사용하지 말자**

코드를 간결하게 작성을 할 수 있지만 외부에서 변경이 불가능하여 **유연한 코드 작성이 불가능해진다.** 또한 DI 프레임워크가 없다면 할 수 있는 것이 없으며 테스트가 어렵다.

## 일반 메소드 주입

메소드의 파라미터로 들어오는 객체를 주입받는 방법이다. 일반적으로 잘 사용되지 않으며 여러개를 주입받을 수 있다.

```java
@Autowired
public void save(MemberRepository memberRepository){
    //do anything    
}
```

## 그렇다면 무슨 주입 방법을 사용해야할까?

**불변, 필수**의 옵션을 사용할 수 있는 것이 가장 큰 장점이다.

### final

`final` 키워드를 이용하여 **불변, 필수**의 옵션을 이용할 수 있다.

### 불변

의존관계 주입으로 주입받은 인스턴스는 애플리케이션이 종료되기 전까지는 **변경 될 일이 거의 없다.** 즉 한 번 주입된 의존관계는 
변하지 말아야 한다는 것이다. (변경이 필요할 때만 수정자 주입을 이용하여 동적으로 변경해주자.)

`final`이 부여된 필드는 **runtime**에 한번 초기화 된 값을 변경할 수 없는 특징을 가지고 있기 때문이다.

### 필수

`final` 키워드를 붙인 필드는 **필수** 옵션을 부여받는다. 즉 초기화를 해주지 않으면 **컴파일에러**를 볼 수 있다.
이로 인해 얻을 수 있는 장점은 누락을 방지할 수 있다.
>java: variable discountPolicy might not have been initialized

내가 생각할 때 가장 큰 장점은 `testCode`를 작성할 때 좋은 거 같다. 생성자로 주입을 받기 때문에 
mock 객체를 집어 넣기도 편하며 안에서 사용하는 객체를 내 마음대로 핸들링 할 수 있다는 장점이 있기 때문이다.

## 의존성 주입을 받을 때 2개이상의 Bean이 조회되었을 때

이전 작성한 `DiscountPolicy`를 구현하고 있는 2개의 class를 모두 빈으로 등록하고 `AutoAppConfigTest`를 실행해보면 다음과 같은
에러메세지를 만날 수 있다.
```java
Unsatisfied dependency expressed through constructor parameter 1; nested exception is org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'hello.corenoref.service.discount.DiscountPolicy' available: expected single matching bean but found 2: fixDiscountPolicy,rateDiscountPolicy
```

즉 `DiscountPolicy`를 주입받는 `OrderSerivce`에서 에러가 발생한 것인데 2개가 등록되서 무엇을 받아야 할 지 모르겠다는 의미이다.

`@Autowired`를 사용하면 `ac.getBean(조회할 타입.class)`와 거의 유사하게 **타입으로 조회한다.**

그렇기 때문에 현재 `DiscountPolicy`로 등록된 타입이 2개로 조회된 결과이다.

### 해결방법

`@Qualifier` 와 `@Primary`를 이용하는 것이다.

#### `@Qualifier`

`@Qualifier`는 Bean을 등록할 때 구분자 이름을 하나 더 부여한다고 이해하면 편하다. 여기서 `@Qualifier`를 이용하면
Bean의 이름이 **변경된다고 생각하면 안된다.**

구분자만 추가를 하는 것이지 빈의 이름까지 변경하는 것은 아니다.

사용법은 아래와 같다.

```java
@Component
@Qualifier(name="추가할 구분자")
public class Foobar{}

// ...

public class QualifierEx{
    private final Foobar foobar;
    
    public QualifierEx(@Qualifier(name="추가할 구분자") Foobar foobar){
        
    }
}
```

이와 같이 주입받을 때 `@Qualifier`와 구분자를 명시하여 중복으로 등록된 타입의 빈에서 원하는 빈을 선택할 수 있다.

`@Qualifier(name = 추가할 구분자)`를 있는 그대로 사용하다보면 오타가 날 확률도 있고 컴파일 타임에서 확인이 되지 않으니
어노테이션을 직접 만들어 사용하는 것을 권장한다.

```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("mainFoobar")
public @interface MainFoobar { }

//...

@Component
@MainFoobar
public class Foobar{}

//...
public class QualifierEx{
    private final Foobar foobar;

    public QualifierEx(@MainFoobar Foobar foobar){

    }
}
```

`@Qualifier`위에 있는 어노테이션들은 `@Qualifier`에서 그대로 가져온 것이다.

애노테이션에는 상속기능은 존재하지 않으며 여러개의 애노테이션을 사용하게 해주는 것은 스프링에서 제공을 해주는 것이다.

#### `@Primary`

`@Primary`는 간단하다 동일한 타입으로 여러 개의 빈이 등록되었을 때 **우선권을 가져온다.**

즉 `@Primary`가 붙어있는 class를 먼저 **의존관계 주입을 받는 다는 것이다.**

사용법은 아래와 같다.

```java
@Component
@Primary
public class Foobar{}
```

## 정리

편리한 자동 기능을 기본으로 사용하자
직접 등록하는 기술 지원 객체는 수동 등록
다형성을 적극 활용하는 비즈니스 로직은 수동 등록을 고민해보자
