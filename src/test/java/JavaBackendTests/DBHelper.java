package JavaBackendTests;

import org.geekbrains.lessson5.dto.Product;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DBHelper {

    public static void assertProductNotExistInDB(db.dao.ProductsMapper productsMapper, Long id) {
        db.model.ProductsExample example = new db.model.ProductsExample();
        example.createCriteria().andIdEqualTo(id);
        assertThat(productsMapper.countByExample(example), equalTo(0L));
    }

    public static void assertProductExistInDB(db.dao.ProductsMapper productsMapper, Long id) {
        db.model.ProductsExample example = new db.model.ProductsExample();
        example.createCriteria().andIdEqualTo(id);
        assertThat(productsMapper.countByExample(example), not(equalTo(0L)));
    }

    public static void assertProductCardInDB(db.dao.ProductsMapper productsMapper, Product expectedProduct, Long id) {
        db.model.Products newProduct = productsMapper.selectByPrimaryKey(id);
        assertThat(newProduct.getId(), equalTo(id));
        assertThat(newProduct.getTitle(), equalTo(expectedProduct.getTitle()));
        assertThat(newProduct.getPrice(), equalTo(expectedProduct.getPrice()));
    }

    public static void assertCategoryExistInDB(db.dao.CategoriesMapper categoriesMapper, Long id) {
        db.model.CategoriesExample example = new db.model.CategoriesExample();
        example.createCriteria().andIdEqualTo(id);
        assertThat(categoriesMapper.countByExample(example), not(equalTo(0L)));
    }
}