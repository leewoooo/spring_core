package hello.corenoref.service.discount;

import hello.corenoref.member.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
