빈 스코프
===

빈 스코프란 번역 그대로 빈이 존재할 수 있는 범위를 뜻한다.

스프링은 다음과 같은 다양한 스코프를 지원한다.

1. 싱글톤 : 기본 스코프이며 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프이다.
2. 프로토 타입 : 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 그 이후는 관여하지 않아 주입받은 클라이언트에서 관리하게 된다.
3. 웹 관련 스코프
   1. request: 요청이 들어오고 나갈 때 까지 유지되는 스코프
   2. session: 웹 세션이 생성되고 종료될 떄까지 유지되는 스코프
   3. application: 웹의 서블릿 컨텍스트와 같은 범위로 유지되는 스코프


## 지정방법

`@Component` 를 이용할 경우

```java
@Scope(value = "")
@Component
public class BeanScopeExample{}
```

`@Bean`을 이용할 경우

```java
@Scope(value ="")
@Bean
public class BeanScopeExample{}
```

## 프로토 타입

싱글톤 빈은 조회하면 **항상 동일한 스프링 빈**을 반환하게 된다. 반면 프로토 타입은 **스프링 컨테이너에 조회를 할 때마다 새로운 
스프링 빈을 생성하여 return해준다.**

```java
@Test 
void prototypeBeanTest(){
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

    PrototypeBean bean = ac.getBean(PrototypeBean.class);
    PrototypeBean bean2 = ac.getBean(PrototypeBean.class);

    System.out.println("bean.getClass() = " + bean);
    System.out.println("bean2.getClass() = " + bean2);
   
    Assertions.assertThat(bean).isNotSameAs(bean2);
    ac.close();
   
    //output
    PrototypeBean.init
    PrototypeBean.init
    bean.getClass() = hello.corenoref.scope.PrototypeTest$PrototypeBean@66c92293 
    bean2.getClass() = hello.corenoref.scope.PrototypeTest$PrototypeBean@332796d3
}


@Scope(value = "prototype")
static class PrototypeBean{
    @PostConstruct
    public void init(){
        System.out.println("PrototypeBean.init");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("PrototypeBean.destroy");
    }
}
```

위의 결과 처럼 `Scope`가 `prototype`인 class를 빈으로 등록하고 스프링컨테이너에서 꺼내오면 결과는 다음과 같다.

조회 된 스프링 빈의 참조값이 다르며 **스프링 빈 생성 및 초기화 까지는 관여하지만 소멸까지 책임을 지지 않는다. 소멸의 책임은 클라이언트에게 있다.**

## 프로토 타입을 싱글톤 빈에서 주입받을 때 문제점

싱글톤 빈은 스프링 빈에서 조회할 때 항상 동일한 빈을 return한다.

하지만 **싱글톤 빈에서 프로토 타입 스코프의 빈을 DI받아 사용한다면 어떻게 될까?**

결론적으로는 **싱글톤 빈에 프로토 타입 빈이 귀속된다. 그렇기 떄문에 만약 프로토 타입에 애플리케이션 전체에 공유 목적인 상태가 있으면 문제를 발생 시킬 수 있다.**

귀속되었기 떄문에 **싱글톤 빈을 다른 곳에서 주입을 받는다고 하더라도 안에 있는 프로토 타입의 빈은 새로 생성되지 않는다.**

### 해결방법

프로토 타입을 주입받는 싱글톤 빈에서 **스프링 컨테이너를 의존하여 직접 꺼내올 수 있다.**

의존관계에 있는 빈을 주입 받는 것이 아니라 직접 조회하는 것을 **DL(Dependency LookUp)**이라고 한다.

스프링과 자바 진영에서는 DL기능을 사용하기 쉽게 제공하는 패키지가 있다.

#### ObjectProvider, ObjectFactory

ObjectProvider, ObjectFactory를 싱글톤 빈에서 **주입**받고 그 이후에 `ObjectProvider, ObjectFactory`여기서 
프로토 타입의 빈을 찾아 사용하면 매번 새로운 프로토타입의 빈을 사용할 수 있다.

사용법은 아래와 같다.

```java
@RequiredArgumentConstructor
public class ProviderExample{
    @Autowired
    private ObjectProvider<주입받을 빈의 타입> provider;
    
    //...
   provider.getObject();
}

//or

@RequiredArgumentConstructor
public class ProviderExample{
   @Autowired
   private ObjectFactory<주입받을 빈의 타입> provider;
   
   //...
   provider.getObject();
}
```

`ObjectProvider, ObjectFactory`는 동일하게 작동한다 하지만 차이점은 

ObjectFactory를 확장한 것이 ObjectProvider이다. ObjectFactory 보다 편리한 기능(상속, optional, stream)들을 제공한다. 

위 두 패키지는 별도 라이브러리를 추가할 필요 없이 사용할 수 있다.

#### JSR-330 Provider

자바 표준에서 제공하는 JSR-330 Provider가 있다. 사용을 하려면 아래의 의존성을 추가해줘야한다.

```java
javax.inject:javax.inject:1
```

사용법은 `ObjectProvider, ObjectFactory`와 크게 차이가 없다. 호출 method가 `get()`이다.
```java
@RequiredArgumentConstructor
public class ProviderExample{
    @Autowired
    private Provider<주입받을 빈의 타입> provider;
    
    //...
   provider.get();
}
```

`ObjectProvider, ObjectFactory`와 다른점은 스프링에 의존적이지 않기 때문에 다른 컨테이너에서도 사용할 수 있다.

### 선택의 기준

만약 스프링 외적으로도 사용 가능성이 있는 코드라면 자바 표준을 이용하고 그렇지 않다면 목적에 맞게 제공하는 기능을 보고 선택하면 된다.

## 웹 스코프

웹 스코프에는 여러가지가 있지만 `request`를 대표적으로 알아보자면 

사용자가 요청이 들어온 시점에 빈을 생성하고 응답을 하는 시점에 빈을 소멸시킨다.

### 여기에도 고려해야할 사항이 있으니

`request`로 정의한 빈을 스프링이 시작되는 시점에 DI를 받게 되면 Exception이 발생하게 된다.

```
Error creating bean with name 'myLogger': Scope 'request' is not active for the
current thread; consider defining a scoped proxy for this bean if you intend to
refer to it from a singleton;
```

즉 아직 `request` 스코프에 있는 빈이 생성되지 않았는데 DI를 받으려고 해서 그렇다.

그럼 `request` 스코프를 사용할 수 없는 것일까?

### 해결방법

먼저 앞에서 사용했던 `ObjectProvider`를 이용하여 해결하는 방법이 있다.

`request`가 스코프인 빈을 직접 주입받는 것이 아닌 **request 스코프 빈 제네릭 타입의 ObjectProvider를 주입받아 사용하면된다.**

더 간단한 벙법은 없을까?

스코프를 지정할 때 `proxyMode`를 지정할 수 있다.

```java
@Scope(value = "request", proxyMode = )
```

proxyMode에는 `ScopedProxyMode.TARGET_CLASS`, `ScopedProxyMode.INTERFACE` 등이 있다.

proxyMode를 활성화 시키면 내부적으로는 다음과 같이 동작한다.

1. CGLIB 바이트 조작 라이브러리를 이용하여 `request` 스코프 빈을 상속받아 새로 생성 후 빈 등록 (내부에는 진짜 빈으로 위임하는 로직이 포함)
2. 새로 만든 프록시 객체를 `request`스코프 빈을 주입받는 곳에 넣어준다.
3. request가 들어와서 해당 객체를 사용할 때 진짜 빈을 요청한다.
4. response가 되기 전까지 해당 빈을 사용하다가 소멸된다.

위와 같이 프록시 모드를 사용하면 **DI를 받을 때 ObjectProvider를 받을 필요 없이 해당 객체를 직접 DI받아서 사용이 가능하다.**

### proxy

정리하자면 원본 객체를 상속받은 프록시 객체를 빈으로 등록하고 해당 빈을 DI받는 곳에 **프록시 객체를 넣어주는 것이다.**

프록시 객체에는 **진짜 빈을 찾는 로직이 포함되어 있어 필요할 때 진짜 빈을 조회하여 처리한다.**

## 결론

provider든 proxy든 핵심은 진짜 객체가 필요한 시점에 **지연 처리를 한다는 점이다.**