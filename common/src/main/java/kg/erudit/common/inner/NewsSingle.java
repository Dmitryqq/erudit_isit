package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({"id", "typeId", "images"})
public class NewsSingle extends News {
    private Integer id;
    private Integer typeId;
    private List<Image> images;

    public NewsSingle(String uuid, String url, String title, String text, Date date, String typeName) {
        super(uuid, url, title, text, date, typeName);
    }

    public NewsSingle(Integer id, Integer typeId, String uuid, String url, String title, String text, Date date) {
        super(uuid, url, title, text, date);
        this.id = id;
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "NewsSingle{" +
                "id=" + id +
                ", typeId=" + typeId +
                ", images=" + images +
                ", uuid='" + uuid + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
