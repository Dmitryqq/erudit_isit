package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"uuid", "url", "title", "text", "date", "typeName", "images"})
public class News {
    private String uuid;
    private String url;
    private String title;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy", timezone = "Asia/Bishkek")
    private Date date;
    private String typeName;
    private List<String> images;

    @Override
    public String toString() {
        return "News{" +
                "uuid='" + uuid + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", typeName='" + typeName + '\'' +
                ", images=" + images +
                '}';
    }

    public News(String url, String title, String text, Date date, String typeName) {
        this.url = url;
        this.title = title;
        this.text = text;
        this.date = date;
        this.typeName = typeName;
    }

    public News(String url, String title, String text, Date date, String typeName, String image) {
        this.url = url;
        this.title = title;
        this.text = text;
        this.date = date;
        this.typeName = typeName;
        if (this.images == null)
            this.images = new ArrayList<>(1);
        if (image != null)
            this.images.add(image);
    }
}
