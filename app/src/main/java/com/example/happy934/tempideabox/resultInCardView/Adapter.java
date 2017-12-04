package com.example.happy934.tempideabox.resultInCardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy934.tempideabox.KeyBoardInput;
import com.example.happy934.tempideabox.R;
import com.example.happy934.tempideabox.ResultViewActivity;
import com.example.happy934.tempideabox.database.DataBaseTransactions;


/**
 * Created by happy934 on 18/10/17.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();
    private String[][] dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{


        CardView cardView;
        TextView title;
        TextView description;

        View view;

        MyViewHolder(View view){

            super(view);
            cardView = view.findViewById(R.id.cardView);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            view.setOnCreateContextMenuListener(this);

            this.view = view;
        }

        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Select The option");
            MenuItem edit = menu.add(0, 1, 0, "View");//groupId, itemId, order, title
            MenuItem viewItem = menu.add(0, 2, 0, "edit");
            MenuItem delete = menu.add(0, 3, 0, "delete");

            edit.setOnMenuItemClickListener(onChange);
            viewItem.setOnMenuItemClickListener(onChange);
            delete.setOnMenuItemClickListener(onChange);

        }
        private final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(RecyclerViewResult.context,Integer.toString(item.getItemId()),Toast.LENGTH_LONG).show();
                switch (item.getItemId()){
                    case 1:
                        Intent intent = new Intent(RecyclerViewResult.context, KeyBoardInput.class);
                        TextView title = (TextView) view.findViewById(R.id.title);
                        TextView description = (TextView) view.findViewById(R.id.description);
                        intent.putExtra("title",title.getText().toString());
                        intent.putExtra("description",description.getText().toString());
                        RecyclerViewResult.context.startActivity(intent);
                        return true;
                    case 2:
                        Toast.makeText(RecyclerViewResult.context,"Delete",Toast.LENGTH_LONG).show();
                        return true;
                }
                return false;
            }
        };

    }
    public Adapter(String[][] dataSet){
        this.dataSet = dataSet;
    }

    public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_result_view,parent,false);
        view.setOnClickListener(mOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    public void onBindViewHolder(MyViewHolder myViewHolder,int position){

        myViewHolder.title.setText(dataSet[position][0]);
        myViewHolder.description.setText(dataSet[position][1]);

    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }


    public int getItemCount(){
        return dataSet.length;
    }

    private class MyOnClickListener implements View.OnClickListener{

        public void onClick(final View view) {
            int itemPosition = RecyclerViewResult.recyclerView.getChildLayoutPosition(view);
            Toast.makeText(RecyclerViewResult.context, Integer.toString(itemPosition), Toast.LENGTH_LONG).show();
        }
    }
}
