package com.triplethree.models;

import com.triplethree.slytherine.R;

public class EvStation {
     private  BasicInfoOfEvCharger basicInfoOfEvCharger;
     private int icon;


    public EvStation() {
    }

    public EvStation(BasicInfoOfEvCharger basicInfoOfEvCharger) {
        this.basicInfoOfEvCharger = basicInfoOfEvCharger;
        updateIcon();

    }


    public BasicInfoOfEvCharger getBasicInfoOfEvCharger() {
        return basicInfoOfEvCharger;
    }

    private void updateIcon(){
        if (basicInfoOfEvCharger.isAvailability()){
            icon = R.drawable.ev_station;
        }else {
            icon = R.drawable.disableevstations;
        }

    }

    public int getIcon() {
        return icon;
    }
}
