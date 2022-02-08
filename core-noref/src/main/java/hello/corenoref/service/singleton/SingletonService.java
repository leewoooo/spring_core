package hello.corenoref.service.singleton;

public class SingletonService {

    // 1. static 영역에 인스턴스 1개 생성
    private static final SingletonService singletonService = new SingletonService();

    // 2. 외부에서 생성자를 호출 할 수 없도록 private으로 설정
    private SingletonService() {
    }

    // 3. static 영역에 instance를 가져오기 위한 getter만 생성
    public static SingletonService getInstance() {
        return singletonService;
    }
}
