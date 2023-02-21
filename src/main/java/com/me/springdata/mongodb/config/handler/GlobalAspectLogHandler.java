package com.me.springdata.mongodb.config.handler;

import com.me.springdata.mongodb.exception.ValidCustomException;
import com.me.springdata.mongodb.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Component
@Aspect
@Log4j2
public class GlobalAspectLogHandler {
    @Around("within(com.me.springdata.mongodb.*.*Controller)")
    public Object aroundLogging(ProceedingJoinPoint point) {

        Object resultVal;
        long start = System.currentTimeMillis();
        try {
            resultVal = point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new ValidCustomException(throwable.getMessage());
        }

        long processTime = System.currentTimeMillis() - start;

        Object[] params = point.getArgs();

        String paramMessage = Arrays.stream(params).map(JsonUtils::toJson).collect(joining(", "));

        log.info("---------------------------------------------------------------------------------------------------------------------------");
        log.info("Processing Time({}) : {} ms", point.getSignature().toShortString(), processTime);
        log.info("Param : {}", paramMessage);
        log.info("Result : {}", JsonUtils.toJson(resultVal));
        log.info("---------------------------------------------------------------------------------------------------------------------------");

        return resultVal;
    }
}
