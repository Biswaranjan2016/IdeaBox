package com.example.happy934.tempideabox.speech;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.happy934.tempideabox.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by happy on 6/3/18.
 */

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioHolder>{

    List<String> paths;
    String path = null;
    MediaPlayer mediaPlayer = null;
    final String TAG = "Recycler view Audio";

    class AudioHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageButton imageButton;
        boolean startPlaying = true;

        public AudioHolder(View view){
            super(view);
            imageButton = (ImageButton) view.findViewById(R.id.imageButton_audio);
            imageButton.setOnClickListener(this);
        }

        public void onClick(View view){

            int index = this.getAdapterPosition();
            path = paths.get(index);

            if (startPlaying){
                startPlaying = false;
                this.imageButton.setBackgroundResource(R.drawable.ic_stop_black_24dp);
                startPlaying(path);
            }else {
                this.imageButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                startPlaying = true;
                stopPlaying();
            }
        }

        private void startPlaying(String path){
            mediaPlayer = new MediaPlayer();
            try{
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }catch (Exception e){
                Log.e(TAG,"Exception");
            }
        }
        private void stopPlaying(){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public AudioAdapter(List<String> paths){
        this.paths = paths;
    }

    public AudioHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_button,parent,false);
        return new AudioHolder(itemView);
    }

    public void onBindViewHolder(AudioHolder audioHolder, int position){
        String path = paths.get(position);
    }

    public int getItemCount(){
        return paths.size();
    }
}