package JavaBackendTests;

import com.geekbrains.BaseTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class RecipeTestGet extends BaseTest {


    private static final String API_KEY = "77f47619dd984e85b6d018d0189ef9fc";
    private static final String BASE_URL = "https://api.spoonacular.com";

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = BASE_URL;
    }


    @Test
    void getRecipePositiveTest() {
        given()
                .queryParam("apiKey", API_KEY)
                .queryParam("includeNutrition", "false")
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information")
        then()
                .statusCode(200);
    }

    @Test
    void getRecipeWithBodyChecksInGivenPositiveTest() {
        given()
                .queryParam("apiKey", API_KEY)
                .queryParam("includeNutrition", "false")
                .expect()
                .body("vegetarian", is(false))
                .body("vegan", is(false))
                .body("license", equalTo("CC BY-SA 3.0"))
                .body("pricePerServing", equalTo(163.15F))
                .body("extendedIngredients[0].aisle", equalTo("Milk, Eggs, Other Dairy"))
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information")
        then()
                .statusCode(200);
    }

    @Test
    void getRecipeWithLoggingPositiveTest() {
        given()
                .queryParam("apiKey", API_KEY)
                .queryParam("includeNutrition", "false")
                .expect()
                .log()
                .all()
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information")
                .prettyPeek()
        then()
                .statusCode(200);
    }


}