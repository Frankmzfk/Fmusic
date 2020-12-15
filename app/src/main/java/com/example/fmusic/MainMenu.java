package com.example.fmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    Button go_music;
    Button go_fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        go_music = (Button) findViewById(R.id.go_music);
        go_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, MusicActivity.class);
                intent.putExtra("TAG", MusicActivity.TAG_ALL);
                startActivity(intent);
            }
        });
        go_fav = (Button) findViewById(R.id.go_favorites);
        go_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, MusicActivity.class);
                intent.putExtra("TAG", MusicActivity.TAG_MY);
                startActivity(intent);
            }
        });
    }
}