package com.example.archi.myapplication;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OcrManager {

    TessBaseAPI baseAPI = null;
    public void initAPI()
    {
        baseAPI = new TessBaseAPI();
        // after copy, my path to trainned data is getExternalFilesDir(null)+"/tessdata/"+"vie.traineddata";
        // but init() function just need parent folder path of "tessdata", so it is getExternalFilesDir(null)
        MainApplication mainApplication = new MainApplication();
        String dataPath = MainApplication.instance.getTessDataParentDirectory();
        baseAPI.init(dataPath,"eng");
        // language code is name of trainned data file, except extendsion part
        // "vie.traineddata" => language code is "vie"

        // first param is datapath which is  part to the your trainned data, second is language code
        // now, your trainned data stored in assets folder, we need to copy it to another external storage folder.
        // It is better do this work when application start firt time
    }

    public String startRecognize(Bitmap bitmap)
    {
        if(baseAPI ==null)
            initAPI();
        baseAPI.setImage(bitmap);
        return baseAPI.getUTF8Text();
    }

}
