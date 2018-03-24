package com.example.happy934.tempideabox.database.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.happy934.tempideabox.database.room.TextualTag;

import java.util.List;

/**
 * Created by happy on 23/3/18.
 */

@Dao
public interface TextualTagDao {

    @Insert
    void insert(TextualTag textualTag);

    @Query("SELECT * from TextualTag")
    List<TextualTag> getAll();
}
