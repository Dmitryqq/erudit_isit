package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserExtended extends User {
    private Integer studentClassId;
    private List<Integer> teacherClassIds;
    private List<Integer> teacherSubjectIds;
    private List<SubjectClass> teacherSubjectClassesIds;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Asia/Bishkek")
    protected Date birthDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy", timezone = "Asia/Bishkek")
    protected Date createDate;

    //Список
    public UserExtended(Integer id, String username, String name, String surname, String patronymic, Integer roleId, String roleCode, String roleName, Boolean locked, Date createDate) {
        super(id, username, name, surname, patronymic, roleId, roleCode, roleName, locked);
        this.createDate = createDate;
    }

    //Подробная инфа
    public UserExtended(Integer id, String username, String name, String surname, String patronymic, Integer roleId, String roleCode, String roleName,
                        Integer studentClassId, String teacherClassIds, String teacherSubjectIds, String teacherSubjectClassesIds, Boolean locked, Date birthDate, Date createDate) {
        super(id, username, name, surname, patronymic, roleId, roleCode, roleName, locked);

        this.studentClassId = studentClassId;

        if (teacherClassIds != null)
            this.teacherClassIds = Arrays.stream(teacherClassIds.split(";"))
                    .mapToInt(Integer::parseInt).boxed().toList();
        if (teacherSubjectIds != null)
            this.teacherSubjectIds = Arrays.stream(teacherSubjectIds.split(";"))
                    .mapToInt(Integer::parseInt).boxed().toList();

        if (teacherSubjectClassesIds != null) {
            String[] linkArray = teacherSubjectClassesIds.split(";");
            this.teacherSubjectClassesIds = new LinkedList<>();
            for (String a : linkArray) {
                String[] b = a.split("_");
                this.teacherSubjectClassesIds.add(new SubjectClass(Integer.parseInt(b[0]), Integer.parseInt(b[1])));
            }
        }
        this.birthDate = birthDate;
        this.createDate = createDate;
    }
}
