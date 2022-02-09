컴포넌트 스캔
===

이전까지는 Bean으로 등록하려는 객체들을 Config를 만들어 하나씩 하나씩 등록을 하였다.

하지만 등록해야 하는 Bean이 수 없이 많다면 어떻게 해야할까?

## 해결방법

Bean을 등록하는 방법은 `@Configuration`을 이용하는 방법도 있지만 **컴포넌트 스캔이라는 방법 또한 존재한다.**

`@Component` 애노테이션이 붙은 객체들을 스프링으로 등록하는 것이다.

또한 컴포넌트 스캔을 이용하면 Config 객체 또한 등록이 된다. 그 이유는 `@Configuration` 안에 `@Component`가 들어있다.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {
    // ...
}
```

`@Component`가 붙은 객체를 Bean으로 등록할 때 Bean의 이름을 등록해줄 수 있다.

```java
@Component(name = "등록할 이름")
public class Foobar{
    //...
}
```

만약 Bean의 이름을 지정해 주지 않는다면 class이름에서 제일 앞 글자를 소문자로 변경하여 등록하게 된다. (Foobar -> foobar)


## 의존성 주입

컴포넌트 스캔을 이용하여 Bean을 등록하였을 때 등록 된 Bean을 주입 받으려면 

`@Autowired`를 이용하면 된다. (7장에서 자세하게)

사용법은 아래와 같이 사용할 수 있다. (먼저 생성자 주입만 보려 한다.)

```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

위와 같이 코드를 작성하면 스프링에 등록되어 있는 Bean을 주입받을 수 있다. 이때 기본 조회 전략은 타입이 같은 빈을 찾아서 주입한다. 

**getBean(MemberRepository.class) 와 동일하다고 이해하면 된다.**

## 스캔 대상 지정하기

`@ComponentSacn()`을 이용할 때 option을 부여하여 스캔 대상을 지정할 수 있다.

```java
@ComponentScan(
        basePackages = "지정할 package"
        // basePackages = {"지정할 package", "지정할 package"} 여러 개 지정 가능
)
```

**Default값으로는 현재 설정파일과 같은 package 부터 이하 모든 package를 조회한다.**

권장하는 방법은 **메인 설정 정보는 프로젝트를 대표하는 정보이기 때문에 프로젝트 시작 루트 위치에 두고 `basePackages`를 생략하는 것을 권장한다.

## 스캔 기본 대상

`@Component`이외에 스캔 기본 대상들이 있다.

- `@Repository` : 스프링 데이터 계층에 접근하는 객체로 인식하며 데이터 계층의 예외를 스프링 예외로 변환해준다.
- `@Service` : 특별한 처리는 없지만 비지니스 핵심 로직이 들어있다는 것을 명시
- `@Controlloer` : 스프링 MVC 컨트롤러로 인식
- `@Component` : 컴포넌트 스캔에 사용 
- `@Configuration :` 스프링 설정파일

## 필터

컴포넌트 스캔의 대상이 되는 class 중 `exclude, include` 필터를 이용하여 포함 및 제외 시킬 수 있다.

```java
incldeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class)
```

위의 예시코드를 확인하면 Filter의 타입으로 `type = FilterType.ANNOTATION`를 선택하였다. 즉 Annotation을 필터링 하여 스캔하겠다는 것이다.
>나머지 필터 타입은 https://docs.spring.io/spring-framework/docs/3.0.0.M4/spring-framework-reference/html/ch03s10.html 를 참고

`classes = MyIncludeComponent.class`는 `MyIncludeComponent`라는 애노테이션이 붙은 class들을 추가 시키라는 것이며 
exclude 필터는 현재와 반대라고 생각하면 된다.

`@Component`면 충분하기 때문에 `incldeFilters`를 거의 사용할 일이 없으며 특정 class를 제외하고 싶을 때 `excludeFilters`를 쓰는 추세이다.

특히 최근에는 스프링 부트에서 기본적으로 **컴포넌트 스캔을 지원하기 때문에 기본설정에 최대한 맞추어 사용하자.**

## 충돌

컴포넌트 스캔을 이용할 때 중복이 발생하면 어떻게 될까?

### 자동 vs 자동

`ConflictingBeanDefinitionException`를 발생시킨다.

### 수동 vs 자동

수동이 **우선권을 가지고 있으며 수동으로 등록된 Bean이 등록된다. 혹은 자동 Bean을 수동 Bean이 오버라이딩 한다.**

최근 스프링 부트는 수동 Bean과 자동 Bean이 충돌하였을 경우 아래와 같은 error를 발생

```java
Consider renaming one of the beans or enabling overriding by setting
spring.main.allow-bean-definition-overriding=true
```



