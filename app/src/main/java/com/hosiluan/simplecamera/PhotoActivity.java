package com.hosiluan.simplecamera;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import static com.hosiluan.simplecamera.ListPhoToActivity.FILE_NAME;

public class PhotoActivity extends BaseActivity {


    private CustomImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String name = intent.getStringExtra(FILE_NAME);
        File imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        ArrayList<File> files = getListFiles(imageDir);
        for (int i = 0; i < files.size(); i++){
            if (files.get(i).getName().equals(name)){
                imageView.loadImage(files.get(i));
            }
        }
    }

    private void setView(){
        imageView  = findViewById(R.id.img_image);
    }


}
