package com.example.todolist;

import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AlarmActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        mediaPlayer = MediaPlayer.create(this, R.raw.me); // replace with your alarm sound resource

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true); // loop the sound
            mediaPlayer.start(); // start playing the sound
        }

        // Getting the task text from Intent
        String taskText = getIntent().getStringExtra("task_text");

        // Setting up TextView
        TextView textView = findViewById(R.id.textview);
        textView.setText(taskText); // Setting the task text

        // Setting up the OK button
        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                finish(); // Close the activity
            }
        });
    }

    @Override
    protected void onDestroy() {
        // The mediaPlayer is stopped and released in the button's OnClickListener.
        super.onDestroy();
    }

}
