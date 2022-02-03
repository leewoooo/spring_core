package hello.core.member;

import hello.core.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberServiceTest {
    MemberService memberService;

    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }

    @Test
    void join() {
        //given
        Member newMember = new Member(1L, "spring", Grade.VIP);

        //when
        memberService.join(newMember);
        Member selectedMember = memberService.findMember(1L);

        //then
        assertThat(selectedMember.getId()).isEqualTo(1L);
        assertThat(selectedMember.getName()).isEqualTo("spring");
        assertThat(selectedMember.getGrade()).isEqualTo(Grade.VIP);
    }
}
