package com.example.archi.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import java.io.IOException;
import java.io.FileOutputStream;
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
import java.io.File;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.FileNotFoundException;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.googlecode.tesseract.android.TessBaseAPI;
public class CameraActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE=1;
    private FirebaseAuth firebaseAuth;
    Button Photobtn,Database,Signout,send;
    android.widget.EditText number;
    ImageView imageView;
    SmsManager smsManager;
    TextView display;
    Bitmap image;
    public ArrayList<Vehicle> arrayList;
    private com.googlecode.tesseract.android.TessBaseAPI mTess;
    String datapath = "";
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
        arrayList = new ArrayList<>();
        image = android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
String language = "eng";

datapath = getFilesDir()+ "/tesseract/";
mTess = new com.googlecode.tesseract.android.TessBaseAPI();
checkFile(new File(datapath+"tessdata/"));
mTess.init(datapath, language);
        runOCR();


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
                Intent intent = new Intent(CameraActivity.this,VehicleListActivity.class);
                intent.putExtra("mylist",arrayList);
                startActivity(intent);
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
            public void messageReceived(Vehicle vehicle) {
                //Toast.makeText(CameraActivity.this, messageText, Toast.LENGTH_SHORT).show();
                arrayList.add(vehicle);
                display.setText(vehicle.getOwner());

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

    private void checkFile(File dir) {
    if (!dir.exists()&& dir.mkdirs()){
        copyFiles();
    }
    if(dir.exists()) {
        String datafilepath = datapath+ "/tessdata/eng.traineddata";
        File datafile = new File(datafilepath);
        if (!datafile.exists()) {
            copyFiles();
        }
    }
}

private void copyFiles() {
    try {
        String filepath = datapath + "/tessdata/eng.traineddata";
        AssetManager assetManager = getAssets();
        InputStream instream = assetManager.open("tessdata/eng.traineddata");
        OutputStream outstream = new FileOutputStream(filepath);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = instream.read(buffer)) != -1) {
            outstream.write(buffer, 0, read);
        }
        outstream.flush();
        outstream.close();
        instream.close();
        File file = new File(filepath);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
    } catch (java.io.FileNotFoundException e) {
        e.printStackTrace();
    } catch (java.io.IOException e) {
        e.printStackTrace();
    }
}
public void runOCR(){
    String OCRresult = null;
    mTess.setImage(image);
    OCRresult = mTess.getUTF8Text();
    TextView tv_OCR_Result =  findViewById(R.id.tv_OCR_Result);
    tv_OCR_Result.setText(OCRresult);
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
