package com.chris.gamelife.GameModel;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
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
import com.chris.gamelife.util.DialogToOver;
import com.chris.gamelife.util.MyTimer;
import com.chris.gamelife.R;

import java.util.HashMap;
import java.util.List;

public class LimitModel extends AppCompatActivity implements CardLayout.OnLayoutListener, MyTimer.OnTimeChangeListener, View.OnTouchListener {
    private CardLayout cardLayout;
    private MyTimer myTimer;
    private int time_limit = 10, distance = 8, scoreChange = 0, time_rest = time_limit;
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
        setContentView(R.layout.game_limit);

        // 绑定相应的按键、view
        cardLayout = (CardLayout) findViewById(R.id.game_limit);// 获取传递的信息
        final Intent tent = getIntent();  // 用于获取上一个活动传递的信息
        if (tent != null) {
            cardLayout.setCards ( (List<Card>) tent.getSerializableExtra("layout") );
            time_limit = tent.getIntExtra("time", 30);
        }

        cardLayout = (CardLayout) findViewById(R.id.game_limit);
        score_tv = (TextView) findViewById(R.id.tvScore);
        best_tv = (TextView) findViewById(R.id.tvBest);
        limit_tv = (TextView) findViewById(R.id.tvLimit);

        // 获取历史记录
        players = new HistoryActivity().getPlayers(2);
        best = players == null ? 0 : players.get(1).getScore();

        // 创建得分监听
        cardLayout.setOnLayoutListener(this);
        onScoreChange(score);

        // 创建倒计时监听
        myTimer = new MyTimer();
        myTimer.setTimeChangeListener(this);
        // 设定倒计时
        myTimer.setMyCountdown(time_limit);
        limit_tv.setText(time_limit + "");

        // 获取倒计时播放
        mediaPlayer = MediaPlayer.create(this, R.raw.tick);

        // 设置屏幕滑动事件，开始计时
        cardLayout.setOnTouchListener(this);


        back_btn = (Button) findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTimer.cancel();
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
        myTimer.cancel();
        new DialogToOver(LimitModel.this, score, isTimeUp, 2, cardLayout, players);
    }

    // 设置如果秒数发生改变相应的事件，实现接口方法
    @Override
    public double onSecChange(int sec) {
        if (sec <= 5) {
            // 时间不到五秒则播放倒计时音效
            mediaPlayer.start();
        }
        // 记录剩余的时间
        time_rest = sec;
        limit_tv.setText(sec + "");

        // 如果最大数改变了，则增加5秒时间，并用Toast显示
        if (scoreChange >= distance) {
            toast = Toast.makeText(LimitModel.this, "改变最大数\n增加五秒", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

            distance *= 2;
            return 5;
        }
        return  0;
    }

    // 重写 OnFinish 方法
    @Override
    public void onFinish() {
        // 时间到了播放结束音效
        MediaPlayer.create(this, R.raw.finish).start();
        // 设置游戏结束方法
        this.onGameOver(score, true);
    }

    // 监听游戏页面第一次滑动，开始倒计时
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            myTimer.start();
        }
        return false;
    }
}
