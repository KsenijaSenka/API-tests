package web.app.api;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.parallel.Execution;
    import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@Execution(ExecutionMode.CONCURRENT)
public class ApiTest {

    @Test
    public void testGetUsers() {
        String endpoint = "https://jsonplaceholder.typicode.com/users";
        when().get(endpoint).then().log().body();
    }

    @Test
    public void verifyStatusCode() {
        Response response =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/users");

        int actualStatusCode = response.getStatusCode();
        assertThat("Expected HTTP status code to be 200 but was " + actualStatusCode,
                actualStatusCode, is(200));
    }

    @Test
    public void verifyContentTypeHeader() {
        Response response =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/users");

        int statusCode = response.getStatusCode();
        assertThat("Expected status code 200 but got " + statusCode, statusCode, is(200));

        String contentTypeHeader = response.getHeader("Content-Type");
        assertThat("Expected 'Content-Type' header to be present", contentTypeHeader, is(notNullValue()));

        assertThat("Expected 'Content-Type' header to be 'application/json; charset=utf-8' but was '"
                        + contentTypeHeader + "'",
                contentTypeHeader, equalTo("application/json; charset=utf-8"));
        ;
    }

    @Test
    public void verifyResponseBody() {
        Response response =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/users");

        int statusCode = response.getStatusCode();
        assertThat("Expected HTTP status code to be 200 but was " + statusCode, statusCode, is(200));

        List<?> users = response.jsonPath().getList("$");

        assertThat("Expected 10 users in the response but found " + users.size(), users.size(), is(10));
    }

    @Test
    public void createUser() {
        String requestBody = "{ \"name\": \"John Doe\", \"username\": \"johndoe\" }";

        Response response =
                given()
                        .contentType("application/json")
                        .body(requestBody)
                        .when()
                        .post("https://jsonplaceholder.typicode.com/users");

        int statusCode = response.getStatusCode();
        assertThat("Expected HTTP status code 201 (Created) but was " + statusCode, statusCode, is(201));
    }

    @Test
    public void updateUserEmail() {

        String updatedUserJson = """
                {
                  "id": 1,
                  "name": "Leanne Graham",
                  "username": "Bret",
                  "email": "leanne.graham@biz.com",
                  "address": {
                    "street": "Kulas Light",
                    "suite": "Apt. 556",
                    "city": "Gwenborough",
                    "zipcode": "92998-3874",
                    "geo": {
                      "lat": "-37.3159",
                      "lng": "81.1496"
                    }
                  },
                  "phone": "1-770-736-8031 x56442",
                  "website": "hildegard.org",
                  "company": {
                    "name": "Romaguera-Crona",
                    "catchPhrase": "Multi-layered client-server neural-net",
                    "bs": "harness real-time e-markets"
                  }
                }
                """;

        Response response =
                given()
                        .contentType("application/json")
                        .body(updatedUserJson)
                        .when()
                        .put("https://jsonplaceholder.typicode.com/users/1");

        int statusCode = response.getStatusCode();
        assertThat("Expected HTTP status code 200 (OK) but was " + statusCode, statusCode, is(200));

        String email = response.jsonPath().getString("email");
        assertThat("Expected email to be updated to 'leanne.graham@biz.com' but was " + email,
                email, equalTo("leanne.graham@biz.com"));

    }

    @Test
    public void deleteUser() {
        Response response =
                given()
                        .when()
                        .delete("https://jsonplaceholder.typicode.com/users/1");

        int statusCode = response.getStatusCode();
        assertThat("Expected HTTP status code 200 or 204 on successful deletion, but was " + statusCode,
                statusCode, anyOf(is(200), is(204)));
    }

    int userId = 1;

    @Test
    public void deleteUserWithPathParamId() {

        Response deleteResponse =
                given()
                        .pathParam("id", userId)
                        .when()
                        .delete("https://jsonplaceholder.typicode.com/users/{id}");

        int deleteStatusCode = deleteResponse.getStatusCode();
        assertThat("Expected HTTP status code 200 or 204 for successful deletion of user ID " + userId + ", but got " + deleteStatusCode,
                deleteStatusCode, anyOf(is(200), is(204)));

        Response getResponse =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/users");

        int getStatusCode = getResponse.getStatusCode();
        assertThat("Expected HTTP status code 200 for retrieving the user list, but got " + getStatusCode,
                getStatusCode, is(200));

        assertThat("User ID " + userId + " should no longer exist in the user list after deletion, but it was found.",
                getResponse.jsonPath().getList("id"), not(hasItem(userId)));
    }
}
