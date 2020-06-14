package com.chris.gamelife.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.GameModel.ClassicModel;
import com.chris.gamelife.GameModel.CountDownModel;
import com.chris.gamelife.GameModel.LimitModel;
import com.chris.gamelife.Service.BackGroundMusicService;
import com.chris.gamelife.R;

import java.io.File;
import java.io.IOException;

public class MenuActivity extends AppCompatActivity {
    private Intent intent;
    private BackGroundMusicService bgm_service;
    private MediaPlayer mediaPlayer;
    public static String path;

    // 监听当前页面是否在运行
    private ServiceConnection conn = new ServiceConnection() {

        // 当 Service 连接时
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bgm_service = ((BackGroundMusicService.AudioBinder) service).getService();
        }

        // 当 Service 没有连接时
        @Override
        public void onServiceDisconnected(ComponentName name) {
            bgm_service = null;
        }
    };
    private Button home_btn;
    private Button music_btn;
    private Button author_btn;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // 初始化文件
        defaultFile();

        // 初始化文件保存位置
        path = getDataDir().toString();

        /*final Intent tent = getIntent();  // 用于获取上一个活动传递的信息
        Log.i("INTENT", "getIntent() = " + tent.getIntExtra("model", 0));
        if (tent != null) {
            setContentView(R.layout.activity_menu);
        } else {
            setContentView(R.layout.menu_continue);
            // 绑定继续按键
            Button continue_btn = (Button) findViewById(R.id.continueGame);
            continue_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int model = getIntent().getIntExtra("model", 1);
                    switch (model) {
                        case 1:
                            intent = new Intent("com.game_2048.classic_model");
                            break;
                        case 2:
                            intent = new Intent("com.game_2048.limit_model");
                            break;
                        case 3:
                            intent = new Intent("com.game_2048.countdown_model");
                            break;
                        default:
                            break;
                    }
                    // 传递信息给下一个活动
                    intent.putExtra("model", tent.getIntExtra("model", 1));
                    intent.putExtra("time", tent.getIntExtra("time", 0));
                    intent.putExtra("cards", (Card[]) tent.getSerializableExtra("cards"));

                    startActivity(intent);
                }
            });
        }*/

        // 绑定按键
        Button home_btn = findViewById(R.id.home);
        Button music_btn = findViewById(R.id.music);
        Button author_btn = findViewById(R.id.author);

        // 绑定音乐
        if (mediaPlayer!= null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.bg_music);
        mediaPlayer.start();



        setMainIcon(home_btn, music_btn, author_btn);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MenuActivity.this, RelaxCenter.class);
                mediaPlayer.pause();
                startActivity(intent);
            }
        });

        music_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                startActivity(new Intent("com.game_2048.sound_control"));
            }
        });

        // 设置模式选择点击事件
        Button model_btn = findViewById(R.id.model);
        model_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent("com.game_2048.model_select");
                mediaPlayer.pause();
                startActivity(intent);
            }
        });

        // 设置开始游戏点击事件
        Button start_btn = findViewById(R.id.start);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                intent = getIntent();
                int diff = intent.getIntExtra("diff", 1);

                Intent intent1;
                switch (diff) {
                    case 1:
                        intent1 = new Intent("com.game_2048.classic_model");
                        startActivity(intent1);
                        break;
                    case 2:
                        intent1 = new Intent("com.game_2048.limit_model");
                        startActivity(intent1);
                        break;
                    case 3:
                        intent1 = new Intent("com.game_2048.countdown_model");
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
            }
        });

        // 设置历史按键点击事件
        Button history_btn = findViewById(R.id.history);
        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                intent = new Intent("com.game_2048.history");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requsetCode, int resultCode, Intent data) {
        super.onActivityResult(requsetCode, resultCode, data);

    }

    private void setMainIcon(Button home_btn, Button music_btn, Button author_btn) {
        Drawable drawable;
        // 控制音量图标大小
        drawable = getResources().getDrawable(R.mipmap.home);
        drawable.setBounds(0, 0, 100, 100);
        home_btn.setCompoundDrawables(null, null, drawable, null);

        music_btn = findViewById(R.id.music);
        drawable = getResources().getDrawable(R.mipmap.music);
        drawable.setBounds(0, 0, 108, 108);
        music_btn.setCompoundDrawables(null, null, drawable, null);

        // 设置 author 图标
        author_btn = findViewById(R.id.author);
        drawable = getResources().getDrawable(R.mipmap.author);
        drawable.setBounds(0, 0, 128, 128);
        author_btn.setCompoundDrawables(null, null, drawable, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void defaultFile() {
        File file = new File(getDataDir(), "classic_score");
        Log.i("MENU", "====classic: " + file.getAbsolutePath() + "====");

        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.i("FILE", "====classic: " + file.getAbsolutePath() + "====");

                file = new File(getDataDir(), "limit_score");
                file.createNewFile();
                Log.i("FILE", "====limit: " + file.getAbsolutePath() + "====");

                file = new File(getDataDir(), "countdown_score");
                file.createNewFile();
                Log.i("FILE", "====countdown: " + file.getAbsolutePath() + "====");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath() {
        return path;
    }
}
