package com.example.happy934.tempideabox.database.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Blob;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by happy on 22/3/18.
 */

@Entity(indices = {@Index(value = "ideaId")},foreignKeys = @ForeignKey(entity = Textual.class,
                                    parentColumns = "id",
                                    childColumns = "ideaId",
                                    onDelete = CASCADE))
public class Graphical {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    byte[] blob;

    @ColumnInfo
    private int ideaId;

    public Graphical(byte[] blob, int ideaId){
        this.blob = blob;
        this.ideaId = ideaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public int getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(int ideaId) {
        this.ideaId = ideaId;
    }
}
