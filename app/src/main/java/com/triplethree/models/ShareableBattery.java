package com.triplethree.models;

import com.triplethree.slytherine.R;

public class ShareableBattery {
    private BasicInfoOfEvCharger basicInfoOfEvCharger;
    private boolean visibility;
    private int icon;

    public ShareableBattery(BasicInfoOfEvCharger basicInfoOfEvCharger, boolean visibility) {
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
        if (basicInfoOfEvCharger.isAvailability()){
            icon = R.drawable.shareablebattery;
        }
        else {
            icon = R.drawable.diableshareablebattery;
        }


    }

    public int getIcon() {
        return icon;
    }
}
