package kg.erudit.common.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"items"})
public class GetListResponse<T> extends DefaultServiceResponse {
    private List<T> items;

    @Override
    public String toString() {
        return "GetListResponse{" +
                "items=" + items +
                ", error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}
