package com.example.happy934.tempideabox;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    TextView textView;
    public static String date;

    public DatePickerFragment() {
        // Required empty public constructor
    }



    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    public void onDateSet(DatePicker datePicker,int year, int month, int day){

        date = Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year);
        TripToMyIdea.selectedId.setText(date);
    }
}
