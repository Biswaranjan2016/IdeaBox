package com.example.happy934.tempideabox;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripToMyIdea extends Fragment implements View.OnClickListener {

    TextView editTextFrom;
    TextView editTextTo;
    String date;

    long value1, value2;

    public static ImageButton selectedImageButton;
    public static TextView selectedId;


    public TripToMyIdea() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_to_my_idea, container, false);
    }


    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        editTextFrom = (TextView) getView().findViewById(R.id.fromDate);
        editTextTo = (TextView) getView().findViewById(R.id.toDate);

        if (editTextFrom==null){
            Toast.makeText(getContext(),"null",Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(getContext(),"Not null",Toast.LENGTH_SHORT).show();
        }

        ImageButton imageButtonFrom = (ImageButton) getView().findViewById(R.id.fromImageButton);
        ImageButton imageButtonTo = (ImageButton) getView().findViewById(R.id.toImageButton);
        Button button = (Button) getView().findViewById(R.id.button2);
        imageButtonFrom.setOnClickListener(this);
        imageButtonTo.setOnClickListener(this);
        button.setOnClickListener(this);
    }

//    public void onResume(){
//        super.onResume();
//        Toast.makeText(getContext(),"Resumed",Toast.LENGTH_SHORT).show();
//    }

    public void onClick(View view){
        if (!(view.getId() == R.id.button2)){
            // Create the instance of DatePicker fragment and invoke show on that instance
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(getFragmentManager(),"timePicker");

            selectedImageButton = (ImageButton)view;
            switch (selectedImageButton.getId()){
                case R.id.toImageButton:
                    selectedId = (TextView) getActivity().findViewById(R.id.toDate);
                    break;
                case R.id.fromImageButton:
                    selectedId = (TextView) getActivity().findViewById(R.id.fromDate);
                    break;
            }
        }else {
            Intent intent = new Intent(getContext(),ResultViewActivity.class);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            try {

                Date date1 = sdf.parse(editTextTo.getText().toString());
                value1=date1.getTime();

                Date date2 = sdf.parse(editTextFrom.getText().toString());
                value2=date2.getTime();

//                Log.d("value1",Long.toString(value1));
//                Log.d("value2",Long.toString(value2)+"\n");
            }catch (Exception e){

            }
            intent.putExtra("fromDate",Long.toString(value2));
            intent.putExtra("toDate",Long.toString(value1));
            startActivity(intent);
        }

    }



}
