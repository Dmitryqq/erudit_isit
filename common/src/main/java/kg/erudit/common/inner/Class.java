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
@JsonPropertyOrder({"id","number","letter"})
public class Class {
    private Integer id;
    private Integer number;
    private String letter;

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", number=" + number +
                ", letter='" + letter + '\'' +
                '}';
    }
}
