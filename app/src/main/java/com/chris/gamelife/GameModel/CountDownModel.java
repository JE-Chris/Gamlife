package com.chris.gamelife.GameModel;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.Activity.HistoryActivity;
import com.chris.gamelife.Model.Card;
import com.chris.gamelife.Model.CardLayout;
import com.chris.gamelife.Model.Player;
import com.chris.gamelife.R;
import com.chris.gamelife.util.DialogToOver;
import com.chris.gamelife.util.MyTimer;

import java.util.HashMap;
import java.util.List;

public class CountDownModel extends AppCompatActivity implements CardLayout.OnLayoutListener, MyTimer.OnTimeChangeListener, View.OnTouchListener {
    private CardLayout cardLayout;
    private MyTimer myTimer;
    private int time_limit = 30, scoreChange = 0, time_rest = time_limit;
    private long score = 0, best = 0;
    private TextView score_tv, best_tv, limit_tv;
    private Button back_btn;
    private HashMap<Integer, Player> players = new HashMap<>(); // 用于整理历史记录
    private MediaPlayer mediaPlayer;
    private Toast toast;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_countdown);

        // 绑定相应的按键、view
        cardLayout = (CardLayout) findViewById(R.id.game_limit);
        final Intent tent = getIntent();  // 用于获取上一个活动传递的信息
        if (tent != null) {
            cardLayout.setCards ( (List<Card>) tent.getSerializableExtra("layout") );
            time_limit = tent.getIntExtra("time", 30);
        }

        score_tv = (TextView) findViewById(R.id.tvScore);
        best_tv = (TextView) findViewById(R.id.tvBest);
        limit_tv = (TextView) findViewById(R.id.tvLimit);

        // 获取历史记录
        players = new HistoryActivity().getPlayers(3);
        best = players == null ? 0 : players.get(1).getScore();

        // 创建得分监听
        cardLayout.setOnLayoutListener(this);
        this.onScoreChange(score);

        // 创建倒计时监听
        myTimer = new MyTimer();
        myTimer.setTimeChangeListener(this);
        // 设定倒计时
        myTimer.setMyCountdown(time_limit);
        limit_tv.setText(time_limit + "");

        // 获取倒计时播放
        mediaPlayer = MediaPlayer.create(CountDownModel.this, R.raw.tick);

        // 设置屏幕滑动事件，开始计时
        cardLayout.setOnTouchListener(this);


        back_btn = (Button) findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTimer.cancel();
                // 传递游戏信息给下一个活动
                Intent intent = new Intent("com.game_2048.menu");
                intent.putExtra("model", 1);
                intent.putExtra("time", time_rest);
//                intent.putExtra("Cards", (Serializable) cardLayout.getCards());
                startActivity(intent);
            }
        });
    }

    // 设置如果分数改变相应的事件，实现接口方法
    @Override
    public void onScoreChange(long score) {
        Log.i("TAG", "score_tv = " + score_tv);
        score_tv.setText("" + score);
        scoreChange = (int) (score - this.score);
        this.score = score;

        if (score > best) {
            best_tv.setText("" + score);
        } else {
            best_tv.setText("" + best);
        }
    }

    // 实现接口方法，游戏结束的判定
    @Override
    public void onGameOver(long score, boolean isTimeUp) {
        new DialogToOver(CountDownModel.this, score, isTimeUp, 3, cardLayout, players);
    }

    // 设置如果秒数发生改变相应的事件，实现接口方法
    @Override
    public double onSecChange(int sec) {
        if (sec <= 5) {
            // 时间不到五秒则播放倒计时音效
            mediaPlayer.start();
        }
        time_rest = sec;
        limit_tv.setText(sec + "");

        int add = scoreChange;
        scoreChange = 0;

        return  addSec(add);
    }

    private double addSec(long add) {
        double random = Math.random();
        double a;
        if (random < 0.5) {
            a = ((int) add) / 16;
        } else if (random < 0.75) {
            a = ((int) add) / 8;
        } else if (random < 0.875) {
            a = ((int) add) / 4;
        } else if (random < 0.935){
            a = ((int) add) / 2;
        } else {
            a = 0;
        }
        Log.i("ADD TIME", "Add " + String.format("%.3f", a > 7 ? 7 : a) + " s");
        if (add > 0) {
            Toast toast = Toast.makeText(this, "增加时间" + String.format("%.3f", a > 7 ? 7 : a) + "秒", Toast.LENGTH_SHORT/8);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }

        return a > 7 ? 7 : a;
    }

    // 重写 OnFinish 方法
    @Override
    public void onFinish() {
        myTimer.cancel();
        // 时间到了播放结束音效
        MediaPlayer.create(this, R.raw.finish).start();
        // 设置游戏结束方法
        this.onGameOver(score, true);
    }

    // 监听游戏页面第一次滑动，则开始游戏
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            myTimer.start();
        }
        return false;
    }
}
