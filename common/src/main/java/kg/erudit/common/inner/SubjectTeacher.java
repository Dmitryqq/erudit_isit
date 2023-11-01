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
@JsonPropertyOrder({"subject", "teacher"})
public class SubjectTeacher {
    private Subject subject;
    private User teacher;

    public SubjectTeacher(Integer subjectId, String subjectName, Integer teacherId, String teacherName, String teacherSurname, String teacherPatronymic) {
        this.subject = new Subject(subjectId, subjectName);
        if (teacherId != null)
            this.teacher = new User(teacherId, teacherName, teacherSurname, teacherPatronymic);
    }

    @Override
    public String toString() {
        return "SubjectTeacher{" +
                "subject=" + subject +
                ", teacher=" + teacher +
                '}';
    }
}
