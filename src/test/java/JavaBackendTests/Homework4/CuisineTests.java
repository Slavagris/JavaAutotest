package JavaBackendTests.Homework4;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CuisineTests extends TestsBase{

    @Test
    void classifyCuisineWithoutQueryParametersTest() {
        given()
                .spec(requestSpecification)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .assertThat().body("confidence", equalTo(0.0F));
    }

    @Test
    void classifyCuisineWithWrongRequestTypeTest() {
        given()
                .spec(requestSpecification)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .when()
                .get("https://api.spoonacular.com/recipes/cuisine")
                .then()
                .statusCode(405);
    }

    @Test
    void classifyCuisineWithAmericanTypeTest() {
        given()
                .spec(requestSpecification)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Burger")
                .param("ingredientList", "3 oz pork shoulder")
                .param("language", "en")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .assertThat().body("cuisine", equalTo("American"))
                .assertThat().body("confidence", equalTo(0.85F));
    }

    @Test
    void classifyCuisineWithVietnameseTypeTest() {
        given()
                .spec(requestSpecification)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Pho")
                .param("ingredientList", "Tofu")
                .param("language", "en")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .assertThat().body("cuisine", equalTo("Vietnamese"))
                .assertThat().body("confidence", equalTo(0.85F));
    }

    @Test
    void classifyCuisineWithEmptyIngredientListTest() {
        given()
                .spec(requestSpecification)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Falafel Burger")
                .param("ingredientList", "")
                .param("language", "en")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .assertThat().body("cuisine", equalTo("Middle Eastern"))
                .assertThat().body("confidence", equalTo(0.85F));
    }
}