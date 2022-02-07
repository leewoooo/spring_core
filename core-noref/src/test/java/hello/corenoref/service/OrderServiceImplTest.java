package hello.corenoref.service;

import hello.corenoref.AppConfig;
import hello.corenoref.member.Grade;
import hello.corenoref.member.Member;
import hello.corenoref.order.Order;
import hello.corenoref.service.member.MemberService;
import hello.corenoref.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceImplTest {

    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    void beforeEach(){
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }

    @Test
    void createOrder_VIP(){
        //given
        Member newMember = new Member(1L, "foobar", Grade.VIP);
        memberService.join(newMember);

        //when
        Order newOrder = orderService.createOrder(newMember, "itemA", 10000);

        //then
        assertThat(newOrder.getMemberId()).isEqualTo(1L);
        assertThat(newOrder.getItemName()).isEqualTo("itemA");
        assertThat(newOrder.getItemPrice()).isEqualTo(10000);
        assertThat(newOrder.getDiscountPrice()).isEqualTo(1000);
    }

    @Test
    void createOrder_BASIC(){
        //given
        Member newMember = new Member(1L, "foobar", Grade.BASIC);
        memberService.join(newMember);

        //when
        Order newOrder = orderService.createOrder(newMember, "itemA", 10000);

        //then
        assertThat(newOrder.getMemberId()).isEqualTo(1L);
        assertThat(newOrder.getItemName()).isEqualTo("itemA");
        assertThat(newOrder.getItemPrice()).isEqualTo(10000);
        assertThat(newOrder.getDiscountPrice()).isEqualTo(0);
    }
}