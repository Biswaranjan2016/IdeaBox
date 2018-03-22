package com.example.happy934.tempideabox.obj;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by happy on 21/3/18.
 */

public class DatabaseDataObject {

    private String title;

    private List<Blob> pictureBlobs;
    private List<Blob> audioBlobs;
    private String description;

    private String[] tags;

    public DatabaseDataObject(){
        pictureBlobs = new ArrayList<>();
        audioBlobs = new ArrayList<>();
    }
    public DatabaseDataObject(String title,Blob pictureBlob, Blob audioBlob, String description, String tags[]){
        this.title = title;
        this.pictureBlobs.add(pictureBlob);
        this.audioBlobs.add(audioBlob);
        this.description = description;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Blob> getPictureBlobs() {
        return pictureBlobs;
    }

    public void setPictureBlob(Blob pictureBlob) {
        pictureBlobs.add(pictureBlob);
    }

    public List<Blob> getAudioBlobs() {
        return audioBlobs;
    }

    public void setAudioBlob(Blob audioBlob) {
        this.audioBlobs.add(audioBlob);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
