package com.chris.gamelife.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.Activity.HistoryActivity;
import com.chris.gamelife.Activity.MenuActivity;
import com.chris.gamelife.Model.CardLayout;
import com.chris.gamelife.Model.Player;
import com.chris.gamelife.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class DialogToOver {
    // 设置结束时候的弹窗
    private AppCompatActivity app;

    public DialogToOver(final AppCompatActivity app, long score, boolean isTimeUp, final int model,
                        final CardLayout cardLayout, final HashMap<Integer, Player> players) {
        final long s = score;
        this.app = app;

        AlertDialog.Builder gameOver = new AlertDialog.Builder(app);
        final View dialogView = LayoutInflater.from(app).inflate(R.layout.dialog_charts,null);
        gameOver.setTitle((isTimeUp) ? "Time Up" : "Game Over");
        gameOver.setView(dialogView);
        gameOver.setMessage("您的成绩是：" + s)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name_et = dialogView.findViewById(R.id.etName);
                        joinHistory(name_et.getText().toString(), s, players, model);
                        cardLayout.restart();
                        app.startActivity(new Intent(app, MenuActivity.class));
                    }
                }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name_et = dialogView.findViewById(R.id.etName);
                        joinHistory(name_et.getText().toString(), s, players, model);
                        app.finish();
                        app.startActivity(new Intent(app, MenuActivity.class));
                    }
                });

        // 设置对话框始终获取焦点
        AlertDialog alertDialog = gameOver.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    FileOutputStream fos;
    ObjectOutputStream oos;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void joinHistory(String name, Long s, HashMap<Integer, Player> players, int model) {
        Player p1 = new Player(name, s, (players != null ? players.size() + 1 : 1));
        players = addPlayer(p1, players);
        Log.i("INFOMATION", "players.get(1) = " + players.get(1).toString());

        try {
            File f = new File(new HistoryActivity().getHistoryFile(model));
            // File 在之前已经生成文件了，不用判断存在，创建序列化对象 oos，
            // player 必须实现 Serializable接口，以及实现CompareTo用于比较对象
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(players);

            oos.flush();

            HashMap<Integer, Player> p = new HistoryActivity().getPlayers(2);
            Log.i("RESULT", "resule: name = " + p.get(1).getName() + "\t score = " + p.get(1).getScore());

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null)
                    oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private HashMap<Integer, Player> addPlayer(Player p, HashMap<Integer, Player> players) {
        if (players == null) {
            players = new HashMap<>();
            players.put(1, p);
            return players;
        } else {
            players.put(players.size() + 1, p);
        }

        players = AddPlayer(players);

        Log.i("TAG", "==== 插入数据成功 ====");
        return players;
    }

    private HashMap<Integer, Player> AddPlayer(HashMap<Integer, Player> players) {
        Player player = players.get(players.size());

        for (int i = players.size()-1; i >= 1; i--) {
            Player p = players.get(i);
            if (player.compareTo(p) > 0) {
                break;
            }
            // 如果player的成绩不小于p，就将p移到player的位置，并且改变对应的Order值
            p.setOrder(player.getOrder());
            players.put(player.getOrder(), p);
            player.setOrder(i);
            players.put(i, player);
        }

        return players;
    }
}
