스프링으로 전환하기
===

`ApplicationContext`를 스프링 컨테이너라고 하며 `ApplicationContext`는 인터페이스이다.

## 스프링 컨테이너의 생성 과정

1. 스프링 컨테이너를 생성 
2. 설정 정보를 이용하여 @Bean을 등록한다. (Bean을 등록할 때 Bean이름과 인스턴스를 같이 저장하며 이름은 지정하지 않으면 method명이 된다. @Bean(name = ${}) 으로 이름 지정 가능)
3. 스프링 Bean 의존관계 설정 준비
4. 스프링 Bean 의존관계 설정 완료 (스프링 컨테이너는 설정 정보를 참고해서 의존관계 주입을 한다. - DI)

>스프링은 빈을 생성하고, 의존관계를 주입하는 단계가 나누어져 있다. 그런데 이렇게 자바 코드로 스프링 빈을 등록하면 생성자를 호출하면서 의존관계 주입도 한번에 처리된다.


## Bean을 조회하기.

`ApplicationContext`에서 `getBean()` API를 이용하여 컨테이너 안에 있는 Bean을 조회할 수 있다.

조회하는 방법은 3가지이다.

1. getBean(조회 할 Type); - 역할 타입으로 주로 조회하고 구현 타입으로는 조회를 지양한다.
2. getBean(조회 할 Bean이름, 조회 할 Type); - Bean이름과 타입으로 조회할 수 있으며 조회하려는 타입으로 등록된 빈이 여러개 일 때 이름을 추가해 조회한다
3. getBeansOfType(조회 할 Type); - 타입으로 조회하면 해당 타입을 구현하고 있는 모든 자식을 조회할 수 있다.

만약 조회하려는 Bean이 존재하지 않을 경우 `NoSuchBeanDefinitionException`이 발생하며

조회하려는 Bean이 2개 이상일 경우 `NoUniqueBeanDefinitionException`이 발생한다.

## BeanFactory와 ApplicationContext

`BeanFactory <- ApplicationContext <- AnnotationConfigApplication`

### BeanFactory

- 스프링 컨테이너의 최상위 인터페이스이다.
- 스프링 빈을 관리하고 조회하는 역할을 담당한다.
- `getBean()`을 제공한다.
- 조회하고 하는 기능들은 대부분 BeanFactory에서 제공한다.

### ApplicationContext

- BeanFactory 기능을 모두 상속받아 제공한다.
- 빈을 관리하고 검색하는 기능을 BeanFactory가 제공해주는데 그러면 왜쓰냐?
- 애플리케이션을 개발할 때는 빈은 관리하고 조회하는 기능이고 수 많은 부가기능이 필요하다. (메세지 소스, 환경변수, 애플리케이션 이벤트, 편리한 리소스 조회 기능이 포함되어 있음.)


