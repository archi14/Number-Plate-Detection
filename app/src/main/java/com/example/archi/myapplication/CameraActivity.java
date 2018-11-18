package com.example.archi.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.googlecode.tesseract.android.TessBaseAPI;
public class CameraActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE=1;
    private FirebaseAuth firebaseAuth;
    Button Photobtn,Database,Signout,send;
    EditText number;
    ImageView imageView;
    //EditText editNumber;
    TextView tv_OCR_Result;
    DatabaseReference mref;
    SmsManager smsManager;
    Uri OutputfileUri;
    //TextView display;
    Bitmap image;
    ProgressBar progressBar;
    public ArrayList<Vehicle> arrayList;
    private TessBaseAPI mTess;
    public static final String language = "eng";

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
        mref = FirebaseDatabase.getInstance().getReference().child("VehicleInfo");
        smsManager = SmsManager.getDefault();
        tv_OCR_Result = findViewById(R.id.tv_OCR_Result);
        //display = findViewById(R.id.display);
        progressBar = findViewById(R.id.progressBar);

datapath = getFilesDir()+ "/tesseract/";
mTess = new com.googlecode.tesseract.android.TessBaseAPI();
checkFile(new File(datapath+"tessdata/"));
mTess.init(datapath, language);




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text ="VAHAN "+number.getText().toString();
                smsManager.sendTextMessage("7738299899",null,text,null,null);
                //Log.d("Camera", "Sent");
                //Toast.makeText(this,"sent",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);

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
        tv_OCR_Result.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String ocroutput = tv_OCR_Result.getText().toString();
                tv_OCR_Result.setVisibility(View.GONE);
                number = findViewById(R.id.number);
                number.setVisibility(View.VISIBLE);
                number.setText(ocroutput);
                return false;
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
            public void messageReceived(final Vehicle vehicle) {
                //Toast.makeText(CameraActivity.this, messageText, Toast.LENGTH_SHORT).show();
                //arrayList.add(vehicle);
                final String [] Number = vehicle.getVehicleNum().split(" ");
                Log.d("awesomewesome", Number[0]);
                final DatabaseReference vehicleRef = mref.child(Number[0]);
                vehicleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            setUpAlertDialog(1,vehicle,null);
                        }else
                        {


                            setUpAlertDialog(0,vehicle,vehicleRef);
                            //mref.child(Number[0]).setValue(vehicle);
                            //display.setText(vehicle.getOwner());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                progressBar.setVisibility(View.GONE);

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
        number.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String text = number.getText().toString();
                number.setVisibility(View.GONE);
                tv_OCR_Result.setVisibility(View.VISIBLE);
                tv_OCR_Result.setText(text);
                return false;
            }
        });
    }

    private void setUpAlertDialog(int i, final Vehicle vehicle, final DatabaseReference Vehicleref) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (i)
        {
            case 0:
                builder.setMessage("Vehicle number not found in Database, Add to:")
                        .setCancelable(false)
                        .setPositiveButton("Blacklist", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                vehicle.setType("Blacklist");
                                Vehicleref.setValue(vehicle);
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("Whitelist", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        vehicle.setType("Whitelist");
                        Vehicleref.setValue(vehicle);
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case 1:
                builder.setMessage("Vehicle number already found in Database\n" +
                        vehicle.getOwner()+vehicle.getVehicleNum()+vehicle.getVehicle())
                        .setCancelable(false)
                        /*.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        */.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                break;

        }

         //builder.setMessage() .setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        /*builder.setMessage("Vehicle number already found in Database")
                .setCancelable(false)
                *//*.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                *//*.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                });*/
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("AlertDialogExample");
        alert.show();
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
public void runOCR() {
    /*try {
        ExifInterface exif = new ExifInterface(imgUri.getPath());
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int rotate = 0;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        options.inTargetDensity = 300;
        Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(), options);
        if (rotate != 0) {

            // Getting width & height of the given image.
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            // Setting pre rotate
            Matrix mtx = new Matrix();
            mtx.preRotate(rotate);

            // Rotating Bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);

        }
        // bitmap = toGrayscale(bitmap);

        final Bitmap b = bitmap;*/
        String OCRresult = null;
        mTess.setImage(image);
        OCRresult = mTess.getUTF8Text();
        tv_OCR_Result.setText(OCRresult);
    }/*catch (IOException e) {
        e.printStackTrace();
    }*/


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            //OutputfileUri = data.getData();
             image = (Bitmap) extras.get("data");
             //OutputfileUri  = getImageUri(this,image);
            imageView = findViewById(R.id.image);
            imageView.setImageBitmap(image);
            runOCR();
        }
    }

   /* public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }*/
}
