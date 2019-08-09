package com.example.androidtouristguide.Model;

public class Places {

    private String name, descripton, locationLatitude, image, category, locationLongitude;

    public Places() {
    }

    public Places(String name, String descripton, String locationLatitude, String image, String category, String locationLongitude) {
        this.name = name;
        this.descripton = descripton;
        this.locationLatitude = locationLatitude;
        this.image = image;
        this.category = category;
        this.locationLongitude = locationLongitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripton() {
        return descripton;
    }

    public void setDescripton(String descripton) {
        this.descripton = descripton;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }
}
