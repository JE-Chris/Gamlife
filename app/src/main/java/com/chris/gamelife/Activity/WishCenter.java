package com.chris.gamelife.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.Model.Wish;
import com.chris.gamelife.R;
import com.chris.gamelife.util.DialogForWish;
import com.chris.gamelife.util.wishUtil;

import java.util.ArrayList;

public class WishCenter extends AppCompatActivity {
    public static String path;
    private TextView prompt_tv;
    private Button task_btn, achieve_btn, relax_btn, addWish_btn;
    private ListView wish_lv;
    private ArrayList<Wish> wishes = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String[] hopes;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_center);

        // 初始化文件地址及列表
        path = this.getDataDir().toString();
        wish_lv = (ListView) findViewById(R.id.wishes);
        prompt_tv = (TextView) findViewById(R.id.prompt);

        prompt_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogForWish().AddWish(WishCenter.this);
                prompt_tv.setVisibility(View.GONE);
            }
        });

        // 设置添加愿望事件
        addWish_btn = (Button) this.findViewById(R.id.add_wish);
        addWish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogForWish().AddWish(WishCenter.this);
                prompt_tv.setVisibility(View.GONE);
                wishes = new wishUtil().getWishes();
                showWisher();
            }
        });

        // 展示愿望清单
        wishes = new wishUtil().getWishes();
        Log.i("==Get==", "====wishes =  " + wishes + "====");
        if (wishes.isEmpty()) {
            prompt_tv.setVisibility(View.VISIBLE);
            wish_lv.setVisibility(View.INVISIBLE);
        }else {
            showWisher();
        }

        // 设置点击跳转事件
        task_btn = (Button) this.findViewById(R.id.task_center);
        task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WishCenter.this, TaskCenter.class));
            }
        });

        achieve_btn = (Button) this.findViewById(R.id.achievement);
        achieve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.game_life.achievement"));
            }
        });

        relax_btn = (Button) this.findViewById(R.id.relax);
        relax_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.game_life.relaxcenter"));
            }
        });
    }

    private void showWisher() {
        Log.i("==Get==", "=====Get wishes====");
        getHopes();
        adapter = new ArrayAdapter<>(WishCenter.this, R.layout.array_adapter, hopes);

        wish_lv.setAdapter(adapter);

        wish_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (wishes.isEmpty()) {
                    new DialogForWish().AddWish(WishCenter.this);
                }
                new DialogForWish().DialogItemClick(WishCenter.this, position);
            }
        });
    }

    private void getHopes() {
        int len = (wishes.isEmpty() ? 1 : wishes.size());
        this.hopes = new String[len];
        if (wishes.isEmpty()) {
            hopes[0] = "";
            Log.i("WISHES", "wishes is EMPTY!!!");
            return;
        } else {
            len = 0;
            for (Wish w : wishes) {
                this.hopes[len++] = len + "、  " + w.getWish() + "\t\t\t- " + w.getCost() + " 金币";
            }
        }
        Log.i("WISHES", "Wish:" + hopes[0]);
    }

    public String getPath() {
        return path;
    }
}
