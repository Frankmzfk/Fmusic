package com.example.fmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class DetailMusic extends AppCompatActivity {

    // Класс для отображения и обработки детальной инфы - то окошко что открывается при нажатии на фильм в ресайкл вью

    Button add_delete;
    Button play;
    LinearLayout text;

    TextView band;
    TextView track;
    ImageView imageView;
    String id;
    boolean play_state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_music);

        band = (TextView) findViewById(R.id.band);
        track = (TextView) findViewById(R.id.track);
        imageView = (ImageView) findViewById(R.id.iv);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        add_delete = (Button) findViewById(R.id.on_off_fav_butt);
        add_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music lm = MyData.get_music(id);

                if (!lm.fav){
                    add_delete.setText("Убрать из избранного");
                }
                else {
                    add_delete.setText("Добавить в избранное");
                }
                MyData.swap_fav(lm);
            }
        });

        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!play_state)
                {
                    play_state = !play_state;
                    play.setText("Остановить");
                    Music lm = MyData.get_music(id);
                    try {
                        MyData.play(lm.file, DetailMusic.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    play_state = !play_state;
                    play.setText("Проиграть");
                    MyData.player.stop();
                }
            }
        });

        text = (LinearLayout) findViewById(R.id.text_music);


        Music m = MyData.get_music(id);
        set_music(m);
    }

    private void set_music(Music m) {
        Picasso.get().load(m.img).into(imageView);
        band.setText("  " + m.band);
        track.setText("  " + m.track);

        for (String line: m.text) {
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            lparams.gravity = Gravity.CENTER;

            TextView text_line = new TextView(this);
            text_line.setLayoutParams(lparams);
            text_line.setText("    " + line);
            this.text.addView(text_line);
        }

        if (m.fav){
            add_delete.setText("Убрать из избранного");
        }
        else {
            add_delete.setText("Добавить в избранное");
        }
    }
}