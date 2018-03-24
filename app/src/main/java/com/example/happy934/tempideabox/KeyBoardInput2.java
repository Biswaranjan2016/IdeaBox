package com.example.happy934.tempideabox;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.happy934.tempideabox.TestPackage.ResultViewAdapter;
import com.example.happy934.tempideabox.database.room.Textual;
import com.example.happy934.tempideabox.database.room.db.IdeaBoxDataBase;

import java.util.List;

public class KeyBoardInput2 extends AppCompatActivity {

    EditText title;
    EditText description;
    EditText tags;
    int index;
    IdeaBoxDataBase ideaBoxDataBase;
    Textual textual;
    Button saveMyIdea;

    private static final String TAG = "KeyBoardInput";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board_input);


        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);
        tags = (EditText)findViewById(R.id.textView4_tags);
        saveMyIdea = (Button)findViewById(R.id.button);
        saveMyIdea.setVisibility(android.view.View.INVISIBLE);

    }
    protected void onResume(){
        super.onResume();
        title.setFocusable(false);
        description.setFocusable(false);
        tags.setFocusable(false);

        new View().execute();

    }
    class View extends AsyncTask<Void, Void, Void>
    {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Perform pre-adding operation here.
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ideaBoxDataBase = Room.databaseBuilder(getApplicationContext(),IdeaBoxDataBase.class, "db").
                    fallbackToDestructiveMigration().build();
            textual = ideaBoxDataBase.textualDao().getSingleRecord(ResultViewAdapter.index+1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startApplyingData();
            //To after addition operation here.
        }

    }

    private void startApplyingData(){
        if (textual != null){
            title.setText(textual.getTitle());
            description.setText(textual.getText_desc());
        }else {
            printLog("textual null");
            printLog(Integer.toString(ResultViewAdapter.index));
        }
    }

    private void printLog(String message){
        Log.e(TAG,message);
    }
}
