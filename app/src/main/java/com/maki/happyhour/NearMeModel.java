package com.maki.happyhour;

public class NearMeModel {
    private int imageUri;
    private String title;
    private String shortDescription;

    public NearMeModel(){

    }
    public NearMeModel(int imageUri, String title, String shortDescription){
        setImageUri(imageUri);
        setTitle(title);
        setShortDescription(shortDescription);
    }

    public int getImageUri() {
        return imageUri;
    }

    public void setImageUri(int imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
