package com.example.happy934.tempideabox;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy934.tempideabox.database.IdeaBoxContract;
import com.example.happy934.tempideabox.database.IdeaBoxDBHelper;
import com.example.happy934.tempideabox.resultInCardView.RecyclerViewResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

//This class is for Calculating the result and preparing the dataset

public class ResultViewActivity extends AppCompatActivity {

    SQLiteDatabase db;
    IdeaBoxDBHelper ideaBoxDBHelper;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);

//        resultView = (TextView) findViewById(R.id.resultview);
        //Instantiate the Database helper class
        ideaBoxDBHelper = new IdeaBoxDBHelper(this);

        //Instantiate the readable database
        db = ideaBoxDBHelper.getReadableDatabase();

        //The following fields are the views from the card layout
        title = (TextView)findViewById(R.id.title);
        description = (TextView)findViewById(R.id.description);
        cardView = (CardView) findViewById(R.id.cardView);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        Intent intent = getIntent();
        String toDateString = intent.getStringExtra("toDate");
        toDate = Long.parseLong(toDateString);
        String fromDateString = intent.getStringExtra("fromDate");
        fromDate = Long.parseLong(fromDateString);

        Log.d("time in ResultView",Long.toString(toDate));
        Log.d("time in ResultView",Long.toString(fromDate));
        //Register the image button for the context menu
        registerForContextMenu(imageButton);

        //Set a onClick listener for the image button
        imageButton.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View view){
                imageButton.showContextMenu();
            }
        });

        getData();


    }

    private void getData(){
        String[] projection= {
                IdeaBoxContract.IdeaBoxDB._ID,
                IdeaBoxContract.IdeaBoxDB.TITLE,
                IdeaBoxContract.IdeaBoxDB.DESCRIPTION,
                IdeaBoxContract.IdeaBoxDB.TAGS,
                IdeaBoxContract.IdeaBoxDB.TIMESTAMP
        };

        Cursor cursor;
        if (toDate != fromDate){
            cursor = db.rawQuery("SELECT * from "+IdeaBoxContract.IdeaBoxDB.TABLENAME+" where "+
                    IdeaBoxContract.IdeaBoxDB.TIMESTAMP+"<= "+toDate+" AND "+IdeaBoxContract.IdeaBoxDB.TIMESTAMP+ ">= "+ fromDate,null);
        }else{
            cursor = db.rawQuery("SELECT * from "+IdeaBoxContract.IdeaBoxDB.TABLENAME,null);
        }
//        Cursor cursor = db.query(
//          IdeaBoxContract.IdeaBoxDB.TABLENAME,          //Table to query
//                projection,                             //Columns to return
//                null,                                   //The columns for the where clause
//                null,                                   //Values for the where clause
//                null,                                   //don't group the rows
//                null,                                   //don't filter by row group
//                null                                    //The sort order
//        );

        Log.d("Inside getData()","getData()");
        if (cursor == null){
            Log.d("state of cursor : "," null");
        }

        itemTitle = new ArrayList<>();
        itemDescription = new ArrayList<>();
        itemTag = new ArrayList<>();
        while (cursor.moveToNext()){
//            Toast.makeText(getApplicationContext(),"Inside while of cursor",Toast.LENGTH_SHORT).show();
//            Log.d("Inside while of cursor","Inside while of cursor");
            String  title = cursor.getString(cursor.getColumnIndexOrThrow(IdeaBoxContract.IdeaBoxDB.TITLE));
            String  description = cursor.getString(cursor.getColumnIndexOrThrow(IdeaBoxContract.IdeaBoxDB.DESCRIPTION));
            if (description == null){
                Log.d("state of description : "," null");
            }else {
                Log.d("state of description : ","Not null");
            }
            String  tag = cursor.getString(cursor.getColumnIndexOrThrow(IdeaBoxContract.IdeaBoxDB.TAGS));
            if (tag == null){
                Log.d("state of tag : "," null");
            }else {
                Log.d("state of tag : ","Not null");
            }

            itemTitle.add(title);
            itemDescription.add(description);
            itemTag.add(tag);
        }

        cursor.close();

        prepareArray();
    }

    private void prepareArray(){
        int len = itemTitle.size();
        dataSet = new String[len][3];

        for(int i = 0; i < len; i++){
            dataSet[i][0] = itemTitle.get(i).toString();
            dataSet[i][1] = itemDescription.get(i).toString();
            dataSet[i][2] = itemTag.get(i).toString();
        }

        Intent intent = new Intent(getApplicationContext(), RecyclerViewResult.class);
        startActivity(intent);
    }

}
