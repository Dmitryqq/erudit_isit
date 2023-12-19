package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "classId", "name", "file", "createDate"})
public class Material {
    private Integer id;
    private Integer classId;
    private String name;
    private File file;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy", timezone = "Asia/Bishkek")
    private Date createDate;

    public Material(Integer id, String name, Date createDate) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
    }

    public Material(Integer id, Integer classId, String name, Date createDate) {
        this.id = id;
        this.classId = classId;
        this.name = name;
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", classId=" + classId +
                ", name='" + name + '\'' +
                ", file=" + file +
                ", createDate=" + createDate +
                '}';
    }
}
