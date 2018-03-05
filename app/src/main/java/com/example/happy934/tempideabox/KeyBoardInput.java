package com.example.happy934.tempideabox;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.happy934.tempideabox.camera.Cam;
import com.example.happy934.tempideabox.camera.ImageAdapter;
import com.example.happy934.tempideabox.camera.ImageSelectorAdapter;
import com.example.happy934.tempideabox.database.IdeaBoxContract;
import com.example.happy934.tempideabox.database.IdeaBoxDBHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class Listeners implements View.OnClickListener{
    public void onClick(View view){

    }
}

public class KeyBoardInput extends AppCompatActivity {

    private String title;
    private String description;
    private String tag;
    private String time;
    private long timeInMilliSeconds;

    private EditText editTextTitle;
    private EditText editTextDescription;

    private ImageButton camera;
    private ImageButton audio;

    private Spinner spinner;
    IdeaBoxDBHelper ideaBoxDBHelper;
    SQLiteDatabase sqLiteDatabase;

    RecyclerView recyclerView;
    List<File> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board_input);

        Intent intent = getIntent();
        String description = intent.getStringExtra("description");

        /*
        * Edit Text
        * */
        editTextTitle = (EditText)findViewById(R.id.title);
        editTextDescription = (EditText)findViewById(R.id.description);

        camera = (ImageButton)findViewById(R.id.camera);
        audio = (ImageButton)findViewById(R.id.audio);
        camera.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Cam.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_keyBoardInput_images);
        files = ImageSelectorAdapter.files;

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setAdapter(new ImageAdapter(files));

        editTextDescription.setText(description);
        //Instantiate the database
        ideaBoxDBHelper = new IdeaBoxDBHelper(this);

        //Gets the data repository in write mode
        sqLiteDatabase = ideaBoxDBHelper.getWritableDatabase();

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
                Toast.makeText(getApplicationContext(),"Please specify the level of importance of your idea",Toast.LENGTH_SHORT)
                        .show();
                getValues(view);
            }
        }else {
            //Create a new map of values where column name are keys
            ContentValues contentValues = new ContentValues();
            contentValues.put(IdeaBoxContract.IdeaBoxDB.TITLE,title);
            contentValues.put(IdeaBoxContract.IdeaBoxDB.DESCRIPTION,description);
            contentValues.put(IdeaBoxContract.IdeaBoxDB.TAGS,tag);
            contentValues.put(IdeaBoxContract.IdeaBoxDB.TIMESTAMP,timeInMilliSeconds);

            //Insert data to the database
            long newRowId = sqLiteDatabase.insert(IdeaBoxContract.IdeaBoxDB.TABLENAME,null,contentValues);

            backToMainActivity();
        }
    }
    private  void backToMainActivity(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    private void getValues(View view){



        //Get the content of TITLE from layout
        title =editTextTitle.getText().toString();

        //Get the DESCRIPTION from the Layout
        description =editTextDescription.getText().toString();

        //Get the TAG from the layout
        tag = spinner.getSelectedItem().toString();

        //Put the timestamp
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        time = simpleDateFormat.format(calendar.getTime());
        try {
            Date date = simpleDateFormat.parse(time);
            timeInMilliSeconds = date.getTime();
        }catch (Exception e){

        }

        Log.d("time in Input",Long.toString(timeInMilliSeconds));
    }

}
