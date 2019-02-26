package com.triplethree.utils;

import com.triplethree.models.EvCharger;

import java.util.ArrayList;

public class EvChargersInfo {
    private static ArrayList<EvCharger> evChargers;


    public static void init(){
        evChargers = new ArrayList<EvCharger>();
    }

    public static  void addEvChrger(EvCharger evCharger){
        evChargers.add(evCharger);
    }

    public static ArrayList<EvCharger> getEvChargers() {
        return evChargers;
    }


}
