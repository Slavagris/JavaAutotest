package JavaBackendTests;

import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.geekbrains.lesson5.helpers.DBHelper;
import org.geekbrains.lessson5.api.ProductService;
import org.geekbrains.lessson5.dto.Product;
import org.geekbrains.lessson5.util.RetrofitUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetProductTest {

    static ProductService productService;
    static SqlSession session;
    static db.dao.ProductsMapper productsMapper;
    int id;

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new
                SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        productsMapper = session.getMapper(db.dao.ProductsMapper.class);
    }

    @Test
    void getProductByIdTest() throws IOException {
        Random random = new Random();
        id = random.ints(1, 5).findFirst().getAsInt();

        Response<Product> getProductResponse = productService.getProductById(id)
                .execute();
        assertThat(getProductResponse.isSuccessful(), CoreMatchers.is(true));
        assertThat(getProductResponse.body().getId(), equalTo(id));
        assertThat(getProductResponse.body().getTitle(), notNullValue());
        assertThat(getProductResponse.body().getCategoryTitle(), notNullValue());
        assertThat(getProductResponse.body().getPrice(), notNullValue());

        DBHelper.assertProductExistInDB(productsMapper, Long.valueOf(id));
    }

    @Test
    void getUnknownProductByIdTest() throws IOException {
        Random random = new Random();
        id = random.ints(10000, 20000000).findFirst().getAsInt();

        Response<Product> getProductResponse = productService.getProductById(id)
                .execute();
        assertThat(getProductResponse.code(), equalTo(404));
        DBHelper.assertProductNotExistInDB(productsMapper, Long.valueOf(id));
    }

    @Test
    void getAllProductsTest() throws IOException {
        Response<ArrayList<Product>> getProductResponse = productService.getProducts()
                .execute();
        assertThat(getProductResponse.isSuccessful(), CoreMatchers.is(true));
        assertThat(getProductResponse.body().size(), greaterThan(1));

        Product expectedProduct = new Product().withId(1)
                .withPrice(95)
                .withTitle("Milk")
                .withCategoryTitle("Food");

        DBHelper.assertProductCardInDB(productsMapper, expectedProduct, (long) expectedProduct.getId());

        Product firstProduct = getProductResponse.body().get(0);
        assertThat(firstProduct.getId(), equalTo(1));
        assertThat(firstProduct.getTitle(), equalTo("Milk"));
        assertThat(firstProduct.getCategoryTitle(), equalTo("Food"));
        assertThat(firstProduct.getPrice(), equalTo(95));
    }

    @SneakyThrows
    @AfterAll
    static void tearDown() {
        session.close();;
    }
}