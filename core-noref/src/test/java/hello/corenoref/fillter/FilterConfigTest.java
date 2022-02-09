package hello.corenoref.fillter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class FilterConfigTest {

    ApplicationContext ac = new AnnotationConfigApplicationContext(FilterConfig.class);

    @Test
    void filterTest() {
        BeanA bean = ac.getBean(BeanA.class);
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean(BeanB.class));
    }
}
