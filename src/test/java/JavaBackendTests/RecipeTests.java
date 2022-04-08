package JavaBackendTests;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class RecipeTests {
    @Test
    void getRecipePositiveTest() {
        given()
                .queryParam("apiKey", "77f47619dd984e85b6d018d0189ef9fc")
                .queryParam("includeNutrition", "false")
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information")
                .then()
                .statusCode(200);
    }



}