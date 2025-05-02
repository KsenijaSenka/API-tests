package web.app.api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiHelper {
    public static int createPlaylist(String name, String description, boolean isPublic, int userId) {
        Playlist playlist = new Playlist(name, description, isPublic, userId);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(playlist)
                .when()
                .post("http://localhost:8888/api/playlists")
                .then()
                .statusCode(201)
                .extract().response();

        return response.path("id");
    }
}