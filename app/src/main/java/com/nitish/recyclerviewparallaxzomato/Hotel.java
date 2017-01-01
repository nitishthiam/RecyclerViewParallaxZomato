package com.nitish.recyclerviewparallaxzomato;

/**
 * Created by nitish on 11-Nov-16.
 */

public class Hotel {

    //pojo

    private String name, address, locality, city, latitude, longitude, cuisines, imageUrl;

    public Hotel(String name, String address, String locality, String city, String latitude, String longitude, String cuisines, String imageUrl) {
        this.name = name;
        this.address = address;
        this.locality = locality;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cuisines = cuisines;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
