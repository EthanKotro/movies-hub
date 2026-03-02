package com.example.myapplication.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditsResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("cast")
    private List<CastMember> cast;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CastMember> getCast() {
        return cast;
    }

    public void setCast(List<CastMember> cast) {
        this.cast = cast;
    }
}
