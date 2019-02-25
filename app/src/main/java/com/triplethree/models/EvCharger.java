package com.triplethree.models;

public class EvCharger {
    private int type;
    private EvStation evStation;
    private HomeStaion homeStaion;
    private ShareableBattery shareableBattery;


    public EvCharger(EvStation evStation) {
        this.evStation = evStation;
        this.type =  1;

    }

    public EvCharger(HomeStaion homeStaion) {
        this.homeStaion = homeStaion;
        this.type = 2;
    }

    public EvCharger(ShareableBattery shareableBattery) {
        this.shareableBattery = shareableBattery;
        this.type =3;
    }

    public int getType() {
        return type;
    }


}
