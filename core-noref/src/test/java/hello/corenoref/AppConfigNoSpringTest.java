package hello.corenoref;

import hello.corenoref.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AppConfigNoSpringTest {

    @Test
    @DisplayName("스프링 없이 순수한 DI 테스트")
    void noSpringDI(){
        //given
        AppConfig appConfig = new AppConfig();

        //when
        MemberRepository memberRepository1 = appConfig.memberRepository();
        MemberRepository memberRepository2 = appConfig.memberRepository();

        //then
        Assertions.assertThat(memberRepository1).isNotSameAs(memberRepository2);
    }
}
