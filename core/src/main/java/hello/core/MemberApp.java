package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();

        // AppConfig에 있는 설정 정보를 통해 Spring이 Bean으로 등록하여 관리해준다.
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        // 기본적으로 이름은 method명으로 등록되고 두번째 파라미터는 class type을 넣어준다.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member newMember = new Member(1L, "springA", Grade.VIP);
        memberService.join(newMember);

        Member selectedMember = memberService.findMember(1L);
        System.out.println("newMember = " + newMember.getName());
        System.out.println("selectedMember = " + selectedMember.getName());
    }
}
