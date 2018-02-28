package com.example.happy934.tempideabox.camera;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.happy934.tempideabox.R;

import java.io.File;
import java.util.List;

public class ImageSelector extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageSelectorAdapter imageAdapter;
    List<File> photoList;

    static ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);

        imageView = (ImageView) findViewById(R.id.imageView_ConfirmationPage);

        if (Cam.photoList != null){
            this.photoList = Cam.photoList;
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_ConfirmationPage);
            imageAdapter = new ImageSelectorAdapter(photoList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(imageAdapter);
        }
    }
    public static void setImage(){
        Log.d("xxxxxxxxxxx","Inside setImage");
        if(ImageSelectorAdapter.file != null){
            imageView.setImageBitmap(BitmapFactory.decodeFile(ImageSelectorAdapter.file.getPath()));
        }
        else {
            Log.d("xxxxxxxxxxx","Null File instance");
        }
    }
    public void goBack(View view){
        this.onBackPressed();
    }
    public void onDelete(View view){
        if (ImageSelectorAdapter.files.size() > 0){
            if (ImageSelectorAdapter.index == ImageSelectorAdapter.files.size()-1)
                imageView.setImageBitmap(BitmapFactory.decodeFile(
                        ImageSelectorAdapter.files.get(0).getPath()));
            else if (ImageSelectorAdapter.files.size() == 1){
                   imageView.setImageResource(R.drawable.ic_image_black_24dp);
                ImageSelectorAdapter.files.remove(ImageSelectorAdapter.index);
            }
            else
                imageView.setImageBitmap(BitmapFactory.decodeFile(
                        ImageSelectorAdapter.files.get(ImageSelectorAdapter.index+1).getPath()));
            ImageSelectorAdapter.files.remove(ImageSelectorAdapter.index);
            imageAdapter.notifyDataSetChanged();
        }else {
            this.goBack(view);
        }
    }

}
