package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","classId","studentId","trimesterId","startDate","endDate"})
public class Schedule {
    protected Integer id;
    protected Integer classId;
    protected Integer studentId;
    protected Integer trimesterId;
    protected String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Asia/Bishkek")
    protected Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Asia/Bishkek")
    protected Date endDate;

    public Schedule(Integer classId, Integer trimesterId) {
        this.classId = classId;
        this.trimesterId = trimesterId;
    }

    public Schedule(Integer id, Integer classId, Integer studentId, Integer trimesterId, String status, Date startDate, Date endDate) {
        this.id = id;
        this.classId = classId;
        this.studentId = studentId;
        this.trimesterId = trimesterId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", classId=" + classId +
                ", studentId=" + studentId +
                ", trimesterId=" + trimesterId +
                ", status='" + status + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
