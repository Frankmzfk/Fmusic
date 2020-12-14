package com.example.fmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    Button go_music;
    Button go_fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        go_music = (Button) findViewById(R.id.go_music);
        go_fav = (Button) findViewById(R.id.go_favorites);
    }
}