package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"subjectName", "visited", "homework", "grades", "startTime", "endTime"})
public class DiaryItem {
    private String subjectName;
    private Boolean visited;
    private Boolean individual;
    private List<String> homework;
    private List<Grade> grades;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public DiaryItem(String subjectName, String homework, String grades, Boolean visited, Boolean individual, LocalTime startTime, LocalTime endTime) {
        this.subjectName = subjectName;
        this.visited = visited;
        this.individual = individual;
//        _____
        if (homework != null) {
            String[] homeworkArray = homework.split("_____");
            this.homework = Arrays.asList(homeworkArray);
        }

        if (grades != null) {
            String[] gradesArray = grades.split(";");
            this.grades = new LinkedList<>();
            for (String a : gradesArray) {
                String[] b = a.split("_");
                this.grades.add(new Grade(b[0], Integer.parseInt(b[1])));
            }
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "DiaryItem{" +
                "subjectName='" + subjectName + '\'' +
                ", visited=" + visited +
                ", individual=" + individual +
                ", homework=" + homework +
                ", grades=" + grades +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
