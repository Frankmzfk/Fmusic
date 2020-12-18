package com.example.fmusic;

import java.util.ArrayList;
import java.util.List;

public class Music {

    public String id; // файрбайсный айдишник
    public String band;
    public String img;
    public String track;
    public List<String> text;
    boolean fav; // избранное ли

    Music(String b, String i, String t, String id, List<String> text, boolean fav) {
        band = b;
        img = i;
        track = t;
        this.id = id;
        this.text = text;
        this.fav = fav;
    }

}
