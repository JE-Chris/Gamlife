package com.chris.gamelife.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.Model.Task;
import com.chris.gamelife.Model.Wish;
import com.chris.gamelife.R;
import com.chris.gamelife.util.DialogForTask;
import com.chris.gamelife.util.taskUtil;
import com.chris.gamelife.util.wishUtil;

import java.util.ArrayList;
import java.util.Random;

public class TaskCenter extends AppCompatActivity {
    // 检测三个下拉菜单是否下拉，展开状态为true，收起为 false
    private static boolean isMore_daily = false, isMore_main = false, isMore_side = false;
    private Button daily_btn, main_btn, side_btn, addTask_btn, badMood_btn;
    private ListView daily_lv, main_lv, side_lv;
    private ArrayList<Task> tasks_daily, tasks_main, tasks_side;
    private Button wish_btn, achieve_btn, relax_btn;
    private String[] daily, main, side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_center);

        // 设置badMood按钮
        Button badMood = (Button) this.findViewById(R.id.badMood);
        badMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badMoodClick();
            }
        });

        // 设置添加悬赏按钮
        Button addTask = (Button) this.findViewById(R.id.add_task);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogForTask().AddTask(TaskCenter.this);
            }
        });

        // 初始化内容
        daily_lv = (ListView) this.findViewById(R.id.daily_lv);
        main_lv = (ListView) this.findViewById(R.id.main_lv);
        side_lv = (ListView) this.findViewById(R.id.side_lv);

        // 初始化列表及文件地址并设置图标
        daily_btn = (Button) this.findViewById(R.id.daily);
        side_btn = (Button) this.findViewById(R.id.side);
        main_btn = (Button) this.findViewById(R.id.main);
        setIcon(daily_btn, main_btn, side_btn, daily_lv, main_lv, side_lv);
        // 在绑定了 TextView 之后再进行显示判定，否则会出现空指针异常
        ShowTasks(daily_lv, main_lv, side_lv);

        daily_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tasks_daily.isEmpty()) {
                    new DialogForTask().addTask(TaskCenter.this, 0);
                } else {
                    hideLists(0, isMore_daily);
                    showList(daily_btn, 0, tasks_daily.isEmpty());
                }
            }
        });

        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tasks_main.isEmpty()) {
                    new DialogForTask().addTask(TaskCenter.this, 1);
                } else {
                    hideLists(1, isMore_main);
                    showList(main_btn, 1, tasks_main.isEmpty());
                }
            }
        });

        side_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tasks_side.isEmpty()) {
                    new DialogForTask().addTask(TaskCenter.this, 2);
                } else {
                    hideLists(2, isMore_side);
                    showList(side_btn, 2, tasks_side.isEmpty());
                }
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

    private void ShowTasks(ListView daily_lv, ListView main_lv, ListView side_lv) {
        this.tasks_daily = new taskUtil().getTasks(0);
        this.tasks_main = new taskUtil().getTasks(1);
        this.tasks_side = new taskUtil().getTasks(2);
        showTasks(daily_lv, 0);
        showTasks(main_lv, 1);
        showTasks(side_lv, 2);
    }

    private void showTasks(ListView lv, final int model) {
        final ArrayList<Task> tasks = new taskUtil().getTasks(model);
        Log.i("GETTER", "get tasks of " + model + " : " + tasks);
        Button tv = getBtnView(model);

        if (tasks.isEmpty()) {
            tv.setText( (getBtnText(model) + "【点击添加】") );
            lv.setVisibility(View.GONE);
        } else {
            switch (model) {
                case 0:
                    tv.setText("    " + getBtnText(0) + "    ");
                    break;
                case 1:
                    tv.setText("    " + getBtnText(1) + "    ");
                    break;
                case 2:
                    tv.setText("    " + getBtnText(2) + "    ");
                    break;
                default:
                    break;
            }
        }

        String[] tasks_str = getStr(model);

        ArrayAdapter adapter = new ArrayAdapter<>(TaskCenter.this, R.layout.array_adapter, tasks_str);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tasks.isEmpty()) {
                    new DialogForTask().addTask(TaskCenter.this, model);
                }
                new DialogForTask().DialogItemClick(TaskCenter.this, position, model);
            }
        });
    }

    private String getBtnText(int model) {
        switch (model) {
            case 0:
                return "每日任务";
            case 1:
                return "主线任务";
            case 2:
                return "支线任务";
            default:
                return "";
        }
    }

    private Button getBtnView(int model) {
        switch (model) {
            case 0:
                return daily_btn;
            case 1:
                return main_btn;
            case 2:
                return side_btn;
            default:
                return null;
        }
    }

    private String[] getStr(int model) {
        ArrayList<Task> tasks = new taskUtil().getTasks(model);
        int len = tasks.isEmpty() ? 1 : tasks.size();
        String[] str = new String[len];

        if (tasks.isEmpty()) {
            str[0] = "还没有设置任务哦！";
        } else {
            len = 0;
            for (Task t : tasks) {
                str[len++] = len + "、  " + t.getTask() + "\t\t\t+ " + t.getGold() + " 金币";
            }
        }
        return str;
    }

    private void badMoodClick() {
        int w1, w2;
        ArrayList<Wish> wishes = new wishUtil().getWishes();

        if (wishes.size() < 2) {
            Toast toast = Toast.makeText(TaskCenter.this, "愿望还不够啊，赶紧添加满两个愿望吧！\n让我来满足你的小心愿吧！！！", Toast.LENGTH_SHORT * 2);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            startActivity(new Intent(TaskCenter.this, WishCenter.class));
            return;
        }

        Random rand = new Random();
        w1 = rand.nextInt(wishes.size());
        w2 = rand.nextInt(wishes.size());
        while (w2 == w1) {
            w2 = rand.nextInt(wishes.size());
        }

        wishes.get(w1).setCost(0);
        wishes.get(w2).setCost(0);

        {
            Toast toast = Toast.makeText(TaskCenter.this, "已经更改了你的第 " + (w1+1) + " 个和第 " + (w2+1) + " 个愿望，快去兑换它们吧！\n\n心情要快点好起来哦！\n期待快些与你的下一次见面！！！", Toast.LENGTH_LONG / 4 * 3);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            onPause();
        }

        new wishUtil().storageWishes(wishes);
        startActivity(new Intent(TaskCenter.this, WishCenter.class));
    }

    private void setIcon(Button daily_btn, Button main_btn, Button side_btn, ListView daily_lv, ListView main_lv, ListView side_lv) {
        Drawable drawable = getResources().getDrawable(R.mipmap.down);
        drawable.setBounds(0, 0, 50, 50);
        daily_btn.setCompoundDrawables(null, null, drawable, null);
        main_btn.setCompoundDrawables(null, null, drawable, null);
        side_btn.setCompoundDrawables(null, null, drawable, null);

        daily_lv.setVisibility(View.GONE);
        main_lv.setVisibility(View.GONE);
        side_lv.setVisibility(View.GONE);
    }

    private void showList(Button btn, final int model, boolean isEmpty) {
        switch (model) {
            case 1:
                changeVisibility(isMore_main, main_lv, isEmpty);
                isMore_main = exchangeIcon(isMore_main, btn);
                return;
            case 2:
                changeVisibility(isMore_side, side_lv, isEmpty);
                isMore_side = exchangeIcon(isMore_side, btn);
                return;
            default:
                changeVisibility(isMore_daily, daily_lv, isEmpty);
                isMore_daily = exchangeIcon(isMore_daily, btn);
                return;
        }
    }

    private void hideLists(int model, boolean isMore) {
        if (isMore) {
            switch (model) {
                case 1:
                    isMore_side = isMore_daily = false;
                    exchangeIcon(isMore_daily, daily_btn);
                    exchangeIcon(isMore_side, side_btn);
                    daily_lv.setVisibility(View.GONE);
                    side_lv.setVisibility(View.GONE);
                    break;
                case 2:
                    isMore_main = isMore_daily = false;
                    exchangeIcon(isMore_daily, daily_btn);
                    exchangeIcon(isMore_main, main_btn);
                    daily_lv.setVisibility(View.GONE);
                    main_lv.setVisibility(View.GONE);
                    break;
                default:
                    isMore_side = isMore_main = false;
                    exchangeIcon(isMore_main, main_btn);
                    exchangeIcon(isMore_side, side_btn);
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

    public boolean exchangeIcon(boolean isMore, Button btn) {
        Drawable drawable;

        if (isMore) {
            drawable = getResources().getDrawable(R.mipmap.up);
        } else {
            drawable = getResources().getDrawable(R.mipmap.down);
        }
        drawable.setBounds(0, 0, 50, 50);
        btn.setCompoundDrawables(null, null, drawable, null);

        return !isMore;
    }
}
