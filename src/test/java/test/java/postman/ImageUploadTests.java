package test.java.postman;

import io.qameta.allure.Story;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.java.dto.PostImageResponse;

import java.io.File;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static test.java.Endpoints.UPLOAD_IMAGE;



@Story("Image api tests")

public class ImageUploadTests extends BaseTest {



    private final String PATH_TO_IMAGE = "src/test/resources/test-image.jpg";
    static String encodedFile;
    String uploadedImageId;

    MultiPartSpecification base64MultiPartSpec;
    MultiPartSpecification multiPartSpecWithFile;

    static RequestSpecification requestSpecificationWithAuthAndMultipartImage;
    static RequestSpecification requestSpecificationWithAuthWithBase64;

    private String imageId;


    @BeforeEach
    void beforeTest() {

        byte[] byteArray = getFileContent(PATH_TO_IMAGE);
        encodedFile = Base64.getEncoder().encodeToString(byteArray);

        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        multiPartSpecWithFile = new MultiPartSpecBuilder(new File("src/test/resources/test-image.jpg"))
                .controlName("image")
                .build();

        requestSpecificationWithAuthAndMultipartImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "gif")
                .addMultiPart(multiPartSpecWithFile)
                .build();

        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(base64MultiPartSpec)
                .build();
        
        imageId = given(requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    
    @DisplayName("Get Image")

    @Test
    void GetImage() {
        
        given()
                .headers("Authorization", token)
                .expect()
                .statusCode(200)
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", imageId)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.imageId");
    }

    @DisplayName("Загрузка файла в формате base64")

    @Test
    void uploadFileTest() {
        uploadedImageId = given(requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

 //   по ДЗ №3
//    @Test
//    void uploadFileTest() {
//        uploadedImageId = given()
//                .headers("Authorization", token)
//                .multiPart("image", encodedFile)
//                .expect()
//                .body("success", is(true))
//                .body("data.id", is(notNullValue()))
//                .when()
//                .post("https://api.imgur.com/3/image")
//                .prettyPeek()
//                .then()
//                .extract()
//                .response()
//                .jsonPath()
//                .getString("data.deletehash");
//    }




    @DisplayName("Загрузка файла в формате base64 без картинки")

    @Test
    void uploadFileTestOutImg64() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .expect()
                .statusCode(400)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @DisplayName("Загрузка видео в формате картинки base64")

    @Test
    void uploadFileTestVideoFileJpg64() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("video", encodedFile)
                .expect()
                .statusCode(400)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @DisplayName("Загрузка файла в формате jpg")

    @Test
    void uploadFileImageTest() {
        uploadedImageId = given(requestSpecificationWithAuthAndMultipartImage)
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

//    к ДЗ №3
//    @Test
//    void uploadFileImageTest() {
//        uploadedImageId = given()
//                .headers("Authorization", token)
//                .multiPart("image", new File("src/test/resources/test-image.jpg"))
//                .expect()
//                .statusCode(200)
//                .when()
//                .post("https://api.imgur.com/3/upload")
//                .prettyPeek()
//                .then()
//                .extract()
//                .response()
//                .body()
//                .as(PostImageResponse.class)
//                .getData().getDeletehash();
//
//    }

    @Test
    void uploadWithMultiPart() {
        uploadedImageId =    given(requestSpecificationWithAuthAndMultipartImage)
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @DisplayName("Загрузка видео с форматом картинки")

    @Test
    void uploadFileImageTestVideoFileJpg() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/test-image.jpg"))
                .multiPart("video", new File("src/test/resources/test-image.jpg"))
                .expect()
                .statusCode(400)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();

    }

    @DisplayName("Загрузка картинки без картинки")

    @Test
    void uploadFileImageTestOutImage() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .expect()
                .statusCode(400)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .statusCode(400)
                .extract()
                .response()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();

    }

//    Эти тесты пока не работают у меня, не понимаю как сделать, чтоб заработали

//    @DisplayName("Загрузка файла картинки в виде ссылки")
//
//    @Test
//    void uploadFileImageTestImgUrl() {
//        uploadedImageId = given()
//                .headers("Authorization", token)
//                .multiPart("image", new Text ("https://yandex.ru/images/search?utm_source=main_stripe_big&text=%D0%97%D0%B8%D0%BC%D0%BE%D1%80%D0%BE%D0%B4%D0%BE%D0%BA&nl=1&source=morda&pos=3&rpt=simage&img_url=https%3A%2F%2Fpbs.twimg.com%2Fmedia%2FEs65JJdXcAA4QoO.jpg"))
//                .expect()
//                .statusCode(400)
//                .when()
//                .post("https://api.imgur.com/3/upload")
//                .prettyPeek()
//                .then()
//                .extract()
//                .response()
//                .body()
//                .as(PostImageResponse.class)
//                .getData().getDeletehash();
//
//    }
//
//    @DisplayName("Загрузка картинки как текст")
//
//    @Test
//    void uploadFileImageTestImgTextWrong() {
//        uploadedImageId = given()
//                .headers("Authorization", token)
//                .multiPart("image", new Text("src/test/resources/test-image.jpg"))
//                .expect()
//                .statusCode(400)
//                .when()
//                .post("https://api.imgur.com/3/upload")
//                .prettyPeek()
//                .then()
//                .extract()
//                .response()
//                .body()
//                .as(PostImageResponse.class)
//                .getData().getDeletehash();
//
//    }

//    @DisplayName("Favorite an Image")
//
//    @Test
//    void favoriteAnImage() {
//        favoriteImageId = given()
//                .headers("Authorization", token)
//                .when()
//                .post("https://api.imgur.com/3/image/{imageHash}/favorite")
//                .prettyPeek()
//                .then()
//                .statusCode(200)
//                .extract()
//                .response()
//                .jsonPath()
//                .getString("data.favorite");
//
//    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
