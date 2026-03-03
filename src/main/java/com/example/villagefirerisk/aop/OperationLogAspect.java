package com.example.villagefirerisk.aop;

import com.example.villagefirerisk.dto.ApiResponse;
import com.example.villagefirerisk.entity.OperationLog;
import com.example.villagefirerisk.entity.User;
import com.example.villagefirerisk.repository.OperationLogRepository;
import com.example.villagefirerisk.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogRepository operationLogRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    public OperationLogAspect(OperationLogRepository operationLogRepository,
                              UserRepository userRepository,
                              HttpServletRequest request,
                              ObjectMapper objectMapper) {
        this.operationLogRepository = operationLogRepository;
        this.userRepository = userRepository;
        this.request = request;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(operationLoggable)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLoggable operationLoggable) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        Integer code = 0;
        try {
            result = joinPoint.proceed();
            if (result instanceof ApiResponse<?> apiResponse) {
                code = apiResponse.getCode();
            }
            return result;
        } catch (Throwable ex) {
            code = 500;
            throw ex;
        } finally {
            OperationLog log = new OperationLog();
            log.setModule(operationLoggable.module());
            log.setOperation(operationLoggable.operation());
            log.setMethod(request.getMethod());
            log.setRequestUri(request.getRequestURI());
            log.setRequestParams(buildParams(joinPoint.getArgs()));
            log.setResultCode(code);
            log.setDurationMs(System.currentTimeMillis() - start);
            log.setIpAddress(request.getRemoteAddr());
            log.setUserAgent(request.getHeader("User-Agent"));
            String username = request.getUserPrincipal() == null ? null : request.getUserPrincipal().getName();
            if (username != null) {
                log.setUsername(username);
                User user = userRepository.findByUsername(username).orElse(null);
                if (user != null) {
                    log.setUserId(user.getId());
                }
            }
            operationLogRepository.save(log);
        }
    }

    private String buildParams(Object[] args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}
