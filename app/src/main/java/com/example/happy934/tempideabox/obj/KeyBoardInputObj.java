package com.example.happy934.tempideabox.obj;

import java.io.Serializable;

/**
 * Created by happy on 22/3/18.
 */

public class KeyBoardInputObj implements Serializable{
    private String title;
    private String description;
    private String tagString;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }
}
