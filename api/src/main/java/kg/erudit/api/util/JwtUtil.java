package kg.erudit.api.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import kg.erudit.common.inner.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Log4j2
public class JwtUtil {
    private static final String HEADER = "Authorization";
    private static final  String PREFIX = "Bearer ";
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

    public static boolean checkJwtToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(PREFIX);
    }

    public static boolean checkJwtToken(StompHeaderAccessor accessor) {
        String authenticationHeader = accessor.getFirstNativeHeader(HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(PREFIX);
    }

    public static Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
//        return jwtParser.parseClaimsJws(jwtToken).getBody();
        try {
            return jwtParser.parseClaimsJws(jwtToken).getBody();
        } catch (SecurityException | ExpiredJwtException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static Claims validateToken(StompHeaderAccessor accessor) {
        String jwtToken = accessor.getFirstNativeHeader(HEADER).replace(PREFIX, "");
        try {
            return jwtParser.parseClaimsJws(jwtToken).getBody();
        } catch (SecurityException e) {
            return null;
        }
    }

    public static String getJwtToken(User user) {
//        System.out.println(new String(Base64.getEncoder().encode(key.getEncoded())));
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("username", user.getUsername())
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .claim("patronymic", user.getPatronymic())
                .claim("role", user.getRole().getCode())
                .claim("pwdChangeRequired", user.getPwdChangeRequired())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7200000))
                .signWith(key).compact();
    }
}
