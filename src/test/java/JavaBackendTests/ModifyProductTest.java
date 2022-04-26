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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ModifyProductTest {
    static ProductService productService;
    static SqlSession session;
    static db.dao.ProductsMapper productsMapper;
    Product product = null;
    Faker faker = new Faker();
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

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
    }

    @Test
    void updateProductInFoodCategoryTest() throws IOException {
        Response<Product> createProductResponse = productService.createProduct(product)
                .execute();
        assertThat(createProductResponse.isSuccessful(), CoreMatchers.is(true));
        id = createProductResponse.body().getId();

        Product newProduct = new Product()
                .withId(id)
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));

        Response<Product> updateProductResponse = productService.modifyProduct(newProduct)
                .execute();
        assertThat(updateProductResponse.isSuccessful(), CoreMatchers.is(true));
        assertThat(updateProductResponse.body().getId(), equalTo(id));
        assertThat(updateProductResponse.body().getTitle(), equalTo(newProduct.getTitle()));
        assertThat(updateProductResponse.body().getCategoryTitle(), equalTo(newProduct.getCategoryTitle()));
        assertThat(updateProductResponse.body().getPrice(), equalTo(newProduct.getPrice()));

        DBHelper.assertProductCardInDB(productsMapper, newProduct, (long) id);

        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        DBHelper.assertProductNotExistInDB(productsMapper, (long) id);
    }

    @Test
    void updateUnknownIdProductTest() throws IOException {
        int unknownId = 252;

        DBHelper.assertProductNotExistInDB(productsMapper, (long) unknownId);

        product.setId(id);

        Response<Product> updateProductResponse = productService.modifyProduct(product)
                .execute();
        assertThat(updateProductResponse.code(), equalTo(400));
    }

    @SneakyThrows
    @AfterAll
    static void tearDown() {
        session.close();
    }
}