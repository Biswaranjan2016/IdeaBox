package com.example.happy934.tempideabox.camera;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.happy934.tempideabox.R;

import java.io.File;
import java.util.List;

/**
 * Created by happy on 27/2/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder>{

    private List<File> files;
    private final String TAG = "Recycler View";
    Cam cam;

    class ImageHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;


        public ImageHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cam cam = new Cam();
                }
            });
        }

    }


    public ImageAdapter(List<File> files){
        this.files = files;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view,
                parent,false);

        return new ImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageHolder imageHolder, int position){
        File file = files.get(position);
        imageHolder.imageView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
    }

    public int getItemCount(){
        return files.size();
    }
}
