package JavaBackendTests.Homework4;

import lombok.SneakyThrows;
import org.geekbrains.lessson4.dto.Request.AddShoppingListRequest;
import org.geekbrains.lessson4.dto.Request.CreateUserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AddShoppingListTests extends TestsBase {
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
    void addShoppingListTest() {
        AddShoppingListRequest addShoppingListRequest = new AddShoppingListRequest()
                .withItem("1 package baking powder")
                .withAisle("Baking")
                .withParse(true);

        id= given()
                .queryParam("hash", hash)
                .spec(requestSpecification)
                .body(addShoppingListRequest)
                .when()
                .post("https://api.spoonacular.com/mealplanner/" + user + "/shopping-list/items")
                .then()
                .spec(responseSpecification)
                .assertThat().body("name", equalTo("baking powder"))
                .assertThat().body("aisle", equalTo("Baking"))
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @Test
    void addShoppingListWithoutItemTest() {
        AddShoppingListRequest addShoppingListRequest = new AddShoppingListRequest()
                .withAisle("Baking")
                .withParse(true);

        id= given()
                .queryParam("hash", hash)
                .spec(requestSpecification)
                .body(addShoppingListRequest)
                .when()
                .post("https://api.spoonacular.com/mealplanner/" + user + "/shopping-list/items")
                .then()
                .spec(responseSpecification)
                .assertThat().body("name", equalTo(""))
                .assertThat().body("aisle", equalTo("Baking"))
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @Test
    void addShoppingListWithoutAisleTest() {
        AddShoppingListRequest addShoppingListRequest = new AddShoppingListRequest()
                .withItem("1 package baking powder")
                .withParse(true);

        id= given()
                .queryParam("hash", hash)
                .spec(requestSpecification)
                .body(addShoppingListRequest)
                .when()
                .post("https://api.spoonacular.com/mealplanner/" + user + "/shopping-list/items")
                .then()
                .spec(responseSpecification)
                .assertThat().body("name", equalTo("baking powder"))
                .assertThat().body("aisle", equalTo("Baking"))
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        given()
                .spec(requestSpecification)
                .queryParam("hash", hash)
                .delete("https://api.spoonacular.com/mealplanner/" + user + "/shopping-list/items/" + id)
                .then()
                .spec(responseSpecification)
                .assertThat().body("status", equalTo("success"));
    }
}