package web.app.api;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class ApiTest {
    @BeforeClass
    public void configureRestAssured() {
        RestAssured.config = RestAssured.config()
                .objectMapperConfig(
                        io.restassured.config.ObjectMapperConfig.objectMapperConfig()
                                .defaultObjectMapperType(ObjectMapperType.GSON)
                );
    }

    @Test
    public void testGetPlaylists() {
        String endpoint = "http://localhost:8888/api/playlists";
        when().get(endpoint).then().log().body();
    }

    private int createPlaylist(String name, String description, boolean isPublic, int userId) {
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

    @Test
    public void testCreatePlaylist() {
        int playlistId = ApiHelper.createPlaylist("Winter songs",
                "Design for good mood creation in winter", true, 10);
        System.out.println("Created Playlist ID: " + playlistId);
    }

    @Test
    public void testRetrieveCreatedPlaylist() {
        int playlistId = createPlaylist("Winter songs", "Design for good mood creation in winter",
                true, 10);

        given()
                .pathParam("id", playlistId)
                .when()
                .get("http://localhost:8888/api/playlists/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Winter songs"))
                .body("description", equalTo("Design for good mood creation in winter"))
                .body("userId", equalTo(10))
                .body("isPublic", equalTo(true));
    }

    @Test
    public void testUpdatePlaylistAttributes() {

        int playlistId = createPlaylist("Autumn Beats", "Perfect for cozy evenings",
                true, 10);

        Playlist updatedPlaylist = new Playlist("Updated Autumn Mix", "New vibes for autumn",
                false, 10);

        given()
                .header("Content-Type", "application/json")
                .pathParam("id", playlistId)
                .body(updatedPlaylist)
                .when()
                .put("http://localhost:8888/api/playlists/{id}")
                .then()
                .statusCode(200);

        given()
                .pathParam("id", playlistId)
                .when()
                .get("http://localhost:8888/api/playlists/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Autumn Mix"))
                .body("description", equalTo("New vibes for autumn"))
                .body("isPublic", equalTo(false))
                .body("userId", equalTo(10));
    }

    @Test
    public void testAddTrackAndVerify() {

        int playlistId = createPlaylist("Chill Vibes", "Smooth and mellow sounds",
                true, 10);

        String addTrack = "{ \"trackId\": 1 }";

        Response addResponse = given()
                .header("Content-Type", "application/json")
                .pathParam("id", playlistId)
                .body(addTrack)
                .when()
                .post("http://localhost:8888/api/playlists/{id}/tracks/add")
                .then()
                .statusCode(200)
                .body("tracks.find { it.id == 1 }", notNullValue())
                .extract().response();

        given()
                .pathParam("id", playlistId)
                .when()
                .get("http://localhost:8888/api/playlists/{id}")
                .then()
                .statusCode(200)
                .body("tracks.find { it.id == 1 }.title", equalTo("Melodies of Tranquil Harmony"))
                .body("tracks.find { it.id == 1 }.artist", equalTo("Tranquil Solace"));
    }

    public void addTrackToPlaylist(int playlistId, int trackId) {
        String requestBody = "{ \"trackId\": " + trackId + " }";

        given()
                .header("Content-Type", "application/json")
                .pathParam("id", playlistId)
                .body(requestBody)
                .when()
                .post("http://localhost:8888/api/playlists/{id}/tracks/add")
                .then()
                .statusCode(200);
    }

    @Test
    public void testRemoveTrackFromPlaylist() {

        int playlistId = createPlaylist("Chill Vibes", "Smooth and mellow sounds", true, 10);

        addTrackToPlaylist(playlistId, 5);
        addTrackToPlaylist(playlistId, 6);

        String removeTrackJson = "{ \"trackId\": 3 }";
        given()
                .header("Content-Type", "application/json")
                .pathParam("id", playlistId)
                .body(removeTrackJson)
                .when()
                .delete("http://localhost:8888/api/playlists/{id}/tracks/remove")
                .then()
                .statusCode(200);

        given()
                .pathParam("id", playlistId)
                .when()
                .get("http://localhost:8888/api/playlists/{id}")
                .then()
                .statusCode(200)
                .body("tracks.find { it.id == 5 }", nullValue())
                .body("tracks.find { it.id == 6 }", notNullValue());

        given()
                .pathParam("id", 3)
                .when()
                .get("http://localhost:8888/api/tracks/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(5));
    }

    @Test
    public void testDeletePlaylist() {

        int playlistId = createPlaylist("Summer Vibes", "Chill tunes for a hot summer", true, 10);

        given()
                .pathParam("id", playlistId)
                .when()
                .delete("http://localhost:8888/api/playlists/{id}")
                .then()
                .statusCode(200);

        given()
                .when()
                .get("http://localhost:8888/api/playlists")
                .then()
                .statusCode(200)
                .body("id", not(hasItem(playlistId)));

    }
}




