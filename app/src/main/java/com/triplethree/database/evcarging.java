package com.triplethree.database;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.triplethree.slytherine.R;

public class evcarging extends AppCompatActivity {
    TextInputEditText name,address,amount,latitute,longitude,type,id;
    TextView submit;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evcarging);
        databaseHelper=new DatabaseHelper(this);
        name=findViewById(R.id.name);
        amount=findViewById(R.id.amount);
        address=findViewById(R.id.address);
        id=findViewById(R.id.id);
        latitute =findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);
        submit=(TextView)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        boolean result=databaseHelper.insertData(Integer.parseInt(id.getText().toString()),name.getText().toString(),
        address.getText().toString(),Long.parseLong(amount.getText().toString()),type.getText().toString());
            }
        });
    }

}