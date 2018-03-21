package com.example.happy934.tempideabox.obj;

import java.sql.Blob;

/**
 * Created by happy on 21/3/18.
 */

public class DatabaseDataObject {

    private String title;

    private Blob pictureBlob;
    private Blob audioBlob;
    private String description;

    private String[] tags;

    public DatabaseDataObject(String title,Blob pictureBlob, Blob audioBlob, String description, String tags[]){
        this.title = title;
        this.pictureBlob = pictureBlob;
        this.audioBlob = audioBlob;
        this.description = description;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Blob getPictureBlob() {
        return pictureBlob;
    }

    public void setPictureBlob(Blob pictureBlob) {
        this.pictureBlob = pictureBlob;
    }

    public Blob getAudioBlob() {
        return audioBlob;
    }

    public void setAudioBlob(Blob audioBlob) {
        this.audioBlob = audioBlob;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
