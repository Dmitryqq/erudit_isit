package kg.erudit.api.config;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthToken extends UsernamePasswordAuthenticationToken {
    @Getter
    private Integer userId;
    @Getter
    private Boolean pwdChangeRequired;

    public CustomAuthToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Boolean pwdChangeRequired, Integer userId) {
        super(principal, credentials, authorities);
        this.pwdChangeRequired = pwdChangeRequired;
        this.userId = userId;
    }
}
