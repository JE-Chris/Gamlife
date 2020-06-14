package com.chris.gamelife.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.chris.gamelife.R;

public class BackGroundMusicService extends Service implements MediaPlayer.OnCompletionListener {

    MediaPlayer player;
    private final IBinder binder = new AudioBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        // 建立音频播放
        player = MediaPlayer.create(this, R.raw.bg_music);
        // 监听音频是否播放完成
        player.setOnCompletionListener(this);
    }

    // 设置在服务开始时执行
    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        if (!player.isPlaying()) {
            player.start();
        }
        return START_STICKY;
    }

    // 销毁 Service
    @Override
    public void onDestroy() {
        Log.i("BGM_Service", "********BGM Service is stopping********");

        if (player.isPlaying()) {
            player.stop();
        }
        player.release();

        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //监听音乐是否完成的函数
    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.start();
    }

    public class AudioBinder extends Binder {
        //返回Service对象
        public BackGroundMusicService getService() {
            return BackGroundMusicService.this;
        }
    }
}
