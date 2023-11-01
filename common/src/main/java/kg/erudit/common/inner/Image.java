package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "fileName", "ext", "main", "base64"})
public class Image {
    private Integer id;
    private String fileName;
    private String ext;
    private Boolean main;
    private String base64;

    @JsonIgnore
    public String getFullFileName() {
//        if (fileName == null || ext == null)
//            return null;
        return fileName + "." + ext;
    }

    public Image(String fileName, String ext) {
        this.fileName = fileName;
        this.ext = ext;
    }

    public Image(String fileName, String ext, String main) {
        this.fileName = fileName;
        this.ext = ext;
        this.main = main != null && Integer.parseInt(main) == 1;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", ext='" + ext + '\'' +
                ", main=" + main +
                ", base64='" + (base64 != null ? base64.substring(0, 15) + "..." : null) + '\'' +
                '}';
    }
}
