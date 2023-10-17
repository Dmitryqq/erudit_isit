package kg.erudit.common.inner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectClass {
    private Integer subjectId;
    private Integer classId;

    @Override
    public String toString() {
        return "SubjectClass{" +
                "subjectId=" + subjectId +
                ", classId=" + classId +
                '}';
    }
}
