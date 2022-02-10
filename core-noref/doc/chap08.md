빈 생명주기 콜백
===

스프링 빈이 컨테이너에 등록될 때 초기화 작업이나 빈이 컨테이너에서 소멸되기 전 작업을 어떻게 할 수 있을까?

예를 들어 데이터 커넥션 풀이나 네트워크 소켓처럼 애플리케이션 시작 시점에 미리 연결을 해두고, 종료 시점에 연결을 
종료하는 작업을 진행할 때 **스프링 빈의 생명주기 콜백을 이용하면 편하게 작업할 수 있다.**

## 생명주기

스프링 빈은 간단하게 다음과 같은 생명주기를 갖는다.

**객체 생성 -> 의존관계 주입**

스프링은 의존관계 주입이 완료가 되면 스프링 빈에게 콜백 메서드를 통해 **초기화 시점을 알려주는 다양한 기능을 제공한다. 또한 
스프링 컨테이너가 종료되기 직전 소멸 콜백을 준다.**

**스프링 빈의 이벤트 라이프 사이클**

스프링 컨테이너 생성 -> 스프링 빈 생성 -> 스프링 의존관계 주입 -> 스프링 빈 초기화 콜백 -> 스프링 빈 사용 -> 스프링 빈 소멸 콜백 -> 스프링 종료

그럼 이제 콜백을 어떻게 지원하는지 알아보자.

## InitializingBean, DisposableBean

`InitializingBean, DisposableBean` 인터페이스를 Bean으로 등록하려는 class가 구현을 하면 된다.

`InitializingBean`은 다음과 같은 method를 Override해야 한다.

```java
// 의존관계 주입이 완료 된 이후 시점
@Override
public void afterPropertiesSet() throws Exception {
    // init
}
```

`DisposableBean`는 다음과 같은 method를 Override해야 한다.

```java
// 스프링이 종료되기 전 시점
@Override
public void destroy() throws Exception {
    //destroy
}
```

위의 두 개의 인터페이스를 구현하면 컨테이너에서 빈이 등록되는 시점, 소멸되는 시점에 콜백으로 호출된다.

인터페이스를 이용해 콜백을 이용하는 것에 단점은 다음과 같다.

1. 초기화 및 소멸 method의 이름을 지정할 수 없다.
2. 인터페이스가 스프링 전용 인터페이스기 떄문에 스프링에 의존적이다.
3. 내가 코드를 고칠 수 없는 라이브러리에 적용할 수 없다.

## Bean(initMethod = "", destroyMethod = "")

`@Bean`을 이용하여 등록할 때 해당 class에 있는 method를 **초기화 및 소멸 콜백 method**로 지정할 수 있다.

```java
//ex
@Bean (initMethod = "init", destroyMethod = "destroy")
public NetworkClient networkClient() {
    NetworkClient networkClient = new NetworkClient();
    networkClient.setUrl("http://hello-spring.dev");
    return networkClient;
}
```

destroyMethod를 정의할 때 내부적으로 보면 다음과 같다.

```java
String destroyMethod() default AbstractBeanDefinition.INFER_METHOD;

Constant that indicates the container should attempt to infer the destroy method name for a bean as opposed to explicit specification of a method name.
The value "(inferred)" is specifically designed to include characters otherwise illegal in a method name,
ensuring no possibility of collisions with legitimately named methods having the same name.
Currently, the method names detected during destroy method inference are "close" and "shutdown", if present on the specific bean class.
public static final String INFER_METHOD = "(inferred)";
```

즉 destroyMethod를 정의하지 않아도 내부적으로 추론을 하여 콜백으로 지정을 해준다. 추론이 가능한 method명은 `close, shotdown`이다.

설정 정보를 통한 콜백 정의의 특징은 다음과 같다.

1. 초기화, 소멸 method의 이름을 지정할 수 있다.
2. 스프링 빈이 스프링 코드에 의존적이지 않다.
3. 코드가 아니라 설정 정보를 사용하기 때문에 **수정할 수 없는 라이브러리에도 적용이 가능하다.**

## @PostConstruct, @PreDestroy

제일 간단하게 초기화에 이용할 method에는 `@PostConstruct`를 붙이고 소멸에 이용할 method에는 `@PreDestroy`를 이용한다.

애노테이션을 통해 콜백을 정의할 때 특징은 다음과 같다.

1. 최신 스프링에서 권장하는 방법이다.
2. 편리하다.
3. 스프링에 의존하는 것이 아닌 자바 표준을 이용한다. (`javax.annotation.PostConstruct` JSR-250)
4. 컴포넌트 스캔과 잘 어울린다.
5. **수정할 수 없는 외부 라이브러리에는 적용을 할 수 없다.**



