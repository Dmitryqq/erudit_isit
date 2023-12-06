package kg.erudit.common.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"item","notFound"})
public class SingleItemResponse<T> extends DefaultServiceResponse {
    private T item;
    private Boolean notFound;

    public SingleItemResponse(T item) {
        if (item != null)
            this.item = item;
    }

    public SingleItemResponse(T item, String message) {
        super(message);
        if (item != null)
            this.item = item;
        else
            this.notFound = true;
    }

    @Override
    public String toString() {
        return "SingleItemResponse{" +
                "item=" + item +
                ", notFound=" + notFound +
                ", error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}
