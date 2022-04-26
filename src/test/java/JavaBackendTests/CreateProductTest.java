package JavaBackendTests;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.geekbrains.lesson5.helpers.DBHelper;
import org.geekbrains.lessson5.api.ProductService;
import org.geekbrains.lessson5.dto.Product;
import org.geekbrains.lessson5.util.RetrofitUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateProductTest {

    static ProductService productService;
    static SqlSession session;
    static db.dao.ProductsMapper productsMapper;
    Product product = null;
    Faker faker = new Faker();
    int id;

    @BeforeAll
    static void beforeAll() throws IOException {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new
                SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        productsMapper = session.getMapper(db.dao.ProductsMapper.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
    }

    @Test
    void createProductInFoodCategoryTest() throws IOException {
        Response<Product> createProductResponse = productService.createProduct(product)
                .execute();
        assertThat(createProductResponse.isSuccessful(), CoreMatchers.is(true));
        assertThat(createProductResponse.body().getId(), notNullValue());

        id =  createProductResponse.body().getId();

        DBHelper.assertProductCardInDB(productsMapper, product, Long.valueOf(id));

        Response<Product> getProductResponse = productService.getProductById(id)
                .execute();
        assertThat(getProductResponse.isSuccessful(), CoreMatchers.is(true));
        assertThat(getProductResponse.body().getId(), equalTo(id));
        assertThat(getProductResponse.body().getTitle(), equalTo(product.getTitle()));
        assertThat(getProductResponse.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(getProductResponse.body().getPrice(), equalTo(product.getPrice()));

        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        DBHelper.assertProductNotExistInDB(productsMapper, Long.valueOf(id));
    }

    @Test
    void createProductWithExtraFieldTest() throws IOException {
        product.setId(2);

        Response<Product> createProductResponse = productService.createProduct(product)
                .execute();

        assertThat(createProductResponse.code(), equalTo(400));
        assertThat(createProductResponse.errorBody().string(), containsString("Id must be null for new entity"));

        DBHelper.assertProductNotExistInDB(productsMapper, Long.valueOf(id));
    }

    @SneakyThrows
    @AfterAll
    static void tearDown() {
        session.close();;
    }
}