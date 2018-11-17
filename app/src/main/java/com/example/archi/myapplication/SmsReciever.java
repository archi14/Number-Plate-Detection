package com.example.archi.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.sql.Array;
import java.util.Arrays;


public class SmsReciever extends BroadcastReceiver {
    private static SmsListener mListener;
    public void onReceive(Context context, Intent intent) {

        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");
        String body="";
        for(int i=0;i<pdus.length;i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();

                Log.d("Inside", "onReceive: ");
                String messageBody = smsMessage.getMessageBody();

                body+=messageBody+" ";
            //Toast.makeText(context, messageBody, Toast.LENGTH_SHORT).show();

            }

        Log.d("message", body);
        //String title = body.substringBetween();
        String [] array = body.split(":?\\n");
        //String [] arr =
        /*Log.d("array", array[1]);
        Log.d("array1", array[2]);
        Log.d("array2", array[3]);
        Log.d("array3", array[0]);*/

        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(array[1]);
        vehicle.setVehicleNum(array[0]);
        vehicle.setVehicle(array[2]);

        mListener.messageReceived(vehicle);
        }
    public interface SmsListener {
        public void messageReceived(Vehicle vehicle);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
