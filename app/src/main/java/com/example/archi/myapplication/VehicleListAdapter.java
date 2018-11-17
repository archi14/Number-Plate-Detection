package com.example.archi.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.VehicleViewHolder> {
    @NonNull
   private ArrayList<Vehicle> arrayList;

    public VehicleListAdapter(@NonNull ArrayList<Vehicle> arrayList) {
        this.arrayList = arrayList;
    }

    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle_list,parent,false);
        return new VehicleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = arrayList.get(position);
        holder.VehicleNum.setText(vehicle.getVehicleNum());
        holder.Owner.setText(vehicle.getOwner());
        holder.Owner.setText(vehicle.getVehicle());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView VehicleNum,Owner,Vehicle;

        public VehicleViewHolder(View itemView) {
            super(itemView);
            VehicleNum = itemView.findViewById(R.id.VehicleNum);
            Owner = itemView.findViewById(R.id.Owner);
            Vehicle = itemView.findViewById(R.id.Vehicle);
        }
    }
}
