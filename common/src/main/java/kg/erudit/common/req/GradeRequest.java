package kg.erudit.common.req;

import lombok.Data;

@Data
public class GradeRequest {
    private Integer scheduleItemId;
    private Integer studentId;
    private Integer gradeTypeId;
    private Integer value;
}
