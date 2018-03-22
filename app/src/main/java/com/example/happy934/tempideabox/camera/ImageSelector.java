package com.example.happy934.tempideabox.camera;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.happy934.tempideabox.KeyBoardInput;
import com.example.happy934.tempideabox.R;
import com.example.happy934.tempideabox.obj.KeyBoardInputObj;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class ImageSelector extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageSelectorAdapter imageAdapter;
    List<File> photoList;
    static boolean flag = false;
    public static boolean isChecked = false;
    private final String TAG = "ImageSelector";

    static ImageView imageView;
    Serializable serializableKeyBoardInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);

        imageView = (ImageView) findViewById(R.id.imageView_ConfirmationPage);

        Intent intent = new Intent();
        serializableKeyBoardInput = intent.getSerializableExtra("InputObj");
        if (Cam.photoList != null){
//            this.photoList = Cam.photoList;
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_ConfirmationPage);
            imageAdapter = new ImageSelectorAdapter(Cam.photoList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(imageAdapter);
        }
    }
    public void onConfirm(View view){

        flag = true;
        isChecked = true;
        Intent intent = new Intent(getApplicationContext(), KeyBoardInput.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    public static void setImage(){
        if(ImageSelectorAdapter.file != null){
            imageView.setImageBitmap(BitmapFactory.decodeFile(ImageSelectorAdapter.file.getPath()));
        }
    }
    public void goBack(View view){
        this.onBackPressed();
    }
    public void onDelete(View view){
        if (ImageSelectorAdapter.index >= 0){
            if (Cam.photoList.size() > 0){


                if (Cam.photoList.size() == 1){
                    imageView.setImageResource(R.drawable.ic_image_black_24dp);
                }
                else if (ImageSelectorAdapter.index == Cam.photoList.size()-1) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(ImageSelectorAdapter.files.get(0).getPath()));
                }
                else{
                    imageView.setImageBitmap(BitmapFactory.decodeFile(Cam.photoList.get(ImageSelectorAdapter.index+1).getPath()));
                }
                Cam.photoList.remove(ImageSelectorAdapter.index);
                ImageSelectorAdapter.files = Cam.photoList;
                ImageSelectorAdapter.index = -1;
                imageAdapter.notifyDataSetChanged();
            }else if(ImageSelectorAdapter.files.size() == 0){
                Log.e(TAG,"File size : 0");
                imageView.setImageResource(R.drawable.ic_image_black_24dp);
                Cam.photoList = ImageSelectorAdapter.files;
            }
            else {
                this.goBack(view);
            }
        }else {
            return;
        }
    }

}
