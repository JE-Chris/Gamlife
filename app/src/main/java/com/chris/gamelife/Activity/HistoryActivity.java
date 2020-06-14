package com.chris.gamelife.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.Model.Player;
import com.chris.gamelife.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.N)
public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private int flag = 1;
    private HashMap<Integer, Player> players = getPlayers(1);
    private String[] info = new String[1];
    private ListView info_lv;
    private Button classic_btn, limit_btn, countdown_btn;
    private ArrayAdapter<String> adapter_info;
    // 初始化文件地址
    public static String path = new MenuActivity().getPath();

    static {
        try {
            HashMap<Integer, Player> players = new HashMap<>();
            File f = new File(path);
            ObjectOutputStream oos;
            if (!f.exists()) {
                f.mkdirs();
            }

            f = new File(path + "/classic_score");
            if (!f.exists()) {
                f.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(players);
                oos.close();
            }

            f = new File(path + "/limit_score");
            if (!f.exists()) {
                f.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(players);
                oos.close();
            }

            f = new File(path + "/countdown_score");
            if (!f.exists()) {
                f.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(players);
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        classic_btn = findViewById(R.id.his_cla);
        limit_btn = findViewById(R.id.his_lim);
        countdown_btn = findViewById(R.id.his_cnd);
        flash();

        classic_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                classic_btn.setAlpha(0.6f);
                limit_btn.setAlpha(1);
                countdown_btn.setAlpha(1);

                players = null;
                flag = 1;
                players = getPlayers(flag);
                flash();
            }
        });

        limit_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                classic_btn.setAlpha(1);
                limit_btn.setAlpha(0.6f);
                countdown_btn.setAlpha(1);

                players = null;
                flag = 2;
                players = getPlayers(flag);
                flash();
            }
        });

        countdown_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                classic_btn.setAlpha(1);
                limit_btn.setAlpha(1);
                countdown_btn.setAlpha(0.6f);

                players = null;
                flag = 3;
                players = getPlayers(flag);
                flash();
            }
        });

        Button back_btn = (Button) findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void moveItems(int position) {
        Log.i("DEBUG", "position = " + position + "\nplayer.size = " + players.size());
        if (position + 1 < players.size()) {    // 说明不是最后一个
            for (int i = position + 1; i < players.size(); i++) {
                players.get(i).setOrder(i-1);
                players.put(i - 1, players.get(i));
            }
        } else {
            players.remove(position);
        }
    }

    private void flash() {
        getInfo(flag);

        Log.i("Info", "player = " + players + "\ninfo[0] = " + info[0]);

        adapter_info = new ArrayAdapter<String>(HistoryActivity.this, R.layout.array_adapter, info);

        info_lv = (ListView) findViewById(R.id.info);

        info_lv.setAdapter(adapter_info);

        info_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HistoryActivity.this, "删除玩家信息：" + players.get(position-1), Toast.LENGTH_SHORT).show();
                moveItems(position);
                flash();
                return true;
            }
        });

        info_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("==INFO==", "position = " + position);
                Toast.makeText(HistoryActivity.this, "name: " + players.get(position+1).getName()
                        + "\nScore: " + players.get(position+1).getScore()
                        + "\nOrder: " + players.get(position+1).getOrder(), Toast.LENGTH_SHORT).show();
                flash();
            }
        });

        info_lv = null;
        info = new String[1];
    }

    private void getInfo(int flag) {
        String words;
        switch (flag) {
            case 2:
                words = "----限时模式";
                break;
            case 3:
                words = "----倒计时模式";
                break;
            default:
                words = "----经典模式";
                break;
        }

        if (players == null) {
            info[0] = "没有记录开始挑战吧！\n" + words;
            return;
        }

        info = new String[players.size()];

        Log.i("DEBUG", "players = " + players);
        for (int i = 1; i <= players.size(); i++) {
            info[i - 1] = players.get(i).getOrder() + "\t\t"
                    + players.get(i).getName() + "\t\t\t\t"
                    + players.get(i).getScore();
        }
    }

    private File file;
    private FileInputStream fis = null;
    private ObjectInputStream ois = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HashMap<Integer, Player> getPlayers(int model) {
        Log.i("INFO", "In <getPlayers>");
        file = new File(getHistoryFile(model));
        Log.i("PATH", file.getAbsolutePath());

        Log.i("FILE","File path: " + file.getAbsolutePath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            players = (HashMap <Integer, Player>) ois.readObject();

            // 如果在历史记录中没有记录，则返回控制，用于避免空指针异常
            if (players == null) {
                return null;
            }

            // 释放资源
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return players;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getHistoryFile(int model) {
        String dataPath;
        switch (model) {
            case 1:
                dataPath = path + "/classic_score";
                break;
            case 2:
                dataPath = path + "/limit_score";
                break;
            case 3:
                dataPath = path + "/countdown_score";
                break;
            default:
                return path + "/classic_score";
        }
        return dataPath;
    }

    public long getHighest() {
        return players.get(1).getScore();
    }

    @Override
    public void onClick(View v) {
        getInfo(flag);
    }
}
