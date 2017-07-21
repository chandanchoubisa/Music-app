package com.example.dell.mzz;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Dell on 05-Mar-17.
 */
public class Song implements Serializable{
    public long id;
    public Bitmap bitmap;
    public   String title;
    public String artist;
    public Song(long songID, String songTitle, String songArtist,Bitmap bitmap1) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        bitmap=bitmap1;
    }
    public long getID(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
}

