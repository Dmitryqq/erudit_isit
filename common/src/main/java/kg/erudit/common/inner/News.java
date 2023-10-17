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
@JsonPropertyOrder({"id", "uuid", "url", "title", "text", "date", "typeName", "typeId", "images"})
public class News {
    private Integer id;
    protected String uuid;
    protected String url;
    protected String title;
    protected String text;
    private String typeName;
    private Image mainImage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy", timezone = "Asia/Bishkek")
    protected Date date;

    public News(String uuid, String url, String title, String text, Date date, String typeName) {
        this.uuid = uuid;
        this.url = url;
        this.title = title;
        this.text = text;
        this.date = date;
        this.typeName = typeName;
    }

    public News(String uuid, String url, String title, String text, Date date) {
        this.uuid = uuid;
        this.url = url;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public News(String uuid, String url, String title, String text, Date date, String typeName, String image) {
        this.uuid = uuid;
        this.url = url;
        this.title = title;
        this.text = text;
        this.date = date;
        this.typeName = typeName;
        if (image != null) {
            String[] imagePart = image.split("\\.");
            this.mainImage = new Image(imagePart[0], imagePart[1]);
        }
//        if (this.images == null)
//            this.images = new ArrayList<>(1);
//        if (image != null)
//            this.images.add(image);
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", typeName='" + typeName + '\'' +
                ", mainImage=" + mainImage +
                ", date=" + date +
                '}';
    }
}
