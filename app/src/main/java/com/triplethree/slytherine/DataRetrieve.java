package com.triplethree.slytherine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.triplethree.models.EvCharger;
import com.triplethree.models.EvStation;
import com.triplethree.utils.EvChargersInfo;

import java.util.ArrayList;

public class DataRetrieve {
    private static final String TAG = "DataRetrieve";

    private Context context;

    private FirebaseFirestore firebaseFirestore;

    private ArrayList<EvCharger> evChargers;
    private boolean dataRetrieved= false;


    public DataRetrieve(Context context) {
        this.context = context;

        FirebaseApp.initializeApp(context);
        this.firebaseFirestore = FirebaseFirestore.getInstance();

        this.evChargers = new ArrayList<EvCharger>();
        loadData();


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
                            dataRetrieved = true;

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    private void addEvCharger(EvCharger evCharger){
        evChargers.add(evCharger);
        Log.d(TAG, "addEvCharger: outside size" +evChargers.size());
    }

    public ArrayList<EvCharger> getEvChargers() {
        return evChargers;
    }

    public boolean isDataRetrieved() {
        return dataRetrieved;
    }
}
