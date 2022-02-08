싱글톤 컨테이너
===

chap04에서 작성한 AppConfig는 요청이 들어올 때 마다 인스턴스를 생성하여 return 해준다.

```java
    void noSpringDI(){
        //given
        AppConfig appConfig = new AppConfig();

        //when
        MemberRepository memberRepository1 = appConfig.memberRepository();
        MemberRepository memberRepository2 = appConfig.memberRepository();

        //then
        Assertions.assertThat(memberRepository1).isNotSameAs(memberRepository2);
    }
```

이와 같이 `appConfig`를 통해 만든 인스턴스는 서로 다르다는 것을 알 수 있다.(호출 할 때마다 새로 생성)

그럼 어떻게 인스턴스 하나를 생성해서 동일한 인스턴스를 계속해서 사용할까?

## 싱글톤 패턴

```java
public class SingletonService {

    // 1. static 영역에 인스턴스 1개 생성
    private static final SingletonService singletonService = new SingletonService();

    // 2. 외부에서 생성자를 호출 할 수 없도록 private으로 설정
    private SingletonService() {
    }

    // 3. static 영역에 instance를 가져오기 위한 getter만 생성 
    public static SingletonService getInstance() {
        return singletonService;
    }
}
```

위와 같이 싱글톤 패턴을 적용하여 하나의 인스턴스를 애플리케이션 여러 곳에서 사용할 수 있도록 한다. 아래는 Testcode이다.

```java
void singleton(){
        //given
        SingletonService instance1 = SingletonService.getInstance();
        SingletonService instance2 = SingletonService.getInstance();

        assertThat(instance1).isSameAs(instance2); // pass
    }
```

### 싱글톤 패턴의 문제점

- 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
- 의존관계상 클라이언트가 구체 클래스에 의존한다  -> DIP위반
- 클라이언트가 구체 클래스에 의존하여 OCP를 위반할 가능성이 높다.
- 테스트하기 어렵고 초기화가 어렵다.
- 결론적으로 유연성이 떨어지고 안티패턴으로 불린다.

## 해결 방안

스프링 컨테이너를 이용한다! 

스프링 컨테이너느 싱글톤 패턴을 적용하지 않아도, 객체 인스턴스를 **싱글톤으로 유지시켜준다.**

이로 인해 장점은 다음과 같다.

- 싱글톤 패턴을 구현하기 위한 코드가 필요 없어진다.
- DIP, OCP, 테스트, private 생성자로 자유롭다.

예제 테스트를 보면 다음과 같다.

```java
AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

@Test
@DisplayName("스프링 컨테이너를 통한 DI 싱글톤을 스프링 컨테이너가 해준다.")
    void singleton() {
            //given
            MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        //when
        assertThat(memberService1).isSameAs(memberService2);
    }
```

스프링 컨테이너를 만들고 스프링 컨테이너에서 `memberService` 인스턴스를 가져온다. 가져온 인스턴스의
주소 값은 동일하며 테스트는 통과한다.

```java
memberService1 = hello.corenoref.service.member.MemberServiceImpl@73173f63
memberService2 = hello.corenoref.service.member.MemberServiceImpl@73173f63
```

### 싱글톤 방식의 주의점

싱글톤의 개념이 하나의 인스턴스를 **애플리케이션 여러 곳에서 공통으로 사용하는 것이기 때문에** 클래스 안에
**공유되는 상태가 존재할 경우** 큰 문제가 발생될 수 있다.

만약 공유되는 상태를 여러 Bean에서 접근을 한다면 원하는 결과를 받기는 힘들 것이다.

즉 **무 상태로 설계를 해야만 한다.**
- 특정 클라이언트에게 의존되는 상태가 있으면 안된다.
- 특정 클라리언트가 값을 변경할 수 있는 상태가 있으면 안된다.
- 가급적 읽기만 가능해야 한다.
- 플드 대신 자바에서 공유되지 않는 **지역변수, 파라미터, ThreadLocal** 등을 사용해야한다.

## 그럼 스프링은 어떻게 싱글톤을 유지할 수 있을까?

해답은 `@Configuration`에 있다. 

Config파일을 이용하여 Bean으로 등록하여 class를 log찍어보면 뒤에 이상한 수식어가 붙어있는 것을 볼 수 있다.

```java
AppConfig bean = ac.getBean(AppConfig.class);
System.out.println("bean = " + bean);

//output
bean = hello.corenoref.AppConfig$$EnhancerBySpringCGLIB$$a6113a10@4233e892
```

위와 같이 class정보 뒤에 `$$EnhancerBySpringCGLIB$$a6113a10@4233e892` 가 붙은 것을 확인 할 수 있다.

스프링은 내부적으로 **CGLIB** 바이트 조작 라이브러리를 이용하여 Bean으로 등록하려는 class를 **상속받아 
새로운 class를 만들어 스프링 컨테이너에 등록한다.**

### AppConfig@CGLIB의 예상 코드

```java
@Bean
public MemberRepository memberRepository() {
    if (memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) { 
        return 스프링 컨테이너에서 찾아서 반환;
    } else { //스프링 컨테이너에 없으면
        기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록 return 반환
    } }
```

예상 코드처럼 컨테이너에 등록되어 있으면 등록되어 있는 인스턴스를 리턴하고 등록되어 있지 않으면 새로 생성한다. 위와 같은 스프링의 특성으로
싱글톤을 보장할 수 있게 되는 것이다.