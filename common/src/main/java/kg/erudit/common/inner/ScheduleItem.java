package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"typeId","subjectId","userId","startTime","endTime"})
public class ScheduleItem {
    private Integer id;
    private Integer typeId;
    private Integer subjectId;
    private Integer userId;
    @JsonFormat(pattern = "hh:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "hh:mm")
    private LocalTime endTime;

    @JsonIgnore
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

    @JsonSetter("startTime")
    public void setStartTime(String startTime) {
        this.startTime = LocalTime.parse(startTime, formatter);
    }

    @JsonSetter("endTime")
    public void setEndTime(String endTime) {
        this.endTime = LocalTime.parse(endTime, formatter);
    }

    //    public String getStartTime() {
//        return startTime;
//    }
//
//    public String getEndTime() {
//        return endTime;
//    }

    public ScheduleItem(Integer typeId, LocalTime startTime, LocalTime endTime) {
        this.typeId = typeId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ScheduleItem(Integer id, Integer typeId, Integer subjectId, Integer userId, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.typeId = typeId;
        this.subjectId = subjectId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ScheduleItem{" +
                "typeId=" + typeId +
                ", subjectId=" + subjectId +
                ", userId=" + userId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}