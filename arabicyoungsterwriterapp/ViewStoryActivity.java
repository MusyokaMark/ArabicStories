package com.example.arabicyoungsterwriterapp;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ViewStoryActivity extends AppCompatActivity {

    StoryModel model;
    private ImageView imagePlayPause, imageGif;
    private TextView textCurrentTime, textTotalDuration, tvTitle, textGraph;
    private SeekBar playerSeekBar;
    private MediaPlayer mediaPlayer;
    private final Handler handler = new Handler();
    private ImageView ivMain;
    private LinearLayout llSound;
    private RelativeLayout llMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);


        imagePlayPause = findViewById(R.id.imagePlayPause);
        textCurrentTime = findViewById(R.id.textCurrentTime);
        textTotalDuration = findViewById(R.id.textTotalDuration);
        playerSeekBar = findViewById(R.id.playerSeekBar);
        imageGif = findViewById(R.id.ivGif);
        tvTitle = findViewById(R.id.tvTitle);
        textGraph = findViewById(R.id.textGraph);
        ivMain = findViewById(R.id.ivMain);
        llSound = findViewById(R.id.llSound);
        llMain = findViewById(R.id.llMain);
        mediaPlayer = new MediaPlayer();
        playerSeekBar.setMax(100);

        Intent intent = getIntent();

        model = intent.getParcelableExtra("key");

        tvTitle.setText(model.getTitle());
        textGraph.setText(model.getPara());

        if (model.getAudio().equals("null")) {
            llSound.setVisibility(View.INVISIBLE);
        } else {
            llSound.setVisibility(View.VISIBLE);
        }

        tvTitle.setTextColor(Color.parseColor(model.getTvColor()));
        textGraph.setTextColor(Color.parseColor(model.getTvColor()));
        llMain.setBackgroundColor(Color.parseColor(model.getBgColor()));

        if (!model.getImage().equals("")) {

            Glide.with(this).load(model.getImage()).into(ivMain);

        }


        Glide.with(this)
                .load(model.getGif())
                .into(imageGif);


        imagePlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    imagePlayPause.setImageResource(R.drawable.ic_play);
                } else {
                    mediaPlayer.start();
                    imagePlayPause.setImageResource(R.drawable.ic_pause);
                    updateSeekBar();
                }
            }
        });
        prepareMediaPlayer();
        playerSeekBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SeekBar seekBar = (SeekBar) v;
                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                textCurrentTime.setText(milliSecondToTimer(mediaPlayer.getCurrentPosition()));
                return false;
            }
        });
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                playerSeekBar.setSecondaryProgress(percent);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playerSeekBar.setProgress(0);
                imagePlayPause.setImageResource(R.drawable.ic_play);
                textCurrentTime.setText(R.string.zero);
                textTotalDuration.setText(R.string.zero);
                mediaPlayer.reset();
                prepareMediaPlayer();
            }
        });

    }

    public void prepareMediaPlayer() {
        try {
            mediaPlayer.setDataSource(model.getAudio());// URL of music file
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// URL of music file
            mediaPlayer.prepare();
            textTotalDuration.setText(milliSecondToTimer(mediaPlayer.getDuration()));
        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateSeekBar() {
        if (mediaPlayer.isPlaying()) {
            playerSeekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }

    private String milliSecondToTimer(long milliSeconds) {
        String timerString = "";
        String secondsString;

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            timerString = hours + ":";

        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        timerString = timerString + minutes + ":" + secondsString;
        return timerString;
    }    private final Runnable updater = new Runnable() {
        @Override
        public void run() {

            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            textCurrentTime.setText(milliSecondToTimer(currentDuration));
        }
    };

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }


}