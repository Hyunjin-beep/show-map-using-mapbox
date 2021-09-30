package com.example.assignment6_hc;

public class FaveLocations {
    private double lat;
    private double lng;
    private String title;

    public FaveLocations(double lat, double lng, String title){
        this.lat = lat;
        this.lng = lng;
        this.title = title;
    }

    public String toString() {
        return lat + ", " + lng + ", " +title;// your string representation
    }

}
