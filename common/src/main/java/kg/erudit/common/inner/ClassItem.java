package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.*;

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

//    public void addScheduleDay(Date date, Integer scheduleItemId, String grades, Boolean visited, LocalTime startTime, LocalTime endTime) {
//        ScheduleDay scheduleDay = this.scheduleDaysMap.get(date);
//        if (scheduleDay == null) {
//            scheduleDay = new ScheduleDay(date);
//            this.scheduleDaysMap.put(date, scheduleDay);
//        }
//        List<Grade> gradesList = new LinkedList<>();
//        if (grades != null) {
//            String[] gradesArray = grades.split(";");
//            for (String a : gradesArray) {
//                String[] b = a.split("_");
//                gradesList.add(new Grade(Integer.parseInt(b[0]), Integer.parseInt(b[1])));
//            }
//        }
//        ScheduleItem scheduleItem = new ScheduleItem(scheduleItemId, gradesList, visited, startTime, endTime);
//        scheduleDay.getItems().add(scheduleItem);
//    }

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
