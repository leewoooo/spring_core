package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA A사용자 10000원 주문
        statefulService1.order("userA",10000);

        // ThreadA B사용자 20000원 주문
        statefulService2.order("userB",20000);

        // ThreadA A사용자 주문 금액 조회
        int price = statefulService1.getPrice();

        System.out.println("price = " + price);

        // 상태가 있는 인스턴스를 이용하기 때문에 공유되는 변수의 값은 예상 밖의 값이 나올수 있다.
        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

}