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
    protected String gradeTypeName;
    protected Integer value;

    public Grade(Integer gradeTypeId, Integer value) {
        this.gradeTypeId = gradeTypeId;
        this.value = value;
    }

    public Grade(String gradeTypeName, Integer value) {
        this.gradeTypeName = gradeTypeName;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeTypeId=" + gradeTypeId +
                ", value=" + value +
                '}';
    }
}
