package kg.erudit.common.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import kg.erudit.common.inner.News;
import kg.erudit.common.inner.Review;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"reviews","news"})
public class HomePageResponse extends DefaultServiceResponse{
    private List<Review> reviews;
    private List<News> news;

    @Override
    public String toString() {
        return "HomePageResponse{" +
                "reviews=" + reviews +
                ", news=" + news +
                ", error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}
