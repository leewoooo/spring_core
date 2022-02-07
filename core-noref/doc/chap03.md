chap03
===

역할(interface)과 구현을 충실하게 분리를 하였다. (다형성을 활용하고, 인터페이스와 구현 객체를 분리하였다.)

OCP, DIP 같은 객체지향 설계 원칙을 충실히 준수하였다.(그렇게 보이겠지만 아니다.) 

- 클라이언트 코드에서 역할과 구현을 둘다 의존하고 있기 때문에 DIP 및 OCP를 지키고 있다고 할 수 없다.

그렇다면 DIP를 지키기 위해서 역할(interface)만 의존하고 있다고 가정해보자. 역할만 의존하고 있는 상태로
로직을 실행하면 분명 `NullPointerException`이 발생한다.

구현체 없이 역할(interface)로만 실행을 할 수 없기 떄문이다.

그렇다면 이 문제를 어떻게 해결할까?

## 해결방법

관심사를 분리한다. 클라이언트 코드는 자신의 로직만 집중하고 구현 클래스를 생성하고 외부에서 넣어주는 **역할만을** 
클래스를 만든다.

즉 **구현 객체를 생성하고 연결하는 책임을 가지는 설정 클래스를 만드는 것이다**

```java
public class AppConfig {

    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }

    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    public DiscountPolicy discountPolicy(){
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
}
```

위와 같이 `AppConfig`라는 클래스를 설정 클래스로 생성하여 **역할에 대한 구현 클래스를 지정하고 각각의 클래스들의 
연관 관계를 설정해 줌으로 관심사를 분리할 수 있었다.**


기존과 비교해보자면 클라이언트가 **의존하는 역할과 구현을 직접 생성하여 사용하였다면** 변경된 점은 클라이언트는 **인터페이스를 의존하며
의존하는 역할이 어떠한 것으로 구현되었는지 알 필요 없고 외부에서 받는 것을 그대로 사용만 하면 된다.** (DIP)

대표적인 예로 추가적으로 `DiscountPolicy`가 변경되더라도 **클라이언트 코드 수정 없이 설정 코드만 변경해주면 된다.** (OCP)

이로 인해 클라이언트는 자신의 역할에만 집중을 할 수 있으며 **역할과 구현을 생성하고 연결하는 책임은 AppConfig가 하게 되었다.**(SRP)


