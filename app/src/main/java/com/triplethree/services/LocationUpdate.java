package com.triplethree.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.triplethree.models.ClusterMarker;
import com.triplethree.models.EvCharger;
import com.triplethree.slytherine.ActivityRefer;
import com.triplethree.slytherine.Vehiclecharger;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class LocationUpdate extends Service implements Parcelable {

        private static final String TAG = "LocationService";

    private ArrayList<ClusterMarker> mClusterMarkers;
    private ArrayList<EvCharger> evChargers;

    private FusedLocationProviderClient mFusedLocationClient;
        private final static long UPDATE_INTERVAL = 4 * 1000;  /* 4 secs */
        private final static long FASTEST_INTERVAL = 2000; /* 2 sec */


        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();



            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            if (Build.VERSION.SDK_INT >= 26) {
                String CHANNEL_ID = "my_channel_01";
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        "My Channel",
                        NotificationManager.IMPORTANCE_DEFAULT);

                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("")
                        .setContentText("").build();

                startForeground(1, notification);
            }




        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d(TAG, "onStartCommand: called.");
            getLocation();

            Gson gson = new Gson();
            String value = intent.getStringExtra("evchargers");
            evChargers = gson.fromJson(value, new TypeToken<List<EvCharger>>(){}.getType());
            Log.d(TAG, "onStartCommand: size of evchargers "+evChargers.size());

            Vehiclecharger vehiclecharger = (Vehiclecharger)getApplicationContext();
            vehiclecharger.clearMarkers();






//            Log.d(TAG, "onStartCommand: 1st station name   "+evChargers.get(0).getChargerDetails().toString());

            return START_NOT_STICKY;
        }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getBaseContext());
    }

        private void getLocation() {


            // ---------------------------------- LocationRequest ------------------------------------
            // Create the location request to start receiving updates
            LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
            mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequestHighAccuracy.setInterval(UPDATE_INTERVAL);
            mLocationRequestHighAccuracy.setFastestInterval(FASTEST_INTERVAL);


            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getLocation: stopping the location service.");
                stopSelf();
                return;
            }
            Log.d(TAG, "getLocation: getting location information.");
            mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {

                            Log.d(TAG, "onLocationResult: got location result.");

                            Location location = locationResult.getLastLocation();

                        }
                    },
                    Looper.myLooper()); // Looper.myLooper tells this to repeat forever until thread is destroyed
        }/*

        private void saveUserLocation(final UserLocation userLocation){

            try{
                DocumentReference locationRef = FirebaseFirestore.getInstance()
                        .collection(getString(R.string.collection_user_locations))
                        .document(FirebaseAuth.getInstance().getUid());

                locationRef.set(userLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: \ninserted user location into database." +
                                    "\n latitude: " + userLocation.getGeo_point().getLatitude() +
                                    "\n longitude: " + userLocation.getGeo_point().getLongitude());
                        }
                    }
                });
            }catch (NullPointerException e){
                Log.e(TAG, "saveUserLocation: User in00+" +
                        "stance is null, stopping location service.");
                Log.e(TAG, "saveUserLocation: NullPointerException: "  + e.getMessage() );
                stopSelf();
            }

        }
*/



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
