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
@JsonPropertyOrder({"id","name","role","rating","text","image"})
public class Review {
    private Integer id;
    private String name;
    private String role;
    private Float rating;
    private String text;
    private Image image;

    public Review(Integer id, String name, String role, Float rating, String text, String image) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.rating = rating;
        this.text = text;
        if (image != null) {
            String[] imagePart = image.split("\\.");
            this.image = new Image(imagePart[0], imagePart[1]);
        }
    }

    public Review(String name, String role, Float rating, String text, String image) {
        this.name = name;
        this.role = role;
        this.rating = rating;
        this.text = text;
        if (image != null) {
            String[] imagePart = image.split("\\.");
            this.image = new Image(imagePart[0], imagePart[1]);
        }
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", rating=" + rating +
                ", text='" + text + '\'' +
                ", image=" + image +
                '}';
    }
}
