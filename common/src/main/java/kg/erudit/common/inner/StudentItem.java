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
@JsonPropertyOrder({"id","name","surname","patronymic","scheduleDays"})
public class StudentItem {
    private Integer id;
    private String name;
    private String surname;
    private String patronymic;
    @JsonIgnore
    private Map<Integer, ScheduleDay> scheduleDaysMap;

//    public StudentItem(Integer id, String name, String surname, String patronymic, Integer dayId, Date date, Integer scheduleItemId, String grades) {
//        this.id = id;
//        this.name = name;
//        this.surname = surname;
//        this.patronymic = patronymic;
//        if (this.scheduleDaysMap == null) {
//            this.scheduleDaysMap = new LinkedHashMap<>();
//            this.scheduleDaysMap.put(dayId, new ScheduleDay(dayId,date));
//        }
//        ScheduleDay scheduleDay = this.scheduleDaysMap.get(dayId);
//        List<Grade> gradesList = new LinkedList<>();
//        if (grades != null) {
//            String[] gradesArray = grades.split(";");
//            for (String a : gradesArray) {
//                String[] b = a.split("_");
//                gradesList.add(new Grade(Integer.parseInt(b[0]), Integer.parseInt(b[1])));
//            }
//        }
//        ScheduleItem scheduleItem = new ScheduleItem(scheduleItemId, gradesList);
//        scheduleDay.getItems().add(scheduleItem);
//    }

    public StudentItem(Integer id, String name, String surname, String patronymic) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        if (this.scheduleDaysMap == null)
            this.scheduleDaysMap = new LinkedHashMap<>();
    }

    public void addScheduleDay(Integer dayId, Date date, Integer scheduleItemId, String grades, Boolean visited, LocalTime startTime, LocalTime endTime) {
        ScheduleDay scheduleDay = this.scheduleDaysMap.get(dayId);
        if (scheduleDay == null) {
            scheduleDay = new ScheduleDay(dayId, date);
            this.scheduleDaysMap.put(dayId, scheduleDay);
        }
        List<Grade> gradesList = new LinkedList<>();
        if (grades != null) {
            String[] gradesArray = grades.split(";");
            for (String a : gradesArray) {
                String[] b = a.split("_");
                gradesList.add(new Grade(Integer.parseInt(b[0]), Integer.parseInt(b[1])));
            }
        }
        ScheduleItem scheduleItem = new ScheduleItem(scheduleItemId, gradesList, visited, startTime, endTime);
        scheduleDay.getItems().add(scheduleItem);
    }

    @JsonGetter("scheduleDays")
    public List<ScheduleDay> getScheduleDays() {
        return scheduleDaysMap.values().stream().toList();
    }
}
