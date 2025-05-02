package web.app.api;

public class Playlist {
    private String name;
    private String description;
    private boolean isPublic;
    private int userId;

    public Playlist() {
    }

    public Playlist(String name, String description, boolean isPublic, int userId ) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.userId = userId;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
