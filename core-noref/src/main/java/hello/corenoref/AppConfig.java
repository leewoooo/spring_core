package hello.corenoref;

import hello.corenoref.repository.MemberRepository;
import hello.corenoref.repository.MemoryMemberRepository;
import hello.corenoref.service.discount.DiscountPolicy;
import hello.corenoref.service.discount.RateDiscountPolicy;
import hello.corenoref.service.member.MemberService;
import hello.corenoref.service.member.MemberServiceImpl;
import hello.corenoref.service.order.OrderService;
import hello.corenoref.service.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }

    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public DiscountPolicy discountPolicy(){
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    @Bean
    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
}
