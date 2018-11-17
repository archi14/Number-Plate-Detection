package com.example.archi.myapplication;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class VehicleListActivity extends AppCompatActivity {
    public ArrayList<Vehicle> arrayList;
    RecyclerView VehicleListView;
    VehicleListAdapter vehicleListAdapter;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);
        VehicleListView = findViewById(R.id.VehicleListView);
        arrayList =(ArrayList<Vehicle>) getIntent().getSerializableExtra("mylist");
        vehicleListAdapter = new VehicleListAdapter(arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        VehicleListView.setLayoutManager(layoutManager);
        VehicleListView.setAdapter(vehicleListAdapter);

    }
}
