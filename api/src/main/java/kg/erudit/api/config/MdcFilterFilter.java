package kg.erudit.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Log4j2
public class MdcFilterFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        MDC.clear();
        if (!request.getRequestURI().startsWith("/swagger") && !request.getRequestURI().startsWith("/v3/api-docs")) {
            String uuid = request.getHeader("transactionId") != null ?
                    request.getHeader("transactionId") : UUID.randomUUID().toString().replace("-", "");
            log.info("Setting transactionId: {} for incoming request from IP: {}", uuid, request.getRemoteAddr());
            MDC.put("transactionId", uuid);
        }
        chain.doFilter(request, response);
    }
}
