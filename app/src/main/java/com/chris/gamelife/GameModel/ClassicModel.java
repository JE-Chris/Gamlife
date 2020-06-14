package com.chris.gamelife.GameModel;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.Activity.HistoryActivity;
import com.chris.gamelife.Model.Card;

import com.chris.gamelife.Model.CardLayout;
import com.chris.gamelife.Model.Player;
import com.chris.gamelife.util.DialogToOver;
import com.chris.gamelife.R;

import java.util.HashMap;
import java.util.List;

public class ClassicModel extends AppCompatActivity implements CardLayout.OnLayoutListener {
    private CardLayout cardLayout;

    private TextView score_tv, best_tv;
    private Button back_btn;
    private long score = 0, best;

    private HashMap<Integer, Player> players = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_classic);

        cardLayout = (CardLayout) findViewById(R.id.game_classic);
        final Intent tent = getIntent();  // 用于获取上一个活动传递的信息
        if (tent != null) {
            cardLayout.setCards ( (List<Card>) tent.getSerializableExtra("layout") );
        }

        score_tv = (TextView) findViewById(R.id.tvScore);
        best_tv = (TextView) findViewById(R.id.tvBest);

        Log.i("INFO", "Get players");
        players = new HistoryActivity().getPlayers(1);
        best = (players == null) ? 0 : players.get(1).getScore();

        cardLayout.setOnLayoutListener(this);
        onScoreChange(score);

        back_btn = (Button) findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.game_2048.menu");
                intent.putExtra("model", 1);
                intent.putExtra("time", 0);
//                intent.putExtra("Cards", (Serializable) cardLayout.getCards());
                startActivity(intent);
            }
        });
    }

    private CardLayout getCardLayout() {
        return this.cardLayout;
    }

    @Override
    public void onScoreChange(long score) {
        // 分数改变时的处理，实现接口 LayoutListener
        score_tv.setText("" + score);
        if (best < score) {
            best_tv.setText("" + score);
        } else {
            best_tv.setText("" + best);
        }
    }

    @Override
    public void onGameOver(long score, boolean isTimeUp) {
        MediaPlayer.create(this, R.raw.over).start();
        new DialogToOver(ClassicModel.this, score, isTimeUp, 1, cardLayout, players);
    }
}