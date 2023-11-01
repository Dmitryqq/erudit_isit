package kg.erudit.common.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetPasswordRequest {
    @NotBlank(message = "Поле 'Пароль' не может быть пустым")
    private String oldPassword;
    @NotBlank(message = "Поле 'Пароль' не может быть пустым")
    private String newPassword;

    @Override
    public String toString() {
        return "SetPasswordRequest{" +
                "oldPassword='" + (oldPassword != null ? "..." : null) + '\'' +
                ", newPassword='" + (newPassword != null ? "..." : null) + '\'' +
                '}';
    }
}
