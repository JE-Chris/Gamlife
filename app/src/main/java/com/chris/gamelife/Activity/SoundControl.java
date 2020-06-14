package com.chris.gamelife.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.R;

public class SoundControl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_control);

        // 设置图标
        Drawable drawable = getResources().getDrawable(R.mipmap.sound_bg);
        drawable.setBounds(0, 0, 116, 116);

        Button sound_btn = findViewById(R.id.sound);
        sound_btn.setCompoundDrawables(null, null, drawable, null);

        controlVolume();

        Button back_btn = findViewById(R.id.done);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.game_2048.menu"));
            }
        });
    }

    private void controlVolume() {
        SeekBar seekBar = findViewById(R.id.bg_sound);
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        // 获取当前系统最大音量
        final int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        seekBar.setMax(maxVolume);
        // 获取当前音量
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Log.i("init", String.valueOf(currentVolume));
        seekBar.setProgress(currentVolume);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress;
            AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
            TextView textView = null;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    seekBarProgress = progress;
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM , progress, 0);
                    int currentVolume = am.getStreamVolume(AudioManager.STREAM_SYSTEM );
                    seekBar.setProgress(currentVolume);
                }
                textView.setText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView.setText("开始了");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("停止了，当前值为：" + seekBarProgress);
                if(seekBarProgress == seekBar.getMax()){
                    textView.setText("达到最值");
                }
            }
        });
    }
}
