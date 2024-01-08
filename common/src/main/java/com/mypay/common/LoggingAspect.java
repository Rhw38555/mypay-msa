package com.mypay.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final LoggingProducer loggingProducer;

    public LoggingAspect(LoggingProducer loggingProducer) {
        this.loggingProducer = loggingProducer;
    }

    // mypay 하위 어댑터(컨트롤러)에 있는 모든 메소드가 실행되기 이전에 실행
    @Before("execution(* com.mypay.*.adapter.in.web.*.*(..))")
    public void beforeMethodExecution(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();

        // key value 전달
        loggingProducer.sendMessage("logging", "Before executing method: " + methodName);

        // logging
    }
}
