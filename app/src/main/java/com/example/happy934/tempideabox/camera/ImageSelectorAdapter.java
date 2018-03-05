package com.example.happy934.tempideabox.camera;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.happy934.tempideabox.R;

import java.io.File;
import java.util.List;

/**
 * Created by happy on 28/2/18.
 */

public class ImageSelectorAdapter extends RecyclerView.Adapter<ImageSelectorAdapter.ImageSelectorHolder>{

    public static List<File> files;
    static File file;
    static int index;

    class ImageSelectorHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        ImageView imageViewLocal;

        public ImageSelectorHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setOnClickListener(this);

        }

        public void onClick(View view){
            imageViewLocal = (ImageView) view.findViewById(R.id.imageButton_ConfirmationPage);
            assert imageViewLocal != null;
            index = this.getAdapterPosition();
            file = files.get(index);
            ImageSelector.setImage();

            Log.e("xxxxxxxxxxxx","123456789");
            Log.e("xxxxxxxxxxxxx", Integer.toString(this.getAdapterPosition()));
        }
    }

    public ImageSelectorAdapter(List<File> files){
        this.files = files;
    }

    @Override
    public ImageSelectorHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view,parent,
                false);

        return new ImageSelectorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageSelectorHolder imageSelectorHolder, int position){
        File file = files.get(position);
        imageSelectorHolder.imageView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
    }

    public int getItemCount(){
        return files.size();
    }
}
