package com.example.todolist;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AlarmActivity extends AppCompatActivity {


    Ringtone ringtone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, notification );
        if (ringtone == null){// если будильник не установлен то какойто звук звонка
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(this, notification );


        }
        if (ringtone != null) { // когда нет ни того не другого
            ringtone.play();


        }

    }
    @Override
    protected void  onDestroy(){
        if(ringtone!=null && ringtone.isPlaying()){
            ringtone.stop();
        }
        super.onDestroy();
    }
}
