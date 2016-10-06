package com.example.aemacc13.picturematchinggame;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**sounds from:
 * http://downloads.khinsider.com/game-soundtracks/browse/P
 * http://www.aiomp3.com/mp3/pokeball-opening-sound-download.html
 * http://audio.online-convert.com/convert-to-ogg
 */

public class GameActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    boolean wonGame= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBattleMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!wonGame)
            stoptBattleMusic();
        else
            stopWinMusic();
    }

    public void startWinMusic(){
        mediaPlayer= MediaPlayer.create(this, R.raw.theme);
        mediaPlayer.setVolume(0.4f, 0.4f);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        wonGame= true;
    }

    public void stopWinMusic(){
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    public void startBattleMusic(){
        mediaPlayer= MediaPlayer.create(this, R.raw.battle);
        mediaPlayer.setVolume(0.4f, 0.4f);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void stoptBattleMusic(){
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    public void startThinking() {
        LinearLayout loading= (LinearLayout)findViewById(R.id.thinking);
        ImageView loading_bar= (ImageView)findViewById(R.id.loading_bar);
        loading.setVisibility(View.VISIBLE);

        //http://stackoverflow.com/questions/3137490/how-to-spin-an-android-icon-on-its-center-point
        RotateAnimation spin= new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        spin.setDuration((long)375);
        spin.setRepeatCount(1);
        loading_bar.startAnimation(spin);
    }

    public void stopThinking() {
        LinearLayout loading= (LinearLayout)findViewById(R.id.thinking);
        loading.setVisibility(View.GONE);
    }
}
