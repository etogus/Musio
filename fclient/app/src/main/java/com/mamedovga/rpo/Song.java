package com.mamedovga.rpo;

public class Song {
    private long id;
    private String title;
    private String artist;

    Song(long songID, String songTitle, String songArtist) {
        id = songID;
        title = songTitle;
        artist = songArtist;
    }

    public long getId() {return id;}
    public String getTitle() {return title;}
    String getArtist() {return artist;}
}