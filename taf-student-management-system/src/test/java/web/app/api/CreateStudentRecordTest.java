package web.app.api;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreateStudentRecordTest {
    @Test
    public void createFullStudent() {
        String body = """
            {
                "firstName": "Jack",
                "lastName": "Daniel",
                "email": "jack_daniel@anywhere.school"
            }
        """;

        given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("http://localhost:8888/students")
                .then()
                .statusCode(200);
    }
    @Test
    public void createStudentWithFirstNameOnly() {
        String body = """
        {
            "firstName": "Smith"
        }
    """;

        given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("http://localhost:8888/students")
                .then()
                .statusCode(200);
    }

}
