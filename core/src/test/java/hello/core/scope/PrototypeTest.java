package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

public class PrototypeTest {

    // prototype은 생성 및 의존관계 주입, 초기화 까지만 해주고 스프링 컨테이너가 관리하지 않기 때문에 소멸 콜백은 클라이언트가 직접 호출해줘야 한다.
    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        System.out.println("find PrototypeBean1");
        PrototypeBean bean1 = ac.getBean(PrototypeBean.class); // 조회할 때 생성하여 return한다.

        System.out.println("find PrototypeBean2");
        PrototypeBean bean2 = ac.getBean(PrototypeBean.class);

        // 인스턴스의 주소값이 다름
        //bean1 = hello.core.scope.PrototypeTest$PrototypeBean@4f0100a7
        //bean2 = hello.core.scope.PrototypeTest$PrototypeBean@3cdf2c61
        System.out.println("bean1 = " + bean1);
        System.out.println("bean2 = " + bean2);

        assertThat(bean1).isNotSameAs(bean2);
        ac.close();

        // 이와 같이 클라이언트 코드가 직접 관리하여야 한다.
        bean1.close();
        bean2.close();
    }

    // @Component가 없어도 등록이 되는 이유는 new AnnotationConfigApplicationContext()에 넣어주면 @Component처럼 동작하여 Bean으로 등록
    @Scope("prototype")
    static class PrototypeBean {
        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init");
        }

        // prototype이라 호출되지 않음.
        @PreDestroy
        public void close() {
            System.out.println("SingletonBean.close");
        }
    }
}
