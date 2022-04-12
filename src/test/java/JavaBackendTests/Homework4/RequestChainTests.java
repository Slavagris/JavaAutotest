package JavaBackendTests.Homework4;

import org.geekbrains.lessson4.dto.Request.CreateUserRequest;
import org.geekbrains.lessson4.dto.Response.AddMealResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RequestChainTests extends TestsBase {

    @BeforeEach
    void createUserTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest()
                .withUsername(user)
                .withEmail(email)
                .withFirstName("first name")
                .withLastName("last name");

        hash = given()
                .spec(requestSpecification)
                .body(createUserRequest)
                .when()
                .post("https://api.spoonacular.com/users/connect")
                .then()
                .spec(responseSpecification)
                .extract()
                .jsonPath()
                .get("hash")
                .toString();
    }


    @Test
    void addMealWithIngredientTypeTest() {

        id = given()
                .queryParam("hash", hash)
                .spec(requestSpecification)
                .body("{\n"
                        + " \"date\": 1644881179,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 banana\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/" + user + "/items")
                .then()
                .spec(responseSpecification)
                .assertThat().body("status", equalTo("success"))
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @Test
    void addMealWithRecipeTypeTest() {
        AddMealResponse response = given()
                .spec(requestSpecification)
                .queryParam("hash", hash)
                .body("{\n"
                        + " \"date\": 1589500800,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"RECIPE\",\n"
                        + " \"value\": {\n"
                        + " \"id\": 296213,\n"
                        + " \"servings\": 2,\n"
                        + " \"title\": \"Spinach Salad with Roasted Vegetables and Spiced Chickpea\",\n"
                        + " \"imageType\": \"jpg\"\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/" + user + "/items")
                .then()
                .spec(responseSpecification)
                .extract()
                .as(AddMealResponse.class);

        assertThat(response.getStatus(), equalTo("success"));
        id = response.getId();
    }

    @AfterEach
    void tearDown() {
        given()
                .spec(requestSpecification)
                .queryParam("hash", hash)
                .delete("https://api.spoonacular.com/mealplanner/" + user + "/items/" + id)
                .then()
                .spec(responseSpecification);
    }
}