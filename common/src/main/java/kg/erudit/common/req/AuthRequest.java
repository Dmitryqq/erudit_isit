package kg.erudit.common.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Поле 'Логин' не может быть пустым")
    private String username;
    @NotBlank(message = "Поле 'Пароль' не может быть пустым")
    private String password;

    @Override
    public String toString() {
        return "AuthRequest{" +
                "username='" + username + '\'' +
                ", password='" + (password != null ? "..." : null) + '\'' +
                '}';
    }
}
