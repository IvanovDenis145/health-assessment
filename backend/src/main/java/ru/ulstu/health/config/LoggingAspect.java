package ru.ulstu.health.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Точка среза: все методы в пакете service.
     */
    @Pointcut("execution(* ru.ulstu.health.service..*(..))")
    public void serviceMethods() {
    }

    /**
     * Логирование входа в метод и аргументов.
     */
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("→ Вызов метода: {} | аргументы: {}", method, Arrays.toString(args));
    }

    /**
     * Логирование успешного завершения и результата.
     */
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();
        log.info("← Метод завершён: {} | результат: {}", method, result);
    }

    /**
     * Логирование исключений.
     */
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        String method = joinPoint.getSignature().toShortString();
        log.error("✗ Ошибка в методе: {} | исключение: {}", method, ex.getMessage(), ex);
    }

    /**
     * Замер времени выполнения метода.
     */
    @Around("serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            String method = joinPoint.getSignature().toShortString();
            log.debug("⏱ Метод {} выполнен за {} мс", method, duration);
        }
    }
}