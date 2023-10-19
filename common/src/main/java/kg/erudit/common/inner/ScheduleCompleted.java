package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleCompleted extends Schedule {
    private List<ScheduleDay> days = new LinkedList<>();

    public ScheduleCompleted(Integer classId, Integer trimesterId) {
        super(classId, trimesterId);
    }

    @Override
    public String toString() {
        return "ScheduleCompleted{" +
                "days=" + days +
                ", id=" + id +
                ", classId=" + classId +
                ", trimesterId=" + trimesterId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
