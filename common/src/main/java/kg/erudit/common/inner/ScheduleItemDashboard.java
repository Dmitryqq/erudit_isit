package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","typeId","subjectId","userId","startTime","endTime"})
public class ScheduleItemDashboard {
    private Integer id;
    private ScheduleItemType itemType;
    private Subject subject;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public ScheduleItemDashboard(Integer id, Integer itemTypeId, String itemTypeName, Integer subjectId,
                                 String subjectName, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.itemType = new ScheduleItemType(itemTypeId, itemTypeName);
        this.subject = new Subject(subjectId, subjectName);
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
