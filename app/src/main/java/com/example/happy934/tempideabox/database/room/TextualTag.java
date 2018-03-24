package com.example.happy934.tempideabox.database.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by happy on 23/3/18.
 */

@Entity(tableName = "TextualTag",
        indices = {@Index(value = "ideaId"),@Index(value = "tagId")},
        primaryKeys = {"ideaId","tagId"},
        foreignKeys = {@ForeignKey(entity = Textual.class,
                                    parentColumns = "id",
                                    childColumns = "ideaId",
                                    onDelete = CASCADE
                                    ),
                        @ForeignKey(entity = Tags.class,
                                    parentColumns = "id",
                                    childColumns = "tagId",
                                    onDelete = CASCADE
                                    )})
public class TextualTag {

    private int ideaId;
    private int tagId;

    public void TextualTag(){

    }

    public TextualTag(int ideaId, int tagId){
        this.ideaId = ideaId;
        this.tagId = tagId;
    }

    public int getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(int ideaId) {
        this.ideaId = ideaId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
