package com.triplethree.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {
    private  LatLng position;
    private String title;
    private String snippet;
    private  int iconPictures;



    public ClusterMarker(LatLng position, String title, String snippet, int iconPictures) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.iconPictures = iconPictures;

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


    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public int getIconPictures() {
        return iconPictures;
    }

    public void setIconPictures(int iconPictures) {
        this.iconPictures = iconPictures;
    }


}
