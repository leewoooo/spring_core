package hello.corenoref.service.discount;

import hello.corenoref.member.Grade;
import hello.corenoref.member.Member;
import org.springframework.stereotype.Component;

@Component
public class RateDiscountPolicy implements DiscountPolicy {

    private final int RATE_DISCOUNT_PERCENT = 10;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            return price * RATE_DISCOUNT_PERCENT / 100;
        }

        return 0;
    }
}
