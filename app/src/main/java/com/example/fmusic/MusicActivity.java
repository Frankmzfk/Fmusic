package com.example.fmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MusicActivity extends AppCompatActivity {

    public static String TAG_ALL = "ALL";
    public static String TAG_MY = "MY";
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        rv = (RecyclerView) findViewById(R.id.rv);
        Intent intent = getIntent();
        String s = intent.getStringExtra("TAG");
        if (s.equals(TAG_ALL))
        {
            // set all_music
        }
        else if (s.equals(TAG_MY))
        {
            // set my music
        }

    }

    public static class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
        private final List<Music> values;

        public MusicAdapter(List<Music> music) {
            values = music;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.music_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Picasso.get().load(values.get(position).img).into(holder.imageView);

            holder.band.setText(values.get(position).band);
            holder.track.setText(values.get(position).track);

            holder.itemView.setTag(values.get(position));
            holder.itemView.setOnClickListener(onClickListener);
        }

        @Override
        public int getItemCount() {
            return values.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView band;
            final TextView track;
            final ImageView imageView;

            ViewHolder(View view) {
                super(view);
                band = view.findViewById(R.id.band);
                track = view.findViewById(R.id.track);
                imageView = view.findViewById(R.id.iv);
            }
        }

        final private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Music item = (Music) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, DetailMusic.class);
                intent.putExtra("id", item.id);
                context.startActivity(intent);
            }
        };
    }
}