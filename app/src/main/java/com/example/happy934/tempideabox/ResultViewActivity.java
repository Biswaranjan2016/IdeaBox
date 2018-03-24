package com.example.happy934.tempideabox;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy934.tempideabox.TestPackage.ResultViewAdapter;
import com.example.happy934.tempideabox.database.IdeaBoxContract;
import com.example.happy934.tempideabox.database.IdeaBoxDBHelper;
import com.example.happy934.tempideabox.database.room.Audio;
import com.example.happy934.tempideabox.database.room.Graphical;
import com.example.happy934.tempideabox.database.room.Tags;
import com.example.happy934.tempideabox.database.room.Textual;
import com.example.happy934.tempideabox.database.room.TextualTag;
import com.example.happy934.tempideabox.database.room.db.IdeaBoxDataBase;
import com.example.happy934.tempideabox.resultInCardView.RecyclerViewResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

//This class is for Calculating the result and preparing the dataset

public class ResultViewActivity extends AppCompatActivity implements AsyncResponse{

    SQLiteDatabase db;
    List itemTitle;
    List itemDescription;
    List itemTag;
    TextView resultView;

    TextView title;
    TextView description;
    CardView cardView;
    ImageButton imageButton;

    long toDate, fromDate;

    public static String dataSet[][];
    RecyclerView recyclerViewResultViewCard;
    IdeaBoxDataBase ideaBoxDataBase;
    List<Textual> textualList;

    private static final String TAG = "ResultViewActivity";
    public AsyncResponse delegate = null;
    private int currentIdeaId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);


        //Receive the intent from Trip to my idea and extract the encoded information
        Intent intent = getIntent();
        String toDateString = intent.getStringExtra("toDate");
        toDate = Long.parseLong(toDateString);
        String fromDateString = intent.getStringExtra("fromDate");
        fromDate = Long.parseLong(fromDateString);


        //Register the image button for the context menu
//        registerForContextMenu(imageButton);
        Log.w(TAG,"83, cntxt menu");

        //Set a onClick listener for the image button
//        imageButton.setOnClickListener(new ImageButton.OnClickListener(){
//            public void onClick(View view){
//                imageButton.showContextMenu();
//            }
//        });

        ideaBoxDataBase = Room.databaseBuilder(getApplicationContext(),IdeaBoxDataBase.class,"db").fallbackToDestructiveMigration().build();
        recyclerViewResultViewCard = (RecyclerView)findViewById(R.id.recyclerView_resultView_card);

//        getData();


    }
    public boolean onContextItemSelected(MenuItem item) {
        int clickedItemPosition = item.getOrder();
        Log.e(TAG,Integer.toString(clickedItemPosition)+" :99");
        Log.e(TAG,Integer.toString(ResultViewAdapter.index)+" :99");
        switch (clickedItemPosition){
            case 0: view(ResultViewAdapter.index);
                    Intent intent = new Intent(getApplicationContext(),KeyBoardInput2.class);
                    startActivity(intent);
                    break;
            case 1: update(ResultViewAdapter.index);
                    Intent intentForUpdate = new Intent(getApplicationContext(),KeyBoardInputForUpdate.class);
                    startActivity(intentForUpdate);
                    break;
            case 2: del(ResultViewAdapter.index);
                    break;
        }
        return super.onContextItemSelected(item);
    }
    private void del(int ideaId){
        currentIdeaId = ideaId;
        new Deletion().execute();
    }

    private void view(int ideaId){

    }

    private void update(int ideaId){

    }

    public void onResume(){
        super.onResume();
        prepareAndStartDatabaseTransaction();
//        Log.e(TAG,Integer.toString(textualList.size())+"Size of List");

    }

    public void processFinish(){
        recyclerViewResultViewCard.setAdapter(new ResultViewAdapter(textualList));
        recyclerViewResultViewCard.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewResultViewCard.setItemAnimator(new DefaultItemAnimator());
    }

    private void prepareAndStartDatabaseTransaction(){
        Log.e(TAG,"PreparingForDBTransaction");
        new DatabaseSync().execute();
    }

    //METHOD
    //This method prepares the Queries the database and gets the data in a cursor
    private void getData(IdeaBoxDataBase ideaBoxDataBase){
        List<Textual> textuals = ideaBoxDataBase.textualDao().fetchAllData();
    }

    private void prepareRecyclerView(){

        if (true){
            recyclerViewResultViewCard.setAdapter(new ResultViewAdapter(textualList));
            recyclerViewResultViewCard.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerViewResultViewCard.setItemAnimator(new DefaultItemAnimator());
        }else {
            Log.e(TAG,"Size : "+Integer.toString(textualList.size()));
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

            //Now access all the methods defined in DaoAccess with sampleDatabase object
            ideaBoxDataBase.textualDao().fetchAllData();
            textualList = ideaBoxDataBase.textualDao().fetchAllData();
            Log.e(TAG,"doingBackgroundTask");
            Log.e(TAG,Integer.toString(textualList.size()));

            List<Tags> tags = ideaBoxDataBase.tagsDao().getAll();
            List<Graphical> graphicalList = ideaBoxDataBase.graphicalDao().getAll();
            List<TextualTag> textualTags = ideaBoxDataBase.textualTagDao().getAll();
            List<Audio> audioList = ideaBoxDataBase.audioDao().getAll();

            Log.e(TAG,"157 Number of entries in junc tab ...");
            Log.d(TAG,Integer.toString(textualTags.size()));

//            Log.e(TAG,"Printing num of pics...");
//            Log.e(TAG,Integer.toString(graphicalList.size()));
//            Log.e(TAG,"Printing num of audios...");
//            Log.e(TAG,Integer.toString(audioList.size()));

            for (Graphical graphical : graphicalList){
                Log.e(TAG,"\nPrinting the idea id in pics");
                Log.e(TAG,Integer.toString(graphical.getIdeaId()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prepareRecyclerView();
            //To after addition operation here.
        }

        boolean onContextItemSelected(MenuItem menuItem){
            return true;
        }
    }
    class Deletion extends AsyncTask<Void, Void, Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Perform pre-adding operation here.
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ideaBoxDataBase.textualDao().deleteRecord(ideaBoxDataBase.textualDao().getSingleRecord(currentIdeaId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //To after addition operation here.
        }

        boolean onContextItemSelected(MenuItem menuItem){
            return true;
        }
    }

}
