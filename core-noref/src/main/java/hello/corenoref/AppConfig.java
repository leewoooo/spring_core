package hello.corenoref;

import hello.corenoref.repository.MemberRepository;
import hello.corenoref.repository.MemoryMemberRepository;
import hello.corenoref.service.discount.DiscountPolicy;
import hello.corenoref.service.discount.RateDiscountPolicy;
import hello.corenoref.service.member.MemberService;
import hello.corenoref.service.member.MemberServiceImpl;
import hello.corenoref.service.order.OrderService;
import hello.corenoref.service.order.OrderServiceImpl;

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
