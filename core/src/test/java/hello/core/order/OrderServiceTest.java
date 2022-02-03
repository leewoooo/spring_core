package hello.core.order;

import hello.core.AppConfig;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest {
    MemberService memberService;
    OrderService orderService;


    @BeforeEach
    public void beforeEach(){
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }

    @Test
    void createOrder() {
        //given
        Member newMember = new Member(1L, "foobar", Grade.VIP);
        memberService.join(newMember);

        //when
        Order newOrder = orderService.createOrder(newMember.getId(), "itemA", 10000);

        //then
        assertThat(newOrder.getDiscountPrice()).isEqualTo(1000);
        assertThat(newOrder.getItemName()).isEqualTo("itemA");
    }
}