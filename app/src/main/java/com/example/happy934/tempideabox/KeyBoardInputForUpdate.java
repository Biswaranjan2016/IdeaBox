package com.example.happy934.tempideabox;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.happy934.tempideabox.TestPackage.ResultViewAdapter;
import com.example.happy934.tempideabox.database.room.Textual;
import com.example.happy934.tempideabox.database.room.db.IdeaBoxDataBase;

public class KeyBoardInputForUpdate extends AppCompatActivity {

    EditText title;
    EditText description;
    EditText tags;

    String stringTitle = null;
    String stringDescription = null;

    int index;
    IdeaBoxDataBase ideaBoxDataBase;
    Textual textual;
    Button saveMyIdea;

    LinearLayout imageLayout;
    LinearLayout speechLayout;

    private static final String TAG = "KeyBoardInput";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board_input);


        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);
        tags = (EditText)findViewById(R.id.textView4_tags);
        saveMyIdea = (Button)findViewById(R.id.button);
//        saveMyIdea.setVisibility(android.view.View.INVISIBLE);
        saveMyIdea.setText("Update my idea");
        saveMyIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Update().execute();
            }
        });
        imageLayout = (LinearLayout) findViewById(R.id.imageLayout);
        imageLayout.setVisibility(View.GONE);
        speechLayout = (LinearLayout)findViewById(R.id.speechLayout);
        speechLayout.setVisibility(View.GONE);
    }
    protected void onResume(){
        super.onResume();
//        title.setFocusable(false);
//        description.setFocusable(false);
        tags.setFocusable(false);
        stringTitle = title.getText().toString();
        stringDescription = description.getText().toString();

        new Assign().execute();
    }
//    public void update(){
//        new Update().execute();
//    }
    class Update extends AsyncTask<Void, Void, Void>
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
            textual.setTitle(stringTitle);
            textual.setText_desc(stringDescription);
            ideaBoxDataBase.textualDao().updateRec(textual.getTitle(),textual.getText_desc(),textual.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //To after addition operation here.
        }

    }
    class Assign extends AsyncTask<Void, Void, Void>
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
