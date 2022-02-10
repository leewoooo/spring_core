package hello.core.service;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogDemoService {
    //    private final ObjectProvider<MyLogger> loggerObjectProvider;
    private final MyLogger myLogger;

    public void logic(){
//        MyLogger myLogger = loggerObjectProvider.getObject();
        myLogger.log("service test");
    }
}
