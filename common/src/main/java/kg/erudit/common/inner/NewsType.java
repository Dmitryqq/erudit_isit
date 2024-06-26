package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","name"})
public class NewsType {
    private Integer id;
    private String name;

    @Override
    public String toString() {
        return "NewsType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
