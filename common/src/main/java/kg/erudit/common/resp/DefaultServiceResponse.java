package kg.erudit.common.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"error","message"})
public class DefaultServiceResponse {
    protected boolean error = false;
    protected String message;

    public DefaultServiceResponse(String message) {
        this.message = message;
    }

    public DefaultServiceResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    @Override
    public String toString() {
        return "DefaultServiceResponse{" +
                "error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}
