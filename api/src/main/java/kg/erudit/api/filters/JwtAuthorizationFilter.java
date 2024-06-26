package kg.erudit.api.filters;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.erudit.api.config.CustomAuthToken;
import kg.erudit.api.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!JwtUtil.checkJwtToken(request)) {
            chain.doFilter(request, response);
            return;
        }
        Claims claims = JwtUtil.validateToken(request);
        if (claims == null || claims.get("role") == null) {
            chain.doFilter(request, response);
            return;
        }

        setUpSpringAuthentication(claims);
        chain.doFilter(request, response);
    }

    private void setUpSpringAuthentication(Claims claims) {
        String role = claims.get("role").toString();
        List<String> roles = new ArrayList<>();
        roles.add(role);
        Boolean pwdChangeRequired = (Boolean) claims.get("pwdChangeRequired");
        Integer userId = (Integer) claims.get("id");

        log.info("Authorized user '{}' with role '{}', pwdChangeRequired {}", claims.getSubject(), role, pwdChangeRequired);
        CustomAuthToken auth = new CustomAuthToken(claims.getSubject(), null,
                roles.stream().map(SimpleGrantedAuthority::new).toList(), pwdChangeRequired, userId);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
