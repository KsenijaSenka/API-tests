package web.app.api;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateStudentWithJacksonTest {
    @Test
    public void createStudentWithJackson() {
        Student student = new Student("Aamir", "Khan", "aamir_khan@anywhere.school");

        given()
                .header("Content-Type", "application/json")
                .body(student)
                .when()
                .post("http://localhost:8888/students")
                .then()
                .statusCode(200);
    }
    @BeforeClass
    public void configureRestAssured() {
        RestAssured.config = RestAssured.config()
                .objectMapperConfig(
                        io.restassured.config.ObjectMapperConfig.objectMapperConfig()
                                .defaultObjectMapperType(ObjectMapperType.GSON)
                );
    }
    @Test
    public void updateStudentWithGson() {
        // First fetch existing student
        Student existingStudent =
                given()
                        .when()
                        .get("http://localhost:8888/students/1")
                        //.get("/students/1")
                        .then()
                        .statusCode(200)
                        .extract().as(Student.class);

// Modify only what you want
        existingStudent.setFirstName("Shanti");

// Then send full object via PUT
        given()
                .header("Content-Type", "application/json")
                .body(existingStudent)
                .when()
                .put("http://localhost:8888/students/1")
                //.put("/students/1")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Shanti"));

//    Student student = new Student();
//student.setFirstName("Shanti");
//
//    given()
//    .header("Content-Type", "application/json")
//    .body(student)
//.when()
//    .put("/students/1")
//.then()
//    .statusCode(200)
//    .body("firstName", equalTo("Shanti"));

}}
