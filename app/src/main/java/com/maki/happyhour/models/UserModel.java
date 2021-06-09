package com.maki.happyhour.models;

import android.location.Geocoder;
import android.net.Uri;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.Locale;

public class UserModel {

    private String Name;
    private String  Location;
    private String Picture;
    private double lat;
    private double lon;
    private UserModel(){}
    private UserModel(String Name,String Picture,double lat, double lon,String Location){
        this.Name=Name;
        this.lat=lat;
        this.lon=lon;
        this.Picture=Picture;
        this.Location=Location;
    }


    public String getLocation() {

        //setLocation();
        return Location;
    }

    public String getName() {
        return Name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
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
