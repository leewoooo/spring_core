package hello.core.Autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredOptionTest {

    @Test
    void autoWiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
    }

    static class TestConfig {
        @Autowired(required = false)
        public void setNoBean(Member member) {
            System.out.println("setNoBean member = " + member);
        }

        @Autowired
        public void setNoBean2(@Nullable Member member) {
            System.out.println("setNoBean2 member = " + member);
        }

        @Autowired
        public void setNoBean3(Optional<Member> member) {
            System.out.println("setNoBean3 member = " + member);
        }

    }
}
