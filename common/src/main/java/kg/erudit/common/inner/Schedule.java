package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","classId","trimesterId","startDate","endDate"})
public class Schedule {
    protected Integer id;
    @JsonProperty("classId")
    protected Integer clazz;
    protected Integer trimesterId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Asia/Bishkek")
    protected Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Asia/Bishkek")
    protected Date endDate;

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", clazz=" + clazz +
                ", trimesterId=" + trimesterId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
