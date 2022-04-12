package JavaBackend.Homework4;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class AddMealRequest {
    @JsonProperty("date")
    private Integer date;
    @JsonProperty("slot")
    private Integer slot;
    @JsonProperty("position")
    private Integer position;
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Value value;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder( {
            "ingredients"
    })
    @Data
    private static class Value {
        @JsonProperty("ingredients")
        private List<Ingredient> ingredients = new ArrayList<Ingredient>();
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder( {"name"})
    @Data
    private static class Ingredient {
        @JsonProperty("name")
        private String name;
    }
}