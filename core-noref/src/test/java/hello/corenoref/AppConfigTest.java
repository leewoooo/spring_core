package hello.corenoref;

import hello.corenoref.member.Member;
import hello.corenoref.repository.MemberRepository;
import hello.corenoref.repository.MemoryMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppConfigTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);

            System.out.println("name: " + beanDefinitionName + "object: " + bean);
        }
    }

    @Test
    @DisplayName("내가 정의한 Bean만 조회하기")
    void findAllApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        //Role ROLE_APPLICATION: 직접 등록한 애플리케이션 빈
        //Role ROLE_INFRASTRUCTURE: 스프링이 내부에서 사용하는 빈
        // getBeanDefinition를 사용하려면 AnnotationConfigApplicationContext를 생성해야 한다.
        for (String beanDefinitionName : beanDefinitionNames) {
            if (ac.getBeanDefinition(beanDefinitionName).getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name: " + beanDefinitionName + "object: " + bean);
            }
        }
    }

    @Test
    @DisplayName("Bean 타입으로 조회하기")
    void findBeanByType() {
        MemberRepository bean = ac.getBean(MemberRepository.class);
        assertThat(bean).isInstanceOf(MemberRepository.class);
    }

    @Test
    @DisplayName("Bean 타입과 이름으로 조회하기")
    void findBeanByTypeAndName() {
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);
        assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }

    @Test
    @DisplayName("Bean이 존재하지 않을 경우")
    void findBeanNotExist() {
        assertThrows(NoSuchBeanDefinitionException.class, () -> {
            ac.getBean(Member.class);
        });
    }

    @Configuration
    static class DuplicateConfig {

        @Bean
        public MemberRepository memberRepository1() {
            return new MemoryMemberRepository();
        }

        @Bean
        public MemberRepository memberRepository2() {
            return new MemoryMemberRepository();
        }
    }

    AnnotationConfigApplicationContext ac2 = new AnnotationConfigApplicationContext(DuplicateConfig.class);

    @Test
    @DisplayName("빈이 중복 될 때")
    void duplicateBean() {
        assertThrows(NoUniqueBeanDefinitionException.class, () -> {
            ac2.getBean(MemberRepository.class);
        });
    }

    @Test
    @DisplayName("타입으로 등록된 빈이 중복될 때 이름을 부여하여 조회할 수 있다.")
    void findBeanByName(){
        MemberRepository memberRepository = ac2.getBean("memberRepository1", MemberRepository.class);
        assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }

    @Test
    @DisplayName("타입으로 등록 된 모든 빈 조회")
    void findAllBeanByType(){
        Map<String, MemberRepository> beansOfType = ac2.getBeansOfType(MemberRepository.class);

        for (String BeanName : beansOfType.keySet()) {
            System.out.println("BeanName = " + BeanName);
        }

        assertThat(beansOfType.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("부모타입으로 조회 할 때 자식 타입 모두를 조회할 수 있다.")
    void findAllByParentType(){
        Map<String, Object> beansOfType = ac2.getBeansOfType(Object.class);

        for (String BeanName : beansOfType.keySet()) {
            System.out.println("BeanName = " + BeanName);
        }
    }

}