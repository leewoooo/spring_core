package hello.corenoref.service.order;

import hello.corenoref.member.Member;
import hello.corenoref.order.Order;
import hello.corenoref.repository.MemberRepository;
import hello.corenoref.service.discount.DiscountPolicy;

public class OrderServiceImpl implements OrderService{

    // OrderService 또한 MemberService처럼 memberRepository와 discountPolicy를 직접 의존하고 있음 DIP 위반
    // OCP 또한 위반 구현체 변경 시 코드를 직접 수정해야 함.
//    private final MemberRepository memberRepository = new MemoryMemberRepository();

    //    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
//    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    // interface에만 의존을 하고 구현 클래스는 외부에서 주입을 받음으로 DIP를 지킬 수 있다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Member member, String itemName, int price) {
        // find Member
        Member selected = memberRepository.findById(member.getId());

        // discount
        int discountPrice = discountPolicy.discount(selected, price);

        // createOrder
        return new Order(selected.getId(), itemName, price, discountPrice);
    }
}
