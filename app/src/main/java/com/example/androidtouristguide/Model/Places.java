package com.example.androidtouristguide.Model;

public class Places {

    private String name, description, locationLatitude, image, category, locationLongitude, distance;

    public Places() {
    }

    public Places(String name, String descripton, String locationLatitude, String image, String category, String locationLongitude, String distance) {
        this.name = name;
        this.description = descripton;
        this.locationLatitude = locationLatitude;
        this.image = image;
        this.category = category;
        this.locationLongitude = locationLongitude;
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescripton(String descripton) {
        this.description = descripton;
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
