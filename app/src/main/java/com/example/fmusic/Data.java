package com.example.fmusic;

import android.util.Log;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data
{
    private static List<Music> my_music;
    private static Map<String, Music> all_music;
    private static String my_id;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void set_my_id(String id)
    {
        my_id = id;
    }

    public static Music get_music(String id)
    {
        return all_music.get(id);
    }

    public static void swap_fav(Music m)
    {
        m.fav = !m.fav;
        // TODO добавить запрос в firebase на изменеие поля
    }

    public static void init(){
        if (all_music == null)
        {
            all_music = new HashMap<>();
            db.collection("music")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());

                                    String band = (String) document.get("band");
                                    String img = (String) document.get("img");
                                    String id = document.getId();
                                    String track = (String) document.get("track");
                                    List<String> text = (ArrayList<String>) document.get("text");

                                    Music m = new Music(band, img, track, id, text, false);
                                    all_music.put(document.getId(), m);
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });


            db.collection("fav_music")
                    .whereEqualTo("id_man", my_id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());

                                    String music = (String) document.get("id_music");
                                    Music m = all_music.get(music);
                                    my_music.add(m);
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }

    }

    public static void set_music_in_rv(RecyclerView rv){

    }
}
