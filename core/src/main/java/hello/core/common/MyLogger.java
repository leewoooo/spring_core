package hello.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Component
@Scope(value = "request",proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    private String uuid;
    private String requestURl;

    public void setRequestURl(String requestURl) {
        this.requestURl = requestURl;
    }

    public void log(String message) {
        System.out.println("[ " + uuid + " ] [" + requestURl + "] [ " + message + " ]");
    }

    @PostConstruct
    public void init(){
        this.uuid = UUID.randomUUID().toString();
        System.out.println("[ " + uuid + " ] [" + requestURl + "] [ init ]");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("[ " + uuid + " ] [" + requestURl + "] [ destory ]");
    }
}
