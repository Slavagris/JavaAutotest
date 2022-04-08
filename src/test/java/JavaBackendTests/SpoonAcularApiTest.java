package JavaBackendTests;

import com.geekbrains.BaseTest;
import io.restassured.RestAssured;
import net.javacrumbs.jsonunit.JsonAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class SpoonAcularApiTest extends BaseTest {

    private static final String API_KEY = "77f47619dd984e85b6d018d0189ef9fc";
    private static final String BASE_URL = "https://api.spoonacular.com";

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    void testGetComplexSearch() throws IOException {

        String actually = RestAssured.given()
                .queryParam("apiKey", API_KEY)
                .queryParam("query", "pasta")
                .queryParam("cuisine", "italian")
                .log()
                .uri()
                .expect()
                .log()
                .body()
                .statusCode(200)
                .time(lessThan(3000L))
                //       .body("results", contains(hasEntry("id", "654959")))
                .body("results[0].id", Matchers.notNullValue())
                .body("offset", is(0))
                .body("number", is(10))
                .body("totalResults", is(127))
                .when()
                .get("recipes/complexSearch")
                .body()
                .asPrettyString();

        String expected = getResourceAsString("expected.json");

        JsonAssert.assertJsonEquals(
                expected,
                actually,
                JsonAssert.when(IGNORING_ARRAY_ORDER)
        );

    }

}