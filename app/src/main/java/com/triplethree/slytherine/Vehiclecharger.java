package com.triplethree.slytherine;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.maps.android.clustering.ClusterManager;
import com.triplethree.models.BasicInfoOfEvCharger;
import com.triplethree.models.ClusterMarker;
import com.triplethree.models.EvCharger;
import com.triplethree.models.EvStation;
import com.triplethree.models.HomeStaion;
import com.triplethree.models.ShareableBattery;
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
   // private DataRetrieve dataRetrieve;
    private volatile boolean dataRetrieved = false;



    private LatLngBounds mMapBoundary;
    private ClusterManager<ClusterMarker> mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();



    private FirebaseFirestore firebaseFirestore;

    private ArrayList<EvCharger> evChargers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiclecharger);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FirebaseApp.initializeApp(this);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        evChargers=  new ArrayList<EvCharger>();
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


    public void addMapMarkers(){

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
            try{

                Log.d(TAG, "addMapMarkers: evCharger size "+evChargers.size());
            for (EvCharger evCharger:evChargers){

                if (evCharger.getType()==1){

                    Gson gson = new Gson();
                    EvStation evStation= gson.fromJson(evCharger.getChargerDetails().toString() , EvStation.class);
                    Log.d(TAG, " => " + evStation.getBasicInfoOfEvCharger().getStationName());
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(evStation.getBasicInfoOfEvCharger().getLocation().getLatitude(),
                                    evStation.getBasicInfoOfEvCharger().getLocation().getLongtitude()),
                            "EV Station",
                            evStation.getBasicInfoOfEvCharger().toString(),
                            evStation.getIcon()
                    );


                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);


                }else if(evCharger.getType()==2){
                    Gson gson = new Gson();
                    HomeStaion homeStaion= gson.fromJson(evCharger.getChargerDetails().toString() , HomeStaion.class);
                    Log.d(TAG, " => " + evCharger.getChargerDetails().toString());
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(homeStaion.getBasicInfoOfEvCharger().getLocation().getLatitude(),
                                    homeStaion.getBasicInfoOfEvCharger().getLocation().getLongtitude()),
                            "Home Station",
                            homeStaion.getBasicInfoOfEvCharger().toString(),
                            homeStaion.getIcon()
                    );


                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);


                }else if (evCharger.getType()==3){

                    Gson gson = new Gson();
                    ShareableBattery shareableBattery= gson.fromJson(evCharger.getChargerDetails().toString() , ShareableBattery.class);
                    Log.d(TAG, " => " + shareableBattery.getBasicInfoOfEvCharger().getStationName());
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(shareableBattery.getBasicInfoOfEvCharger().getLocation().getLatitude(),
                                    shareableBattery.getBasicInfoOfEvCharger().getLocation().getLongtitude()),
                            "Shareable Battery",
                            shareableBattery.getBasicInfoOfEvCharger().toString(),
                            shareableBattery.getIcon()
                    );


                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);


                }

            }



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
           // dataRetrieve = new DataRetrieve(this);
            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
            //dataRetrieved = dataRetrieve.isDataRetrieved();
            loadData();


             


        }
        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

    }


    private void loadData(){
        firebaseFirestore.collection("EvChargerTest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                EvCharger evCharger = document.toObject(EvCharger.class);
                                // EvChargersInfo.addEvChrger(evCharger);


                                addEvCharger(evCharger);
                                Log.d(TAG, "loadData: array size "+evChargers.size());

                                if (evCharger.getType()==1){

                                    Gson gson = new Gson();
                                    EvStation evStation= gson.fromJson(evCharger.getChargerDetails().toString() , EvStation.class);
                                    Log.d(TAG, document.getId() + " => " + evStation.getBasicInfoOfEvCharger().getStationName());


                                }
                                Log.d(TAG, document.getId() + " => " + evCharger.getType());
                            }
                            Log.d(TAG, "onComplete: true");
                            addMapMarkers();
                            dataRetrieved = true;

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

/*

    class CheckDataRetrieved extends AsyncTask<Void, Void, Boolean> {
        Context context;
        CheckDataRetrieved(Context context){this.context=context;}
        @Override
        protected void onPostExecute(Boolean result){
            if(result){
                dataRetrieved = true;
                addMapMarkers(context);}
            else{
                dataRetrieved = false;}


        }
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: InBackground");
            dataRetrieved=  dataRetrieve.isDataRetrieved();

            try {
                Thread.sleep(2000);
            }catch (Exception e){}
            addMapMarkers(context);
            Log.d(TAG, "doInBackground: data dataRetrieved = "+dataRetrieved);
            return true;
        }
    }
*/

    private void addEvCharger(EvCharger evCharger){
        evChargers.add(evCharger);
        Log.d(TAG, "addEvCharger: outside size" +evChargers.size());
    }

}
