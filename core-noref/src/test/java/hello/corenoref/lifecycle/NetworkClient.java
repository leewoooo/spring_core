package hello.corenoref.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//public class NetworkClient implements InitializingBean, DisposableBean {
public class NetworkClient{

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출 url: " + url);
//        connect();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void connect(){
        System.out.println("connect = " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + "message = " + message);
    }

    public void disconnect(){
        System.out.println("close: " + url);
    }

//    // 의존관계 주입이 완료 된 이후 시점
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        connect();
//        call("초기화 연결 메세지");
//    }
//
//    // 스프링이 종료되기 전 시점
//    @Override
//    public void destroy() throws Exception {
//        disconnect();
//    }
//

//    // 주입이 완료 된 이후 시점
//    public void init() {
//        connect();
//        call("초기화 연결 메세지");
//    }
//
//    // 스프링이 종료되기 전 시점
//    public void destroy() {
//        disconnect();
//    }

    // 주입이 완료 된 이후 시점
    @PostConstruct
    public void init() {
        connect();
        call("초기화 연결 메세지");
    }

    // 스프링이 종료되기 전 시점
    @PreDestroy
    public void destroy() {
        disconnect();
    }
}
