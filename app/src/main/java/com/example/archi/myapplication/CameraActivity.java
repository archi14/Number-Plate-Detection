package com.example.archi.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class CameraActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE=1;
    private FirebaseAuth firebaseAuth;
    Button Photobtn,Database,Signout,send;
    android.widget.EditText number;
    ImageView imageView;
    SmsManager smsManager;
    TextView display;
    //SmsVerifyCatcher smsVerifyCatcher;
    //private SmsBroadcastReceiver smsBroadcastReciever;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Photobtn = findViewById(R.id.Photobtn);
        Database = findViewById(R.id.Database);
        Signout = findViewById(R.id.signout);
        firebaseAuth = FirebaseAuth.getInstance();
        number = findViewById(R.id.number);
        send = findViewById(R.id.send);
        smsManager = SmsManager.getDefault();
        display = findViewById(R.id.display);
        /*SmsReciever.bindListener(new SmsReciever.SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("TAG", "messageReceived: ");
                display.setText(messageText);
                //Toast.makeText(this,"recieved",Toast.LENGTH_LONG).show();
            }
        });*/


        /*smsVerifyCatcher = new SmsVerifyCatcher(this,new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                Log.d("listenning", "onSmsCatch: ");
                display.setText(message);
            }
        });*/

        /*smsVerifyCatcher.setPhoneNumberFilter("VM-VAHAAN");
        smsVerifyCatcher.setFilter("VAHAAN");*/
        Photobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text ="VAHAN "+number.getText().toString();
                smsManager.sendTextMessage("7738299899",null,text,null,null);
                Log.d("Camera", "Sent");
                //Toast.makeText(this,"sent",Toast.LENGTH_LONG).show();

            }
        });

        Database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Signout.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           firebaseAuth.signOut();
                                           Log.d("awesome", "onClick: ");
                                           FirebaseUser user = firebaseAuth.getCurrentUser();
                                           Log.d("awesome", "2");

                                           if (user == null) {
                                               // user auth state is changed - user is null
                                               // launch login activity
                                               startActivity(new Intent(CameraActivity.this, LoginActivity.class));
                                               finish();
                                           }
                                       }
                                   });

        String msg;
        SmsReciever.bindListener(new SmsReciever.SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Toast.makeText(CameraActivity.this, messageText, Toast.LENGTH_SHORT).show();
                display.setText(messageText);

            }
        });

        Photobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView = findViewById(R.id.image);
            imageView.setImageBitmap(imageBitmap);
        }
    }

}
