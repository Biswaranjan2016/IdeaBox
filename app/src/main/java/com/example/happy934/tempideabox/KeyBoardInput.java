package com.example.happy934.tempideabox;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.happy934.tempideabox.database.IdeaBoxContract;
import com.example.happy934.tempideabox.database.IdeaBoxDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class KeyBoardInput extends AppCompatActivity {

    private String title;
    private String description;
    private String tag;
    private String time;
    private long timeInMilliSeconds;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Spinner spinner;
    IdeaBoxDBHelper ideaBoxDBHelper;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board_input);

        Intent intent = getIntent();
        String description = intent.getStringExtra("description");

        editTextTitle = (EditText)findViewById(R.id.title);
        editTextDescription = (EditText)findViewById(R.id.description);
        spinner = (Spinner)findViewById(R.id.spinner);

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
