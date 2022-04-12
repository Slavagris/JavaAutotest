package JavaBackendTests.Homework4;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RecipeTests extends TestsBase{

    @Test
    void getRecipeWithoutQueryParametersTest() {
        given()
                .spec(requestSpecification)
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .assertThat().body("totalResults", equalTo(5222));
    }

    @Test
    void getRecipeWithWrongRequestTypeTest() {
        given()
                .spec(requestSpecification)
                .when()
                .post("https://api.spoonacular.com/recipes/complexSearch")
                .then()
                .statusCode(405);
    }

    @Test
    void getRecipeWithQueryParameterTest() {
        given()
                .spec(requestSpecification)
                .queryParam("query", "burger")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .assertThat().body("totalResults", equalTo(54));
    }

    @Test
    void getRecipeWithQueryAndDietParametersTest() {
        given()
                .spec(requestSpecification)
                .queryParam("query", "burger")
                .queryParam("diet", "vegetarian")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .assertThat().body("totalResults", equalTo(3))
                .assertThat().body("results[0].title", containsString("Falafel Burger"));
    }

    @Test
    void getRecipeWithExcludeCuisineParameterTest() {
        given()
                .spec(requestSpecification)
                .queryParam("excludeCuisine", "greek")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .then()
                .spec(responseSpecification)
                .assertThat().body("totalResults", equalTo(5198));
    }
}