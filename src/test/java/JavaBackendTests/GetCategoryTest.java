package JavaBackendTests;

import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.geekbrains.lesson5.helpers.DBHelper;
import org.geekbrains.lessson5.api.CategoryService;
import org.geekbrains.lessson5.dto.GetCategoryResponse;
import org.geekbrains.lessson5.util.RetrofitUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetCategoryTest {
    static CategoryService categoryService;
    static SqlSession session;
    static db.dao.CategoriesMapper categoriesMapper;

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        categoryService = RetrofitUtils.getRetrofit()
                .create(CategoryService.class);
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new
                SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        categoriesMapper = session.getMapper(db.dao.CategoriesMapper.class);
    }

    @SneakyThrows
    @Test
    void getCategoryByIdPositiveTest() {
        int id = 1;
        Response<GetCategoryResponse> response = categoryService.getCategory(id).execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.body().getTitle(), equalTo("Food"));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo("Food")));

        DBHelper.assertCategoryExistInDB(categoriesMapper, (long) id);
    }

    @SneakyThrows
    @AfterAll
    static void tearDown() {
        session.close();
    }
}