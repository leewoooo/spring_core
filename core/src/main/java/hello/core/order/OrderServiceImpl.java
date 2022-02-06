package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        // 회원 찾기
        Member selectedMember = memberRepository.findById(memberId);

        // 회원에 따른 할인금액 얻기
        int discountPrice = discountPolicy.discount(selectedMember, itemPrice);

        // 새로운 주문 생성하여 return
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // test용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
