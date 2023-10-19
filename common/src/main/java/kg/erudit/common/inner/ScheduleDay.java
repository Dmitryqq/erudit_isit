package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","date","items"})
public class ScheduleDay extends ScheduleDayBase {
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Asia/Bishkek")
    private Date date;

    public ScheduleDay(Date date) {
        this.date = date;
    }

    public ScheduleDay(Integer id, Date date) {
        this.id = id;
        this.date = date;
        this.items = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ScheduleDay{" +
                "id=" + id +
                ", date=" + date +
                ", items=" + items +
                '}';
    }
}
