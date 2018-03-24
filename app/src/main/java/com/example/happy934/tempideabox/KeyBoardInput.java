package com.example.happy934.tempideabox;

import android.arch.persistence.room.Room;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.AsyncTask;
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
import com.example.happy934.tempideabox.database.room.Audio;
import com.example.happy934.tempideabox.database.room.Graphical;
import com.example.happy934.tempideabox.database.room.Tags;
import com.example.happy934.tempideabox.database.room.Textual;
import com.example.happy934.tempideabox.database.room.TextualTag;
import com.example.happy934.tempideabox.database.room.db.IdeaBoxDataBase;
import com.example.happy934.tempideabox.obj.DatabaseDataObject;
import com.example.happy934.tempideabox.obj.KeyBoardInputObj;
import com.example.happy934.tempideabox.speech.AudioAdapter;
import com.example.happy934.tempideabox.speech.Mic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
    private IdeaBoxDataBase ideaBoxDataBase;

    RecyclerView recyclerView;
    RecyclerView audioRecyclerView;
    List<File> files;

    DatabaseDataObject databaseDataObject = null;
    KeyBoardInputObj keyBoardInputObj = null;

    List<Graphical> pictureList;
    List<Audio> audioList;

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

        pictureList = new ArrayList<>();
        audioList = new ArrayList<>();

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
        if (Mic.paths != null && Mic.paths.size() > 0){
            int sizeOfListInstance = Mic.paths.size();
            Log.e("KeyBoardInput","non null instance");
            Log.e(TAG,Integer.toString(sizeOfListInstance));

            audioRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_keyBoardInput_audio);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            audioRecyclerView.setItemAnimator(new DefaultItemAnimator());
            audioRecyclerView.setLayoutManager(linearLayoutManager);
            audioRecyclerView.setAdapter(new AudioAdapter(Mic.paths));
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
            ideaBoxDataBase = Room.databaseBuilder(getApplicationContext(),IdeaBoxDataBase.class,"db").fallbackToDestructiveMigration().build();
            new DatabaseSync().execute();
        }

    }

    class DatabaseSync extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Perform pre-adding operation here.
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Let's add some dummy data to the database.
            Textual textual = new Textual();
            textual.setTitle(title);
            textual.setText_desc(description);
            textual.setDate(time);
            textual.setTitle(title);


            //Now access all the methods defined in DaoAccess with sampleDatabase object
            ideaBoxDataBase.textualDao().insertOnlySingleRecord(textual);
            List<Textual> textualsLocal = ideaBoxDataBase.textualDao().fetchAllData();
            Textual textualLocal = textualsLocal.get(textualsLocal.size()-1);
            int id = textualLocal.getId();

            //Here Queries related to Graphical will go.
            preparePictureBlob(id);
            if (pictureList.size() > 0){
                Log.e(TAG,"213: Picture list size"+Integer.toString(pictureList.size()));
                for (Graphical graphical : pictureList){
                    ideaBoxDataBase.graphicalDao().insert(graphical);
                }
            }

            //Here Queries related to Audio go
            prepareAudioBlob(id);
            if (audioList.size() > 0){
                for (Audio audio : audioList){
                    ideaBoxDataBase.audioDao().insert(audio);
                }
            }

            //Here Queries related to tags will go
            Tags tag;
            if (tags.length > 0){
                List<Tags> tagsRes = ideaBoxDataBase.tagsDao().getAll();
                for (String tagString : tags){

                    if (!isAvailable(tagString,tagsRes)){
                        ideaBoxDataBase.tagsDao().insert(new Tags(tagString));
                        List<Tags> tags1 = ideaBoxDataBase.tagsDao().getAll();
                        int tagId = tags1.get(tags1.size() - 1).getId();
                        ideaBoxDataBase.textualTagDao().insert(new TextualTag(id, tagId));
                    }else{
                        Log.e(TAG,"Available! Enter another");
                        int tagId = searchTag(tagString,tagsRes);
                        if (tagId > 0){
                            Log.e(TAG,"Available! Enter another");
                            Log.e(TAG,"idea id"+Integer.toString(id));
                            Log.e(TAG,"tag id"+Integer.toString(tagId));

                            TextualTag textualTag = new TextualTag(id, tagId);

                            Log.e(TAG,"idea id"+Integer.toString(textualTag.getIdeaId()));
                            Log.e(TAG,"tag id"+Integer.toString(textualTag.getTagId()));

                            ideaBoxDataBase.textualTagDao().insert(textualTag);
                        }
                    }

                }
            }

            //Here Queries related to textualTag will go


            return null;
        }

        protected boolean isAvailable(String textString,List<Tags> tagRes){

            for (Tags tags : tagRes){
                if (textString.equals(tags.getTag())){
                    return true;
                }
            }
            return false;
        }

        protected int searchTag(String textString,List<Tags> tagRes){
            for (Tags tags : tagRes){
                if (textString.equals(tags.getTag())){
                    return tags.getId();
                }
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Record inserted successfully",Toast.LENGTH_SHORT).show();
            //To after addition operation here.
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

    }



    private void preparePictureBlob(int ideaId){
        Log.e(TAG,"Inside PrepaprePictureBlob");
        pictureList.clear();
        if (Cam.photoList != null && Cam.photoList.size() > 0){
            Log.e(TAG,Integer.toString(Cam.photoList.size())+" :321");
            for (File file : Cam.photoList){

                byte[] fileContent = new byte[(int)file.length()];
                FileInputStream fileInputStream = null;

                try{
                    fileInputStream = new FileInputStream(file);
                    fileInputStream.read(fileContent);
                }catch (IOException ioe){
                    Log.e(TAG,ioe.getMessage());
                }
                pictureList.add(new Graphical(fileContent,ideaId));
            }
        }
    }

    private void prepareAudioBlob(int ideaId){
        Log.e(TAG,"Inside PrepareAudioBlob");
        audioList.clear();
        if (Mic.paths != null && Mic.paths.size() > 0){
            Log.e(TAG,Integer.toString(Mic.paths.size())+" :342");
            for (String file : Mic.paths){

                File audioFileForDBObject;
                byte[] audioByteForDBObject = null;

                try{

                    audioFileForDBObject = new File(file);
                    audioByteForDBObject = new byte[(int) audioFileForDBObject.length()];

                    FileInputStream fileInputStream = new FileInputStream(file);
                    fileInputStream.read(audioByteForDBObject);

                    audioList.add(new Audio(audioByteForDBObject,ideaId));

                }catch (IOException ioe) {
                    Log.e(TAG, ioe.getMessage());
                }
            }

        }else {
            Log.e(TAG,"350 null ref");
        }
    }


}
