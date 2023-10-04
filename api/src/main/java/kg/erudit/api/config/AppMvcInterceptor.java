package kg.erudit.api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@Log4j2
public class AppMvcInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.clear();
        String uuid = request.getHeader("transactionId") != null ?
                request.getHeader("transactionId") : UUID.randomUUID().toString().replace("-", "");
        log.info("Setting transactionId: {} for incoming request from IP: {}", uuid, request.getRemoteAddr());
        MDC.put("transactionId", uuid);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
