package com.chris.gamelife.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.R;

public class ModelSelect extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_select);

        Button classic_btn = findViewById(R.id.classic);
        Button limit_btn = findViewById(R.id.limit);
        Button countdown_btn = findViewById(R.id.countdown);

        classic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.game_2048.menu");
                intent.putExtra("diff", 1);
                startActivity(intent);
            }
        });

        limit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.game_2048.menu");
                intent.putExtra("diff", 2);
                startActivity(intent);
            }
        });

        countdown_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.game_2048.menu");
                intent.putExtra("diff", 3);
                startActivity(intent);
            }
        });
    }
}
