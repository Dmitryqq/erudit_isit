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
@JsonPropertyOrder({"fileName", "ext", "base64"})
public class File {
    protected String fileName;
    protected String ext;
    protected String base64;

    public File(String fullFileName) {
        String[] filePart = fullFileName.split("\\.");
        this.fileName = filePart[0];
        this.ext = filePart[1];
    }

    @JsonIgnore
    public String getFullFileName() {
        return fileName + "." + ext;
    }

    @Override
    public String toString() {
        return "File{" +
                "fileName='" + fileName + '\'' +
                ", ext='" + ext + '\'' +
                ", base64='" + (base64 != null ? base64.substring(0, 15) + "..." : null) + '\'' +
                '}';
    }
}
