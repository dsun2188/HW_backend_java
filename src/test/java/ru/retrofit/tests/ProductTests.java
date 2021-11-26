package ru.retrofit.tests;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.retrofit.dto.Product;
import ru.retrofit.enums.CategoryType;
import ru.retrofit.service.ProductService;
import ru.retrofit.utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

//import ru.retrofit.db.dao.ProductsMapper;
//import ru.retrofit.utils.DbUtils;

public class ProductTests {
//    int productId;
//    static ProductsMapper productsMapper;
    static Retrofit client;
    static ProductService productService;
    Faker faker = new Faker();
    Product product;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
//        productsMapper = DbUtils.getProductsMapper();
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
    }

    @Test
    void postProductTest() throws IOException {
//        Integer countProductsBefore = DbUtils.countProducts(productsMapper);
        Response<Product> response = productService.createProduct(product).execute();
//        Integer countProductsAfter = DbUtils.countProducts(productsMapper);
//        assertThat(countProductsAfter, equalTo(countProductsBefore+1));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
//        productId = response.body().getId();
    }



//    @Test
//    void getProductIdTest() throws IOException {
//         getProduct = ArrayList<Product>;
//
//        Response<Product> response = productService
//                .getProduct(id)
//                .execute();
//    }

//    @AfterEach
//    void tearDown() throws IOException {
//        Response<ResponseBody> response = productService.deleteProduct(productId).execute();
//        assertThat(response.isSuccessful(), is(true));
//    }
}
