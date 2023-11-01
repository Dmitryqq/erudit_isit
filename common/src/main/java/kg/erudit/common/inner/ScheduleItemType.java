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
@JsonPropertyOrder({"id","name","durationMin"})
public class ScheduleItemType {
    private Integer id;
    private String name;
    private Integer durationMin;

    public ScheduleItemType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ScheduleItemType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", durationMin=" + durationMin +
                '}';
    }
}
