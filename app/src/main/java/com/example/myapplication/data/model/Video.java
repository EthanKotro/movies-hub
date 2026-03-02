package com.example.myapplication.data.model;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("type")
    private String type;

    @SerializedName("official")
    private boolean official;

    public Video() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public String getYoutubeUrl() {
        if ("YouTube".equalsIgnoreCase(site) && key != null) {
            return "https://www.youtube.com/watch?v=" + key;
        }
        return null;
    }

    public String getYoutubeThumbnailUrl() {
        if ("YouTube".equalsIgnoreCase(site) && key != null) {
            return "https://img.youtube.com/vi/" + key + "/hqdefault.jpg";
        }
        return null;
    }
}
