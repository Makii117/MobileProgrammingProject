package com.maki.happyhour.models;

import android.net.Uri;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;

public class UserModel {

    private String Name;
    private String  Location;
    private String Picture;
    private float lat;
    private float lon;
    private UserModel(){}
    private UserModel(String Name,String Picture,float lat, float lon,String Location){
        this.Name=Name;
        this.lat=lat;
        this.lon=lon;
        this.Picture=Picture;
        this.Location=Location;
    }


    public String getLocation() {
        return Location;
    }

    public String getName() {
        return Name;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPicture() {
        return Picture;
    }
}
