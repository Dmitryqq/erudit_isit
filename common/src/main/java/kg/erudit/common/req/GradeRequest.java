package kg.erudit.common.req;

import kg.erudit.common.inner.Grade;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GradeRequest extends Grade {
    private Integer scheduleItemId;
    private Integer studentId;

    @Override
    public String toString() {
        return "GradeRequest{" +
                "scheduleItemId=" + scheduleItemId +
                ", studentId=" + studentId +
                ", gradeTypeId=" + gradeTypeId +
                ", value=" + value +
                '}';
    }
}
