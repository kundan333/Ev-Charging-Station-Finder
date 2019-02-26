package com.triplethree.slytherine;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.triplethree.models.BasicInfoOfEvCharger;
import com.triplethree.models.EvCharger;
import com.triplethree.models.EvStation;
import com.triplethree.utils.EvChargersInfo;

import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.Map;

public class UpdateCharger extends AppCompatActivity {

    private static final String TAG ="UpdateCharger";

    Button button;
    TextInputEditText textInputEditTextLatitude;
    TextInputEditText textInputEditTextLongtitude;
    TextInputEditText textInputEditTextStationName;
    TextInputEditText textInputEditTextPrice;
    AppCompatCheckBox checkBoxVisibility;
    AppCompatCheckBox checkBoxAvilability;
    AppCompatCheckBox checkBoxEvStation;
    AppCompatCheckBox checkBoxHomeStation;
    AppCompatCheckBox checkBoxShareableBattery;




    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.evupdateform);
        FirebaseApp.initializeApp(this);

        textInputEditTextLatitude = findViewById(R.id.latitude);
        textInputEditTextLongtitude = findViewById(R.id.longitude);
        textInputEditTextStationName = findViewById(R.id.stationName);
        textInputEditTextPrice = findViewById(R.id.price);
        checkBoxVisibility= findViewById(R.id.visibility);
        checkBoxAvilability= findViewById(R.id.availability);
        checkBoxEvStation= findViewById(R.id.evchargingstation);
        checkBoxHomeStation= findViewById(R.id.homestation);
        checkBoxShareableBattery= findViewById(R.id.sharing);
        button = findViewById(R.id.store);


        firebaseFirestore = FirebaseFirestore.getInstance();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();

            }
        });






    }

    private void saveDetails(){

        com.triplethree.models.Location  location  =
                new com.triplethree.models.Location(Double.parseDouble( textInputEditTextLatitude.getText().toString()),Double.parseDouble(textInputEditTextLongtitude.getText().toString()));
        BasicInfoOfEvCharger basicInfoOfEvCharger = new BasicInfoOfEvCharger(location, textInputEditTextStationName.getText().toString(), Float.parseFloat(textInputEditTextPrice.getText().toString())
                ,checkBoxAvilability.isChecked());
        EvStation evStation = new EvStation(basicInfoOfEvCharger);
        EvCharger evCharger = new EvCharger(evStation);


        firebaseFirestore.collection("EvChargerTest").document("EvStation").set(evCharger).addOnSuccessListener(
                new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: success");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });

    }






}
