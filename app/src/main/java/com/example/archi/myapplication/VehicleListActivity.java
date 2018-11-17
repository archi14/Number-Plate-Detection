package com.example.archi.myapplication;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VehicleListActivity extends AppCompatActivity {
    public ArrayList<Vehicle> arrayList;
    RecyclerView VehicleListView;
    VehicleListAdapter vehicleListAdapter;
    DatabaseReference mref;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);
        VehicleListView = findViewById(R.id.VehicleListView);
        arrayList = new ArrayList<>();
        mref = FirebaseDatabase.getInstance().getReference().child("VehicleInfo");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Vehicle vehicle = dataSnapshot1.getValue(Vehicle.class);
                    //Log.d("userdetails", user.getEmail());
                    arrayList.add(vehicle);
                }
                vehicleListAdapter = new VehicleListAdapter(arrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                VehicleListView.setLayoutManager(mLayoutManager);
                VehicleListView.setItemAnimator(new DefaultItemAnimator());
                VehicleListView.setHasFixedSize(true);
                VehicleListView.setAdapter(vehicleListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*//arrayList =(ArrayList<Vehicle>) getIntent().getSerializableExtra("mylist");
        vehicleListAdapter = new VehicleListAdapter(arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        VehicleListView.setLayoutManager(layoutManager);
        VehicleListView.setAdapter(vehicleListAdapter);*/

    }
}
