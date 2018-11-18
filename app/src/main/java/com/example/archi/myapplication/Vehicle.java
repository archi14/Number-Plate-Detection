package com.example.archi.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Vehicle implements Serializable{
    String vehicleNum;
    String owner;
    String vehicle;
    String type;

    public  Vehicle()
    {

    }
    public Vehicle(String vehicleNum, String owner, String vehicle,String type) {
        this.vehicleNum = vehicleNum;
        this.owner = owner;
        this.vehicle = vehicle;
        this.type =type;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public String getOwner() {
        return owner;
    }

    public String getVehicle() {
        return vehicle;
    }
    public String getType(){return type;}
    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public void setType(String type){this.type = type;}
}
