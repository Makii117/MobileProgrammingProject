package com.maki.happyhour.models;

public class UserModel {

    private String Name;
    private String  Location;
    private String Picture;
    private double lat;
    private double lon;
    private String id;
    private UserModel(){}
    private UserModel(String Name,String Picture,double lat, double lon,String Location,String id){
        this.Name=Name;
        this.lat=lat;
        this.lon=lon;
        this.Picture=Picture;
        this.Location=Location;
        this.id=id;
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

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
}
