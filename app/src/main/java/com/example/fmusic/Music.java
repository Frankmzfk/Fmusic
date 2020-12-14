package com.example.fmusic;

public class Music {
    public String id;
    public String band;
    public String img;
    public String track;

    Music(String b, String i, String t, String id) {
        band = b;
        img = i;
        track = t;
        this.id = id;
    }
}
