package ru.retrofit.tests;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("Создание продукта с валидными данными")
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



    @DisplayName("Создание продукта с непустым id")
    @Test
    void postProductWithIdTest() throws IOException {
        product = new Product()
                .withId(1);
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Создание продукта с пустым заголовком")
    @Test
    void postProductWithNullTitleTest() throws IOException {
        product = new Product()
                .withTitle(null)
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Создание продукта с пустой ценой")
    @Test
    void postProductWithNullPriceTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(null)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Создание продукта с пустой категорией")
    @Test
    void postProductWithNullCategoryTitleTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(null);
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Создание продукта с нулевой ценой")
    @Test
    void postProductWithZeroPriceTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(0)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Создание продукта с отрицательной ценой")
    @Test
    void postProductWithNegativePriceTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(-1)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }



//    @AfterEach
//    void tearDown() throws IOException {
//        Response<ResponseBody> response = productService.deleteProduct(productId).execute();
//        assertThat(response.isSuccessful(), is(true));
//    }
}
