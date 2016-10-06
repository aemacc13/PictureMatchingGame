package com.example.aemacc13.picturematchinggame;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer= MediaPlayer.create(this, R.raw.palette_town);
        mediaPlayer.setVolume(0.4f, 0.4f);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    public void showAbout() {
        LinearLayout about= (LinearLayout) findViewById(R.id.about);
        Button back= (Button)findViewById(R.id.back);
        about.setVisibility(View.VISIBLE);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAbout();
            }
        });
    }

    public void hideAbout() {
        LinearLayout about= (LinearLayout) findViewById(R.id.about);
        about.setVisibility(View.GONE);
    }
}
