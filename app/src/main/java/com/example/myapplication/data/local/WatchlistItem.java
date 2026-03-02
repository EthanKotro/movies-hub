package com.example.myapplication.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "watchlist")
public class WatchlistItem {

    @PrimaryKey
    private int id;

    private String title;
    private String posterPath;
    private String overview;
    private String mediaType; // "movie" or "tv"
    private double voteAverage;
    private String releaseDate;
    private long addedAt;
    private String status; // e.g., "Plan to Watch", "Currently Watching", "Completed"

    public WatchlistItem() {
    }

    @androidx.room.Ignore
    public WatchlistItem(int id, String title, String posterPath, String overview,
            String mediaType, double voteAverage, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.mediaType = mediaType;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.status = "Plan to Watch"; // default status
        this.addedAt = System.currentTimeMillis();
    }

    @androidx.room.Ignore
    public WatchlistItem(int id, String title, String posterPath, String overview,
            String mediaType, double voteAverage, String releaseDate, String status) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.mediaType = mediaType;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.status = status;
        this.addedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(long addedAt) {
        this.addedAt = addedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullPosterPath() {
        if (posterPath == null || posterPath.isEmpty())
            return null;
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }
}
