package com.example.assignment6_hc;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class FaveLocations {
    private LatLng location;
    private String title;
    private String image;

    public FaveLocations(LatLng location, String title){
        this.location = location;
        this.title = title;
        this.image = image;
    }

    public LatLng getLocation(){
        return this.location;
    }

    public String getTitle(){
        return this.title;
    }

    public String getImage(){
        return this.image;
    }





}
