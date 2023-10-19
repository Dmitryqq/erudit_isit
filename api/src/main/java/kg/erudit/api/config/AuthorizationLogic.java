package kg.erudit.api.config;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

@Component("authz")
public class AuthorizationLogic {
    public boolean check(MethodSecurityExpressionOperations operations) {
        // ... authorization logic
        CustomAuthToken auth = (CustomAuthToken) operations.getAuthentication();
//        System.out.printf("");
//        operations.getReturnObject()
        return !auth.getPwdChangeRequired();
    }
}
