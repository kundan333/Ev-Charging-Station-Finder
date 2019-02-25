package com.triplethree.slytherine;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;
import com.triplethree.models.BasicInfoOfEvCharger;
import com.triplethree.models.ClusterMarker;
import com.triplethree.models.EvCharger;
import com.triplethree.models.EvStation;
import com.triplethree.utils.CustomInfoWindowAdapter;
import com.triplethree.utils.EvChargersInfo;
import com.triplethree.utils.MyClusterManagerRenderer;

import java.util.ArrayList;

public class Vehiclecharger extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "VehicleChager";
    private GoogleMap mMap;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLoactionPermissonGranted = false;
    private static final int LOCATION_PERMISSION_GRANTED_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;



    private LatLngBounds mMapBoundary;
    private ClusterManager<ClusterMarker> mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiclecharger);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        getLocationPermission();
    }


    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: device location called");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLoactionPermissonGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM,"this is my location");
                            }

                        } else {
                            Toast.makeText(Vehiclecharger.this, "unable to find location", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

        } catch (SecurityException e) {

        }
    }

    private void moveCamera(LatLng latLng, float zoom,String title) {
        Log.d(TAG, "moveCamera : moving the camera to :lat " + latLng.latitude + " , lng " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        //MarkerOptions options = new MarkerOptions().position(latLng).title(title);
        //mMap.addMarker(options);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        addMapMarkers(latLng);
    }


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_GRANTED_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                            mLoactionPermissonGranted = false;
                        return;
                    }
                }
                mLoactionPermissonGranted = true;

                initMap();

            }
        }

    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLoactionPermissonGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_GRANTED_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_GRANTED_REQUEST_CODE);
        }


    }


    private void addMapMarkers(LatLng latLng){

        if(mMap != null){

            if(mClusterManager == null){
                mClusterManager = new ClusterManager<ClusterMarker>(this.getApplicationContext(), mMap);
            }
            if(mClusterManagerRenderer == null){
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        this,
                        mMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            com.triplethree.models.Location  location  =
                    new com.triplethree.models.Location(latLng.latitude+0.1,latLng.longitude);
            BasicInfoOfEvCharger basicInfoOfEvCharger = new BasicInfoOfEvCharger(location, "Maharaja", 5f,true);
            EvStation  evStation = new EvStation(basicInfoOfEvCharger);
            EvCharger evCharger = new EvCharger(evStation);
            EvChargersInfo.init();
            EvChargersInfo.addEvChrger(evCharger);

/*
            for(UserLocation userLocation: mUserLocations){

                Log.d(TAG, "addMapMarkers: location: " + userLocation.getGeo_point().toString());
                try{
                    String snippet = "";
                    if(userLocation.getUser().getUser_id().equals(FirebaseAuth.getInstance().getUid())){
                        snippet = "This is you";
                    }
                    else{
                        snippet = "Determine route to " + userLocation.getUser().getUsername() + "?";
                    }

                    int avatar = R.drawable.ev_station; // set the default avatar
                    try{
                        avatar = Integer.parseInt(userLocation.getUser().getAvatar());
                    }catch (NumberFormatException e){
                        Log.d(TAG, "addMapMarkers: no avatar for " + userLocation.getUser().getUsername() + ", setting default.");
                    }
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(userLocation.getGeo_point().getLatitude(), userLocation.getGeo_point().getLongitude()),
                            userLocation.getUser().getUsername(),
                            snippet,
                            avatar,
                            userLocation.getUser()
                    );
                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);

                }catch (NullPointerException e){
                    Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage() );
                }

            }
            */

          try{
                String snippet = "This is you";

                        int avatar =R.drawable.ev_station ; // set the default avatar
                        ClusterMarker newClusterMarker = new ClusterMarker(
                                   new LatLng(latLng.latitude, latLng.longitude),
                        "Ev Station",
                        snippet,"yess",
                        avatar,
                        evCharger

                );

                mClusterManager.addItem(newClusterMarker);
                mClusterMarkers.add(newClusterMarker);

            }catch (NullPointerException e){
                Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage() );
            }

            mClusterManager.cluster();

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLoactionPermissonGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
        }
        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

    }






}
