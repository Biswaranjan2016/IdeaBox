package com.example.happy934.tempideabox.database.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by happy on 23/3/18.
 */
@Entity(indices = {@Index(value = {"tag"},unique = true)})
public class Tags {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String tag;

    public Tags(String tag){
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
