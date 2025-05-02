package web.app.api;

public class Track {
    private int trackId;
    private String title;
    private String artist;
    private int duration;
    private int year;
    private String album;

    public Track() {
    }
    public Track (int trackId) {
        this.trackId = trackId;
    }
    public Track(int trackId,  String title, String artist, int duration, int year, String album) {
        this.trackId = trackId;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.year = year;
        this.album = album;
    }

    public int getTrackId() {
        return trackId;
    }
}

