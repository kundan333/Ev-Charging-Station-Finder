package com.triplethree.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.triplethree.models.BasicInfoOfEvCharger;
import com.triplethree.models.EvStation;
import com.triplethree.models.HomeStaion;
import com.triplethree.models.ShareableBattery;
import com.triplethree.slytherine.R;

public class CustomInfoWindowAdapter implements  GoogleMap.InfoWindowAdapter{

    private View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;

        try {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindow = mInflater.inflate(R.layout.custom_info_window, null);
      }catch (Exception e){
          e.printStackTrace();
      }

    }

    private void rendowWindowText(Marker marker, View view){

        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

        Gson gson = new Gson();
        String snippet = marker.getSnippet();

        int type = Integer.parseInt(String.valueOf(snippet.charAt(0)));

        String ChargerClass = snippet.substring(1);

        if (type==1){
            EvStation evStation= gson.fromJson(ChargerClass , EvStation.class);
            setStationName(evStation.getBasicInfoOfEvCharger().getStationName(),view);
            setAvailability(evStation.getBasicInfoOfEvCharger().isAvailability(),view);
            setPrice(evStation.getBasicInfoOfEvCharger().getPrice(),view);
        }
        else  if (type==2){
            HomeStaion homeStaion= gson.fromJson(ChargerClass , HomeStaion.class);
            setStationName(homeStaion.getBasicInfoOfEvCharger().getStationName(),view);

            setAvailability(homeStaion.getBasicInfoOfEvCharger().isAvailability(),view);
            setPrice(homeStaion.getBasicInfoOfEvCharger().getPrice(),view);
        }
        else if(type==3){
            ShareableBattery shareableBattery= gson.fromJson(ChargerClass , ShareableBattery.class);
            setStationName(shareableBattery.getBasicInfoOfEvCharger().getStationName(),view);

            setAvailability(shareableBattery.getBasicInfoOfEvCharger().isAvailability(),view);
            setPrice(shareableBattery.getBasicInfoOfEvCharger().getPrice(),view);
        }


       // BasicInfoOfEvCharger basicInfoOfEvCharger= gson.fromJson(snippet, BasicInfoOfEvCharger.class);

/*
        TextView tvSnippet2 = (TextView) view.findViewById(R.id.snippet2);
        tvSnippet2.setText(String.valueOf(basicInfoOfEvCharger.getPrice()));

        TextView availability = (TextView)view.findViewById(R.id.ImageViewText);
        if (basicInfoOfEvCharger.isAvailability()) {
            String avilable = "Avilable";
            avilability.setText(avilable);
        }else {
            String notAvilable = "Not Avilable";
            avilability.setText(notAvilable);
        }
        */

    }

    private void setStationName(String stationName,View view){
        TextView tvSnippet = (TextView) view.findViewById(R.id.stationName);
        tvSnippet.setText(stationName);
    }

    private void setAvailability(boolean isAvailable,View view){
        TextView availability = (TextView) view.findViewById(R.id.availability);
        if (isAvailable){
            String available ="Available";
            availability.setText(available);

            availability.setTextColor(Color.parseColor("#237A32"));

        }else {
            String available ="Not Available";
            availability.setText(available);

            availability.setTextColor(Color.parseColor("#FFE91E36"));

        }


    }
    private void setPrice(float price,View view){
        TextView priceTextView = (TextView)view.findViewById(R.id.price);
        String priceS = String.valueOf(price);
        priceTextView.setText(priceS);


    }



    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

}
