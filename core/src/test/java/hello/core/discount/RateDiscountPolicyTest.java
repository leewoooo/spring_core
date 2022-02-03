package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RateDiscountPolicyTest {

    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다") // JUnit5부터 지원하는 Annotation. 테스트 이름을 한글로 지정 가능.
    void vip_o() {
        //given
        Member newMember = new Member(1L, "foobarVIP", Grade.VIP);

        //when
        int discountPrice = discountPolicy.discount(newMember, 10000);

        //then
        assertThat(discountPrice).isEqualTo(1000);
    }

    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다") // JUnit5부터 지원하는 Annotation. 테스트 이름을 한글로 지정 가능.
    void vip_x() {
        //given
        Member newMember = new Member(2L, "foobarBASIC", Grade.BASIC);

        //when
        int discountPrice = discountPolicy.discount(newMember, 10000);

        //then
        assertThat(discountPrice).isEqualTo(0);
    }
}