package ru.retrofit.tests;

import com.github.javafaker.Faker;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.retrofit.dto.Product;
import ru.retrofit.enums.CategoryType;
import ru.retrofit.service.ProductService;
import ru.retrofit.utils.PrettyLogger;
import ru.retrofit.utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ProductUpdateTests {
    static Retrofit client;
    static ProductService productService;
    Faker faker = new Faker();
    Product product;
    int id;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
    }

    @BeforeEach
    void setUp() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();
        id = response.body().getId();
    }

    @DisplayName("Модификация продукта валидными данными")
    @Test
    void putProductTest() throws IOException {
        product.setId(id);
        product.setTitle(faker.food().dish());
        product.setPrice((int) ((Math.random() + 1) * 100));
        product.setCategoryTitle(CategoryType.FURNITURE.getTitle());

        Response<Product> response = productService.updateProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(200));
        assertThat(response.isSuccessful(), equalTo(true));
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @DisplayName("Модификация продукта с невалидным id")
    @Test
    void putProductWrongIdTest() throws IOException {
        product.setId(0);
        product.setTitle(faker.food().dish());

        Response<Product> response = productService.updateProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(404));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Удаление названия продукта")
    @Test
    void putProductNullTitleTest() throws IOException {
        product.setId(id);
        product.setTitle(null);

        Response<Product> response = productService.updateProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Удаление цены продукта")
    @Test
    void putProductNullPriceTest() throws IOException {
        product.setId(id);
        product.setPrice(null);

        Response<Product> response = productService.updateProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Удаление категории продукта")
    @Test
    void putProductNullCategoryTest() throws IOException {
        product.setId(id);
        product.setCategoryTitle(null);

        Response<Product> response = productService.updateProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Обнуление цены продукта")
    @Test
    void putProductZeroPriceTest() throws IOException {
        product.setId(id);
        product.setPrice(0);

        Response<Product> response = productService.updateProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Ввод отрицательной цены продукта")
    @Test
    void putProductNegativePriceTest() throws IOException {
        product.setId(id);
        product.setPrice((int) ((Math.random() - 1) * 100));

        Response<Product> response = productService.updateProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @AfterEach
    void tearDown() throws IOException {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();

        assertThat(response.code(), equalTo(200));
        assertThat(response.isSuccessful(), equalTo(true));
    }
}