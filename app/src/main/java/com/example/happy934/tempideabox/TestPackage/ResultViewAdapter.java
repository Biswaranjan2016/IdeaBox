package com.example.happy934.tempideabox.TestPackage;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy934.tempideabox.R;
import com.example.happy934.tempideabox.database.room.Textual;

import java.util.List;

/**
 * Created by happy on 23/3/18.
 */

public class ResultViewAdapter extends RecyclerView.Adapter<ResultViewAdapter.CardHolder>{

    public static int index;
    List<Textual> textuals;
    private static final String TAG = "ResultViewAdapter";
    class CardHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView title;
        TextView description;

        public CardHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_cardTitle);
            description = (TextView) view.findViewById(R.id.textView_cardDescription);
            view.setOnCreateContextMenuListener(this);
        }

        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Op");
            MenuItem menuItem;
            index = getAdapterPosition();
            menu.add(0, view.getId(), 0, "View");
            menu.add(0, view.getId(), 1, "Update");
            menu.add(0, view.getId(), 2, "Delete");
        }


    }
    public ResultViewAdapter(List<Textual> textuals){
        this.textuals = textuals;
    }

    public CardHolder onCreateViewHolder(ViewGroup parent, int pos){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new CardHolder(view);
    }

    public void onBindViewHolder(CardHolder cardHolder, int pos){
        Textual textual = textuals.get(pos);
        cardHolder.title.setText(textual.getTitle());
        cardHolder.description.setText(textual.getText_desc());
    }

    public int getItemCount(){
        return textuals.size();
    }
}
