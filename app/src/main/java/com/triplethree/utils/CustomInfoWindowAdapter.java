package com.triplethree.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.triplethree.models.BasicInfoOfEvCharger;
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
        BasicInfoOfEvCharger basicInfoOfEvCharger= gson.fromJson(snippet, BasicInfoOfEvCharger.class);
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);

        if(!snippet.equals("")){
            tvSnippet.setText(basicInfoOfEvCharger.getStationName());
        }

        TextView tvSnippet2 = (TextView) view.findViewById(R.id.snippet2);
        tvSnippet2.setText(String.valueOf(basicInfoOfEvCharger.getPrice()));

        TextView avilability = (TextView)view.findViewById(R.id.ImageViewText);
        if (basicInfoOfEvCharger.isAvailability()) {
            String avilable = "Avilable";
            avilability.setText(avilable);
        }else {
            String notAvilable = "Not Avilable";
            avilability.setText(notAvilable);
        }

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
