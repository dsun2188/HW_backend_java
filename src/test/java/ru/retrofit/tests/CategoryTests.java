package ru.retrofit.tests;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.retrofit.db.model.Categories;
import ru.retrofit.dto.Category;
import ru.retrofit.dto.Product;
import ru.retrofit.enums.CategoryType;
import ru.retrofit.service.CategoryService;
import ru.retrofit.utils.DbUtils;
import ru.retrofit.utils.RetrofitUtils;
//import utils.PrettyLogger;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CategoryTests {

    static Retrofit client;
    static CategoryService categoryService;
    Faker faker = new Faker();
    Product product;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        categoryService = client.create(CategoryService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
    }

//    @Test
//    void getCategoryByIdTest() throws IOException {
//        Integer id = CategoryType.FOOD.getId();
//
//        Response<Category> response = categoryService
//                .getCategory(id)
//                .execute();
//
////        PrettyLogger.DEFAULT.log(response.toString());
//
////        log.info(response.body().toString());
//        assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
//        assertThat(response.body().getId(), equalTo(id));
//    }
//
@DisplayName("Получение категории по валидному id")
@Test
void getCategoryByIdTest() throws IOException {
    Integer id = CategoryType.FOOD.getId();

    Categories category = DbUtils.getCategoriesMapper().selectByPrimaryKey(id);
    Response<Category> response = categoryService.getCategory(id).execute();

  //  PrettyLogger.DEFAULT.log(response.toString());

    assertThat(category.getTitle(), equalTo(CategoryType.FOOD.getTitle()));

    assertThat(response.code(), equalTo(200));
    assertThat(response.isSuccessful(), equalTo(true));
    assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
    assertThat(response.body().getId(), equalTo(id));
    response.body().getProducts().forEach(product ->
            assertThat(product.getCategoryTitle(), equalTo(CategoryType.FOOD.getTitle())));
}

    @DisplayName("Получение категории по невалидному id")
    @Test
    void getCategoryByFailIdTest() throws IOException {
        Response<Category> response = categoryService.getCategory(4).execute();

     //   PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(404));
        assertThat(response.isSuccessful(), equalTo(false));
    }
}
