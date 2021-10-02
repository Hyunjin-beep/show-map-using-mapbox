package com.example.assignment6_hc;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class FaveLocations {
    private LatLng location;
    private String title;
    private int image;

    public FaveLocations(LatLng location, String title, int image){
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

    public int getImage(){
        return this.image;
    }





}
