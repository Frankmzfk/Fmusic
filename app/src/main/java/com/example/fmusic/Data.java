package com.example.fmusic;

import android.widget.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Data
{
    private static List<Music> my_music;
    private static List<Music> all_music;
    private static String my_id;
    
    public static Music get_music(String id)
    {
        for (Music music: my_music) {
            if (music.id.equals(id)){
                return music;
            }
        }
        for (Music music: all_music) {
            if (music.id.equals(id)){
                return music;
            }
        }
        return null;
    }

    public static void swap_fav(Music m)
    {
        m.fav = !m.fav;
        // TODO добавить запрос в firebase на изменеие поля
    }
}
