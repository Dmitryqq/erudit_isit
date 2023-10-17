package kg.erudit.common.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"token"})
public class AuthResponse extends DefaultServiceResponse {
    private String token;

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + token + '\'' +
                ", error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}
