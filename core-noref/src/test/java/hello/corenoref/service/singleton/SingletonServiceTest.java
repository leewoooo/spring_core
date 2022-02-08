package hello.corenoref.service.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SingletonServiceTest {

    @Test
    @DisplayName("싱글톤 패턴 테스트")
    void singleton(){
        //given
        SingletonService instance1 = SingletonService.getInstance();
        SingletonService instance2 = SingletonService.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }
}