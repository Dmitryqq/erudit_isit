package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name","role","rating","text","image"})
public class Review {
    private String name;
    private String role;
    private Float rating;
    private String text;
//    private String image;


    @Override
    public String toString() {
        return "Review{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", rating=" + rating +
                ", text='" + text + '\'' +
                '}';
    }
}
