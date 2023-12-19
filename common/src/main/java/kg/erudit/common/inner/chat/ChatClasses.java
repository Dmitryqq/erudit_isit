package kg.erudit.common.inner.chat;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import kg.erudit.common.inner.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","name","students"})
public class ChatClasses {
    private Integer id;
    private String name;
    @JsonIgnore
    private Map<Integer, User> studentItemMap;

    public ChatClasses(Integer id, String name) {
        this.id = id;
        this.name = name;
        if (this.studentItemMap == null)
            this.studentItemMap = new LinkedHashMap<>();
    }

    @JsonGetter("students")
    public List<User> getStudents() {
        return studentItemMap.values().stream().toList();
    }

    @Override
    public String toString() {
        return "ChatClasses{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", studentItemMap=" + studentItemMap +
                '}';
    }
}
