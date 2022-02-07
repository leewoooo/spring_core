package hello.corenoref.service;

import hello.corenoref.member.Grade;
import hello.corenoref.member.Member;
import hello.corenoref.service.member.MemberService;
import hello.corenoref.service.member.MemberServiceImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceImplTest {

    MemberService memberService = new MemberServiceImpl();

    @Test
    void joinAndFindById() {
        //given
        Member member = new Member(1L,"foobar",Grade.VIP);

        //when
        memberService.join(member);

        //then
        Member selected = memberService.findMember(1L);

        assertThat(selected.getId()).isEqualTo(1L);
        assertThat(selected.getName()).isEqualTo("foobar");
        assertThat(selected.getGrade()).isEqualTo(Grade.VIP);
    }
}