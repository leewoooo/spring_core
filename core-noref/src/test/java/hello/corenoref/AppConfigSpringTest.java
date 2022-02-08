package hello.corenoref;

import hello.corenoref.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class AppConfigSpringTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("스프링 컨테이너를 통한 DI 싱글톤을 스프링 컨테이너가 해준다.")
    void singleton() {
        //given
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        AppConfig bean = ac.getBean(AppConfig.class);
        System.out.println("bean = " + bean);

        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        //when
        assertThat(memberService1).isSameAs(memberService2);
    }
}
