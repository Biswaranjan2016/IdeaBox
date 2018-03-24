package com.example.happy934.tempideabox.database.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.nfc.Tag;

import com.example.happy934.tempideabox.database.room.Audio;
import com.example.happy934.tempideabox.database.room.Tags;

import java.util.List;

/**
 * Created by happy on 23/3/18.
 */
@Dao
public interface TagsDao {
    @Query("SELECT COUNT(*) from Tags")
    abstract int countAudio();

    @Query("SELECT *from Tags ")
    List<Tags> getAll();

    @Insert
    void insert(Tags tag);
}
