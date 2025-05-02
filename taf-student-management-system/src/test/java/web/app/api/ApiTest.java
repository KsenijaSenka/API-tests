package web.app.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ApiTest {
    @Test
    public void testGetStudents() {
        String endpoint = "http://localhost:8888/students";
        when().get(endpoint).then().log().body();
    }

    @Test
    public void testGetStudentById() {
        String endpoint = "http://localhost:8888/students";
        given().queryParam("id", 1).
                when().
                get(endpoint).
                then().assertThat().statusCode(200).
                body("id", equalTo(1));
    }

    @Test
    public void updateStudent() {

        RestAssured.baseURI = "http://localhost:8888";

        Student updatedStudent = new Student(
                "Emmanuel",
                "Garcia",
                "emmanuel_garcia@anywhere.school"
        );


        Response response = given()
                .header("Content-Type", "application/json")
                .body(updatedStudent)
                .when()
                .put("/students/1");

        assertThat(response.getStatusCode(), equalTo(200));

        assertThat(response.jsonPath().getString("firstName"), equalTo("Emmanuel"));
        assertThat(response.jsonPath().getString("lastName"), equalTo("Garcia"));
        assertThat(response.jsonPath().getString("email"), equalTo("emmanuel_garcia@anywhere.school"));


    }
    @Test
    public void updateStudentFirstName() {

        RestAssured.baseURI = "http://localhost:8888";

        Student updatedStudent = new Student();
                updatedStudent.setFirstName("Shanti");




        Response response = given()
                .header("Content-Type", "application/json")
                .body(updatedStudent)
                .when()
                .put("/students/1");


        assertThat(response.getStatusCode(), equalTo(200));

        assertThat(response.jsonPath().getString("firstName"), equalTo("Shanti"));



    }

    @Test
    public void deleteStudentPathParamId() {
        String endpoint = "http://localhost:8888/students/{id}";

        given().pathParam("id", 9).when().delete(endpoint)
                .then()
                .statusCode(200);
    }

    @Test
    public void deleteStudentDeleteParamId() {
        String endpoint = "http://localhost:8888/students/{id}";
        int id = 10;
        given().when().delete(endpoint, id)
                .then()
                .statusCode(200);

    }

    @Test
    public void deleteStudentEndpointId() {
        String endpoint = "http://localhost:8888/students/19";
        given()
                .when()
                .delete(endpoint)
                .then()
                .statusCode(200);
    }
}


