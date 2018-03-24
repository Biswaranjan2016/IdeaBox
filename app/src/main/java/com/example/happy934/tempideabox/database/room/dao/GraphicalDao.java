package com.example.happy934.tempideabox.database.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.happy934.tempideabox.database.room.Audio;
import com.example.happy934.tempideabox.database.room.Graphical;

import java.util.List;

/**
 * Created by happy on 23/3/18.
 */
@Dao
public interface GraphicalDao {
    @Query("SELECT COUNT(*) from Audio")
    abstract int countAudio();

    @Query("SELECT *from Graphical")
    List<Graphical> getAll();

    @Insert
    void insert(Graphical graphical);
}
