package com.example.happy934.tempideabox.database.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by happy on 22/3/18.
 */

@Entity
public class Textual {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String title,text_desc,date;

    @ColumnInfo
    private int rank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText_desc() {
        return text_desc;
    }

    public void setText_desc(String text_desc) {
        this.text_desc = text_desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
