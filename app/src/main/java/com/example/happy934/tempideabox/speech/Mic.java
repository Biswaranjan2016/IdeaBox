package com.example.happy934.tempideabox.speech;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.happy934.tempideabox.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mic extends AppCompatActivity {

    private static final String TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private static String fileName = null;
    private String timeStamp = null;
    List<String> paths = null;

    private ImageButton recordButton = null;
    private MediaRecorder mediaRecorder = null;

    private ImageButton playButton = null;
    private MediaPlayer mediaPlayer = null;

    //Requesting Permission to record audio
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    //RecyclerView Instantiation
    RecyclerView recyclerView = null;
    AudioAdapter audioAdapter = null;
    View viewAudio1 = null;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted){
            finish();
        }
    }

    private void onRecord(boolean start){
        if (start){
            startRecording();
        }else {
            stopRecording();
        }
    }

    private void onPlay(boolean start){
        if (start){
            startPlaying();
        }else {
            stopPlaying();
        }
    }

    private void startPlaying(){
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (IOException ioe){
            Log.e(TAG,"Prepare() failed");
        }
    }

    private void stopPlaying(){
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(createFile());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mediaRecorder.prepare();
        }catch (IOException ioe){
            Log.e(TAG,"prepare() failed");
        }

        mediaRecorder.start();
        paths.add(fileName);


        Log.e(TAG, Integer.toString(paths.size())+" entry(ies)");
    }

    private void stopRecording(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private String createFile(){
        timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss").format(new Date());
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += timeStamp;
        fileName += "audioRecordTest.3gp";
        Log.e(TAG,fileName);
        return fileName;
    }

    class RecordListener implements View.OnClickListener{
        boolean startRecording = true;

        @Override
        public void onClick(View view) {
            onRecord(startRecording);
            if (startRecording){
                recordButton.setImageResource(R.drawable.ic_stop_black_24dp);
                recordButton.setBackgroundResource(R.drawable.mic_background);
                viewAudio1.setBackgroundColor(getResources().getColor(R.color.greenSecondary));
                startRecording = false;
            }else {
                recordButton.setImageResource(R.drawable.ic_mic_black_24dp);
                recordButton.setBackgroundResource(R.drawable.mic_background_2);
                viewAudio1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                startRecording = true;
                audioAdapter.notifyDataSetChanged();
            }
        }
    }

    class PlayListener implements View.OnClickListener{

        boolean startPlaying = true;

        @Override
        public void onClick(View view){
            onPlay(startPlaying);
            if (startPlaying){
                startPlaying = false;
            }else {
                startPlaying = true;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mic);
        paths = new ArrayList<>();

        recordButton = (ImageButton)findViewById(R.id.mic);
        recordButton.setOnClickListener(new RecordListener());

        playButton = (ImageButton) findViewById(R.id.imageButton_check_confirmation);
        playButton.setOnClickListener(new PlayListener());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_audio_outputs);
        viewAudio1 = findViewById(R.id.view_mic_1);

        if (paths != null){
            audioAdapter = new AudioAdapter(paths);
            recyclerView.setAdapter(audioAdapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(layoutManager);
        }


        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaRecorder != null){
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
