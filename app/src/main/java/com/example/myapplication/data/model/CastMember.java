package com.example.myapplication.data.model;

import com.google.gson.annotations.SerializedName;

public class CastMember {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("character")
    private String character;

    @SerializedName("profile_path")
    private String profilePath;

    @SerializedName("order")
    private int order;

    @SerializedName("known_for_department")
    private String knownForDepartment;

    public CastMember() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getKnownForDepartment() {
        return knownForDepartment;
    }

    public void setKnownForDepartment(String knownForDepartment) {
        this.knownForDepartment = knownForDepartment;
    }

    public String getFullProfilePath() {
        if (profilePath == null || profilePath.isEmpty())
            return null;
        return "https://image.tmdb.org/t/p/w185" + profilePath;
    }
}
