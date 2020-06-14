package com.chris.gamelife.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.Model.User;
import com.chris.gamelife.R;
import com.chris.gamelife.util.userUtil;

public class Achievement extends AppCompatActivity {
    // 检测三个下拉菜单是否下拉，展开状态为true，收起为 false
    private boolean isMore_daily = false, isMore_main = false, isMore_side = false;
    private TextView daily_tv, main_tv, side_tv, gold_tv;
    private ListView daily_lv, main_lv, side_lv;
    private Button wish_btn, task_btn, relax_btn;
    Intent intent;
    private static User user = new userUtil().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement);

        gold_tv = findViewById(R.id.gold);
        gold_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(Achievement.this, "记录\n总共完成悬赏：" + user.getCount_task()
                        + "次\n总共兑换愿望：" + user.getCount_wish()
                        + "次\n总共获得金币：" + user.getTotal_get()
                        + "个\n总共使用金币：" + user.getTotal_use() + "个", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        showGold();
        daily_tv = (TextView) findViewById(R.id.daily_achieve);
        side_tv = (TextView) findViewById(R.id.side_achieve);
        main_tv = (TextView) findViewById(R.id.main_achieve);
        setIcon(daily_tv, main_tv, side_tv);

        // 设置点击跳转事件
        wish_btn = (Button) this.findViewById(R.id.wish_center);
        wish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent("com.game_life.wishcenter");
                startActivity(intent);
            }
        });

        task_btn = (Button) this.findViewById(R.id.task_center);
        task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent("com.game_life.taskcenter");
                startActivity(intent);
            }
        });

        relax_btn = (Button) this.findViewById(R.id.relax);
        relax_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent("com.game_life.relaxcenter");
                startActivity(intent);
            }
        });
    }

    private void showGold() {
        if (user == null) {
            user = new User(10);
            new userUtil().storageUser(user);
        }
        gold_tv.setText("我的金币：" + user.getGold());
    }

    private void hideLists(int model, boolean isMore) {
        if (isMore) {
            switch (model) {
                case 1:
                    isMore_side = isMore_daily = false;
                    exchangeIcon(isMore_daily, daily_tv);
                    exchangeIcon(isMore_side, side_tv);
                    daily_lv.setVisibility(View.GONE);
                    side_lv.setVisibility(View.GONE);
                    break;
                case 2:
                    isMore_main = isMore_daily = false;
                    exchangeIcon(isMore_daily, daily_tv);
                    exchangeIcon(isMore_main, main_tv);
                    daily_lv.setVisibility(View.GONE);
                    main_lv.setVisibility(View.GONE);
                    break;
                default:
                    isMore_side = isMore_main = false;
                    exchangeIcon(isMore_main, main_tv);
                    exchangeIcon(isMore_side, side_tv);
                    main_lv.setVisibility(View.GONE);
                    side_lv.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void changeVisibility(boolean isMore, ListView lv, boolean isEmpty) {
        if (isMore && !isEmpty) {
            lv.setVisibility(View.VISIBLE);
        } else {
            lv.setVisibility(View.GONE);
        }
    }

    public boolean exchangeIcon(boolean isMore, TextView tv) {
        Drawable drawable;

        if (isMore) {
            drawable = getResources().getDrawable(R.mipmap.up);
        } else {
            drawable = getResources().getDrawable(R.mipmap.down);
        }
        drawable.setBounds(0, 0, 50, 50);
        tv.setCompoundDrawables(null, null, drawable, null);

        return !isMore;
    }

    private void setIcon(TextView daily_tv, TextView main_tv, TextView side_tv) {
        Log.i("INFO_ICON", "daily_tv = " + daily_tv + "\nside_tv = " + side_tv + "\nmain_tv = " + main_tv);
        Drawable drawable = getResources().getDrawable(R.mipmap.down);
        drawable.setBounds(0, 0, 50, 50);
        daily_tv.setCompoundDrawables(null, null, drawable, null);
        main_tv.setCompoundDrawables(null, null, drawable, null);
        side_tv.setCompoundDrawables(null, null, drawable, null);
    }
}
