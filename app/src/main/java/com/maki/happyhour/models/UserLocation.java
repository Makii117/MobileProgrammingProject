package com.maki.happyhour.models;

public class UserLocation {
    private static double lon;
    private static double lat;
    private String Name;
    private String Picture;


    private UserLocation(){}
    private UserLocation(String Name,String Picture,double lat, double lon){
        this.Name=Name;
        this.lat=lat;
        this.lon=lon;
        this.Picture=Picture;

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

    public static double getLat() {
        return lat;
    }

    public static double getLon() {
        return lon;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setPicture(String Picture) {
        this.Picture = Picture;
    }

    public String getPicture() {
        return Picture;
    }
}
