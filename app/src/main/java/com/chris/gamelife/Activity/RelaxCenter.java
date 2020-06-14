package com.chris.gamelife.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.R;

public class RelaxCenter extends AppCompatActivity {
    private Button task_btn, achieve_btn, wish_btn, game_2048, game_plane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relax_center);

        game_2048 = (Button) this.findViewById(R.id.game2048);
        game_2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RelaxCenter.this, MenuActivity.class));
            }
        });

        game_plane = (Button) this.findViewById(R.id.game_plane);
        game_plane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RelaxCenter.this, LoadActivity.class));
            }
        });

        // 设置点击跳转事件
        wish_btn = (Button) this.findViewById(R.id.wish_center);
        wish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.game_life.wishcenter"));
            }
        });

        task_btn = (Button) this.findViewById(R.id.task_center);
        task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RelaxCenter.this, TaskCenter.class));
            }
        });

        achieve_btn = (Button) this.findViewById(R.id.achievement);
        achieve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.game_life.achievement"));
            }
        });
    }
}
