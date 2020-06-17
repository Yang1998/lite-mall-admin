package com.yyt.axios.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {
    private String join(Object[] args) {
        if(args == null || args.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            if(arg instanceof String) {
                sb.append("'").append(arg).append("'").append(",");
            } else {
                sb.append(arg).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Pointcut("execution(* com.yyt.axios..*(..))")
    private void pointCut(){}

    @Around(value = "pointCut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] args = proceedingJoinPoint.getArgs();
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info(String.format("%s.%s(%s) 执行了 %d ms", className, methodName, join(args), end - start));
        return result;
    }
}
