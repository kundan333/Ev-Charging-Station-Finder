package com.triplethree.models;

public class EvCharger {
    private int type;
    private Object chargerDetails;

    public EvCharger() {
    }

    public EvCharger(Object chargerDetails) {
        this.chargerDetails = chargerDetails;
        if (chargerDetails.getClass()==EvStation.class){
            this.type=1;
        }else if (chargerDetails.getClass()== HomeStaion.class){
            this.type=2;
        }else if (chargerDetails.getClass()==ShareableBattery.class){
            this.type=3;
        }

    }

    public Object getChargerDetails() {
        return chargerDetails;
    }

    public int getType() {
        return type;
    }

}
