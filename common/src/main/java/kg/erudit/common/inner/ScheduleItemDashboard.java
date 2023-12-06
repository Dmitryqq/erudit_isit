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
    private String ids;
    private String classes;
    private ScheduleItemType itemType;
    private Subject subject;
    private Boolean individual;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public ScheduleItemDashboard(String ids, Integer itemTypeId, String itemTypeName, Integer subjectId,
                                 String subjectName, Boolean individual, LocalTime startTime, LocalTime endTime) {
        this.ids = ids;
        this.itemType = new ScheduleItemType(itemTypeId, itemTypeName);
        this.subject = new Subject(subjectId, subjectName);
        this.individual = individual;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ScheduleItemDashboard(String ids, String classes, Integer itemTypeId, String itemTypeName, Integer subjectId,
                                 String subjectName, Boolean individual, LocalTime startTime, LocalTime endTime) {
        this.ids = ids;
        this.classes = classes;
        this.itemType = new ScheduleItemType(itemTypeId, itemTypeName);
        this.subject = new Subject(subjectId, subjectName);
        this.individual = individual;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ScheduleItemDashboard{" +
                "ids='" + ids + '\'' +
                ", classes='" + classes + '\'' +
                ", itemType=" + itemType +
                ", subject=" + subject +
                ", individual=" + individual +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
