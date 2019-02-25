package com.triplethree.models;

public class BasicInfoOfEvCharger {

    private Location location;
    private String stationName;
    private float price;
    private boolean availability;

    public BasicInfoOfEvCharger(Location location,String stationName, float price,boolean availability) {

        this.location = location;
        this.stationName = stationName;
        this.price = price;
        this.availability = availability;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

}
