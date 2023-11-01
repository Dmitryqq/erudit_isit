package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","name"})
public class Subject {
    private Integer id;
    private String name;
    private Boolean teacherRequired;

    public Subject(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Subject(Integer id, String name, Boolean teacherRequired) {
        this.id = id;
        this.name = name;
        this.teacherRequired = teacherRequired;
    }
}
