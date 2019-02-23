package com.triplethree.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {
    private  LatLng position;
    private String title;
    private String snippet;
    private String snippet2;
    private  int iconPictures;
    private EvCharger evCharger;


    public ClusterMarker(LatLng position, String title, String snippet,String snippet2, int iconPictures, EvCharger evCharger) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.snippet2 = snippet2;
        this.iconPictures = iconPictures;
        this.evCharger = evCharger;
    }

    public ClusterMarker() {
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public String getSnippet2() {
        return snippet2;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public int getIconPictures() {
        return iconPictures;
    }

    public void setIconPictures(int iconPictures) {
        this.iconPictures = iconPictures;
    }

    public EvCharger getEvCharger() {
        return evCharger;
    }

    public void setEvCharger(EvCharger evCharger) {
        this.evCharger = evCharger;
    }
}
