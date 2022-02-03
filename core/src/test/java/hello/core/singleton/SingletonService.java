package hello.core.singleton;

public class SingletonService {

    // statice 영역에 instance가 올라간다. java가 실행할 때 올라간다.
    private static final SingletonService instance = new SingletonService();

    // instance를 조회할 때는 getter를 이용하여 instance를 이용, 이 method를 통해서만 조회를 허용한다.
    public static SingletonService getInstance(){
        return instance;
    }

    // 외부에서 또 생성을 할 수 없도록 생성자를 private으로 막는다.
    private SingletonService() {}
}
