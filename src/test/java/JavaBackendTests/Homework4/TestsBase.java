package JavaBackendTests.Homework4;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;

import static org.geekbrains.lesson4.Utils.getConfigValue;

public class TestsBase {
    ResponseSpecification responseSpecification = null;
    static RequestSpecification requestSpecification = null;
    protected String id;
    protected String user = "lesnikov" + new Random().nextInt(9999);
    protected String email = "mail" + new Random().nextInt(9999) + "@example.ru";
    protected String hash;

    @BeforeEach
    public void beforeTest() {
        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", getConfigValue("apiKey"))
                .log(LogDetail.ALL)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .expectHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, OPTIONS, DELETE, PUT")
                .log(LogDetail.ALL)
                .build();
    }

}