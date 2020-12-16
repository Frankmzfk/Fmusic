package com.example.fmusic;

import android.util.Log;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
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

    public static void exit(){
        my_music = null;
        all_music = null;
        my_id = null;
    }

    private static String delete_id;

    public static void swap_fav(Music m)
    {
        if (m.fav)
        {
            // удаление
            db.collection("fav_music")
                    .whereEqualTo("id_man", my_id)
                    .whereEqualTo("id_music", m.id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if (task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    delete_id = document.getId();
                                }
                                Log.e("TAG", delete_id);
                                db.collection("fav_music").document(delete_id)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                                delete_id = "";
                                                m.fav = false;

                                                my_music.remove(m);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting document", e);
                                                delete_id = "";

                                            }
                                        });
                            }
                            else
                            {
                                Log.e("TAGload", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        else
        {
            // добавление
            Map<String, Object> new_fav_mus = new HashMap<>();
            new_fav_mus.put("id_man", my_id);
            new_fav_mus.put("id_music", m.id);

            db.collection("fav_music")
                    .add(new_fav_mus)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference)
                        {
                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                            m.fav = true;
                            my_music.add(m);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });

        }
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

            my_music = new ArrayList<>();
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
                                    m.fav = true;
                                    my_music.add(m);
                                    Log.e("TAG", my_music.size() + "");
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }

    }

    public static void set_all_music_in_rv(RecyclerView rv){
        if (all_music != null)
        {
            rv.setAdapter(new MusicActivity.MusicAdapter(new ArrayList<Music>(all_music.values())));
        }
        else
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
                                rv.setAdapter(new MusicActivity.MusicAdapter((ArrayList<Music>) all_music.values()));

                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }

    public static void set_my_music_in_rv(RecyclerView rv){
        if (my_music != null)
        {
            Log.e("TAG", my_music.size() + "");
            rv.setAdapter(new MusicActivity.MusicAdapter(my_music));
        }
        else
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

            my_music = new ArrayList<>();
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
                                rv.setAdapter(new MusicActivity.MusicAdapter(new ArrayList<Music> (my_music)));
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }
}
