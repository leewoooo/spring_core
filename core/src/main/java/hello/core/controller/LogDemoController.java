package hello.core.controller;

import hello.core.common.MyLogger;
import hello.core.service.LogDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class LogDemoController {

//    private final ObjectProvider<MyLogger> loggerObjectProvider;
    private final MyLogger myLogger;
    private final LogDemoService logDemoService;

    @GetMapping("/log/demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request){
//        MyLogger myLogger = loggerObjectProvider.getObject();
        myLogger.setRequestURl(request.getRequestURI());
        myLogger.log("controller test");

        logDemoService.logic();
        return "OK";
    }
}
