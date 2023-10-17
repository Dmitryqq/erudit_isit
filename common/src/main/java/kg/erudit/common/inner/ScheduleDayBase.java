package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"dayNumber","items"})
public class ScheduleDayBase {
    private Integer dayNumber;
    protected List<ScheduleItem> items;

    @Override
    public String toString() {
        return "ScheduleDayBase{" +
                "dayNumber=" + dayNumber +
                ", items=" + items +
                '}';
    }
}

