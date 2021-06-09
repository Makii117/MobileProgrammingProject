package com.maki.happyhour.models;

import android.net.Uri;

import java.lang.reflect.Array;

public class UserModel {

    private String name;
    private String  location;
    private Uri imgUri;
    private float lat;
    private float lon;
    private UserModel(){}
    private UserModel(String name,Uri imgUri,float lat, float lon){
        this.name=name;
        this.lat=lat;
        this.lon=lon;
        this.imgUri=imgUri;
    }


    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImgUri() {
        return imgUri;
    }
}
