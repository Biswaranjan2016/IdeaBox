package com.example.happy934.tempideabox;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy934.tempideabox.camera.Cam;
import com.example.happy934.tempideabox.camera.ImageAdapter;
import com.example.happy934.tempideabox.camera.ImageSelector;
import com.example.happy934.tempideabox.camera.ImageSelectorAdapter;
import com.example.happy934.tempideabox.database.IdeaBoxContract;
import com.example.happy934.tempideabox.database.IdeaBoxDBHelper;
import com.example.happy934.tempideabox.obj.BlobObj;
import com.example.happy934.tempideabox.obj.DatabaseDataObject;
import com.example.happy934.tempideabox.obj.KeyBoardInputObj;
import com.example.happy934.tempideabox.speech.Mic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class KeyBoardInput extends AppCompatActivity {

    private final String TAG = "KeyBoardInput";

    private String title;
    private String description;
    private String tag;
    private String tags[];
    private String time;
    private long timeInMilliSeconds;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextTags;

    private ImageButton camera;
    private ImageButton audio;

    private Spinner spinner;
    IdeaBoxDBHelper ideaBoxDBHelper;
    SQLiteDatabase sqLiteDatabase;

    RecyclerView recyclerView;
    List<File> files;

    DatabaseDataObject databaseDataObject = null;
    KeyBoardInputObj keyBoardInputObj = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board_input);
        keyBoardInputObj = new KeyBoardInputObj();
        Log.e(TAG,"oncreate()::KeyBoardInput");


        Intent intent = getIntent();
        String description = intent.getStringExtra("description");
        if (ImageSelector.isChecked){
            keyBoardInputObj = (KeyBoardInputObj) intent.getSerializableExtra("InputObj");
            Log.e(TAG,"unchecked");
        }else{
            Log.e(TAG,"Checked");
        }

        //editTexts such as title, description, tagString
        editTextTitle = (EditText)findViewById(R.id.title);
        editTextDescription = (EditText)findViewById(R.id.description);
        editTextTags = (EditText)findViewById(R.id.textView4_tags);

        camera = (ImageButton)findViewById(R.id.camera);
        audio = (ImageButton)findViewById(R.id.audio);

        camera.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Cam.class);
                startActivity(intent);
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mic.class);
                startActivity(intent);
            }
        });



        editTextDescription.setText(description);
        //Instantiate the database
//        ideaBoxDBHelper = new IdeaBoxDBHelper(this);
        databaseDataObject = new DatabaseDataObject();

        //Gets the data repository in write mode
//        sqLiteDatabase = ideaBoxDBHelper.getWritableDatabase();

    }



    public void onResume(){
        super.onResume();
        if (Cam.photoList != null && Cam.photoList.size() > 0){
            int sizeOfListInstance = Cam.photoList.size();
            Log.e("KeyBoardInput","non null instance");
            Log.e(TAG,Integer.toString(sizeOfListInstance));

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_keyBoardInput_images);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new ImageAdapter(Cam.photoList));

        }else {
            Log.e("KeyBoardInput","null list instance");
        }

    }

    public void onInput(View view){
        //Get the values from the fields in the layout to insert into the database
        getValues(view);

        if (title.equals("")||tag.equals("")){
            if (title.equals("")){
                Toast.makeText(getApplicationContext(),"Please enter a title for your idea",Toast.LENGTH_SHORT)
                        .show();
                getValues(view);
            }
            else if (tag.equals("")){
                Toast.makeText(getApplicationContext(),"Please tag your idea",Toast.LENGTH_SHORT)
                        .show();
                getValues(view);
            }
        }else {
            startDatabaseTransaction();
        }
    }
    private void getValues(View view){

        if (keyBoardInputObj != null){
            //prepareObject();
            Log.e(TAG,"non Null keyboardInputObj ref");
        }else {
            Log.e(TAG,"Null keyboardInputObj ref");
        }

        //Get the content of TITLE from layout
        title =editTextTitle.getText().toString();

        //Get the DESCRIPTION from the Layout
        description =editTextDescription.getText().toString();

        //Get the TAG from the layout
        tag = editTextTags.getText().toString();
        Pattern regularExpressionPattern = Pattern.compile("\\s*[;]\\s|\\s*[;]|\\s");
        tags = regularExpressionPattern.split(tag);
        Log.e(TAG,Integer.toString(tags.length));

        if (tags.length > 0){
            databaseDataObject.setTags(tags);
        }else{
            Log.e(TAG,"Array length 0 (192)");
        }

        //Put the timestamp
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        time = simpleDateFormat.format(new Date());
        try {
            Date date = simpleDateFormat.parse(time);
            timeInMilliSeconds = date.getTime();
        }catch (Exception e){

        }

        Log.d("time in Input",Long.toString(timeInMilliSeconds));
    }

    private void startDatabaseTransaction(){
        Log.e(TAG,title);
        Log.e(TAG,description);
        Log.e(TAG,time);
        if (tags.length > 0){
            for (String tag : tags){
                Log.e(TAG,tag);
            }
        }else{
            Log.e(TAG,"Array length 0 (218)");
        }
        prepareDatabaseDataObject();
    }


    private void prepareDatabaseDataObject(){

        BlobObj audioBlobObj = new BlobObj();
        BlobObj pictureBlobObj = new BlobObj();

        if (Cam.photoList != null && Mic.paths != null){
            if (Cam.photoList.size() > 0){
                prepareAudioBlob(pictureBlobObj);
                Log.e(TAG, Integer.toString(databaseDataObject.getPictureBlobs().size())+": Picture Blob size");
            }else {
                Log.e(TAG,"PhotoList is empty");
            }
            if (Mic.paths.size() > 0){
                preparePictureBlob(audioBlobObj);
                Log.e(TAG, Integer.toString(databaseDataObject.getAudioBlobs().size())+": Audio Blob size");
            }else {
                Log.e(TAG,"audioList is empty");
            }
        }

    }

    private void prepareAudioBlob(BlobObj audioBlobObj){
        for (File file : Cam.photoList){

            byte[] fileContent = new byte[(int)file.length()];
            FileInputStream fileInputStream = null;

            try{
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(fileContent);
            }catch (IOException ioe){
                Log.e(TAG,ioe.getMessage());
            }

            try{
                audioBlobObj.setBytes(0,fileContent);
            }catch (SQLException se){
                Log.e(TAG,"Sql Exception");
            }

            databaseDataObject.setPictureBlob(audioBlobObj);
        }
    }

    private void preparePictureBlob(BlobObj pictureBlobObj){
        for (String file : Mic.paths){

            File audioFileForDBObject;
            byte[] audioByteForDBObject = null;

            try{

                audioFileForDBObject = new File(file);
                audioByteForDBObject = new byte[(int) audioFileForDBObject.length()];

                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(audioByteForDBObject);

                pictureBlobObj.setBytes(0,audioByteForDBObject);

            }catch (IOException ioe){
                Log.e(TAG,ioe.getMessage());
            }catch (SQLException sqe){
                Log.e(TAG,sqe.getMessage());
            }
            databaseDataObject.setPictureBlob(pictureBlobObj);
        }
    }

}
