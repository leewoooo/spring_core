package hello.corenoref.scope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
        bean1.addCount();

        assertThat(bean1.getCount()).isEqualTo(1);

        PrototypeBean bean2 = ac.getBean(PrototypeBean.class);
        bean2.addCount();

        assertThat(bean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUseProtoType() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ClientBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);
    }

    // 현재는 싱글톤 인스턴스에 prototypeBean이 종속
    @Scope("singleton")
    static class ClientBean {
        private final PrototypeBean prototypeBean;

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic(){
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    // provider를 이용하여 종속되지 않게 사용할 수 있다.
    @Test
    void singletonClientUseProtoTypeWithProvider() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ClientBeanObjectProvider.class);

        ClientBeanObjectProvider bean = ac.getBean(ClientBeanObjectProvider.class);
        int count1 = bean.logic();
        assertThat(count1).isEqualTo(1);

        ClientBeanObjectProvider bean2 = ac.getBean(ClientBeanObjectProvider.class);
        int count2 = bean2.logic();
        assertThat(count2).isEqualTo(1);
    }

    // provider를 이용하여 필요할 때마다 컨테이너에 DL
    @Scope("singleton")
    static class ClientBeanObjectProvider {
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;

        public int logic(){
            // provider에서 찾을 때 새로 생성해서 반환해준다. 찾아주는 기능만 지원
            PrototypeBean prototypeBean = prototypeBeanObjectProvider.getObject();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    // provider를 이용하여 종속되지 않게 사용할 수 있다.
    @Test
    void singletonClientUseProtoTypeWithJavaXProvider() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ClientBeanJavaXProvider.class);

        ClientBeanJavaXProvider bean = ac.getBean(ClientBeanJavaXProvider.class);
        int count1 = bean.logic();
        assertThat(count1).isEqualTo(1);

        ClientBeanJavaXProvider bean2 = ac.getBean(ClientBeanJavaXProvider.class);
        int count2 = bean2.logic();
        assertThat(count2).isEqualTo(1);
    }

    @Scope("singleton")
    static class ClientBeanJavaXProvider{
        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic(){
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("prototype")
    static class PrototypeBean {
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean init: " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }


        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }
}
