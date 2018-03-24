package com.example.happy934.tempideabox.database.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.happy934.tempideabox.database.room.Textual;

import java.util.List;

/**
 * Created by happy on 23/3/18.
 */

@Dao
public interface TextualDao {

    @Insert
    void insertOnlySingleRecord(Textual textual);

    @Insert
    void insertMultipleRecord(List<Textual> textuals);

    @Query("SELECT * from Textual")
    List<Textual> fetchAllData();

    @Query("SELECT *from Textual WHERE id =:ideaId")
    Textual getSingleRecord(int ideaId);

    @Query("UPDATE OR ABORT Textual SET  title = :title, text_desc = :description WHERE id = :id")
    void updateRec(String title, String description, int id);


    @Delete
    void deleteRecord(Textual textual);

}
