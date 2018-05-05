package com.ase.eu.travel_buddy;

import java.util.ArrayList;

public class RawRouteData {
    private String name;
    private ArrayList<String> dates;
    private ArrayList<String> points;
    private String imageUri;

    public RawRouteData(String name, ArrayList<String> dates, ArrayList<String> points, String imageUri) {
        this.name = name;
        this.dates = dates;
        this.points = points;
        this.imageUri = imageUri;
    }

    public RawRouteData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }

    public ArrayList<String> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<String> points) {
        this.points = points;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "RawRouteData{" +
                "name='" + name + '\'' +
                ", dates=" + dates +
                ", points=" + points +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}
