package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","scheduleItemId","name","students"})
public class ClassItem {
    private Integer id;
    private Integer scheduleItemId;
    private String name;
    @JsonIgnore
    private Map<Integer, StudentItem> studentItemMap;

    public ClassItem(Integer id, String name) {
        this.id = id;
        this.name = name;
        if (this.studentItemMap == null)
            this.studentItemMap = new LinkedHashMap<>();
    }

    @JsonGetter("students")
    public List<StudentItem> getStudents() {
        return studentItemMap.values().stream().toList();
    }

    @Override
    public String toString() {
        return "ClassItem{" +
                "id=" + id +
                ", scheduleItemId=" + scheduleItemId +
                ", name='" + name + '\'' +
                ", studentItemMap=" + studentItemMap +
                '}';
    }
}
