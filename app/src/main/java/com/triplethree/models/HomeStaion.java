package com.triplethree.models;

import com.triplethree.slytherine.R;

public class HomeStaion {
    private BasicInfoOfEvCharger basicInfoOfEvCharger;
    private boolean visibility;
    private int icon;

    public HomeStaion(BasicInfoOfEvCharger basicInfoOfEvCharger, boolean visibility) {
        this.basicInfoOfEvCharger = basicInfoOfEvCharger;
        this.visibility = visibility;
        updateIcon();
    }

    public BasicInfoOfEvCharger getBasicInfoOfEvCharger() {
        return basicInfoOfEvCharger;
    }

    public boolean isVisibility() {
        return visibility;
    }

    private void updateIcon(){

        if (visibility){
            icon = R.drawable.homestation;
        }
        else if(!visibility) {
            icon= R.drawable.disablehomestation;
        }
        else if (!basicInfoOfEvCharger.isAvailability()){
            icon = R.drawable.disablehomestation;
        }
    }

    public int getIcon() {
        return icon;
    }
}
