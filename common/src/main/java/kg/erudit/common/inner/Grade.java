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
@JsonPropertyOrder({"gradeTypeId","value"})
public class Grade {
    protected Integer gradeTypeId;
    protected Integer value;

    @Override
    public String toString() {
        return "Grade{" +
                "gradeTypeId=" + gradeTypeId +
                ", value=" + value +
                '}';
    }
}
