package com.example.happy934.tempideabox.database.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Blob;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by happy on 23/3/18.
 */

@Entity(indices = {@Index(value = "ideaId")},foreignKeys = @ForeignKey(entity = Textual.class,
                                    parentColumns = "id",
                                    childColumns = "ideaId",
                                    onDelete = CASCADE))
public class Audio {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] audioBlob;

    @ColumnInfo
    private int ideaId;

    public Audio(byte[] audioBlob, int ideaId){
        this.audioBlob = audioBlob;
        this.ideaId = ideaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getAudioBlob() {
        return audioBlob;
    }

    public void setAudioBlob(byte[] audioBlob) {
        this.audioBlob = audioBlob;
    }

    public int getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(int ideaId) {
        this.ideaId = ideaId;
    }
}
