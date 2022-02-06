package hello.corenoref.service;

import hello.corenoref.member.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
