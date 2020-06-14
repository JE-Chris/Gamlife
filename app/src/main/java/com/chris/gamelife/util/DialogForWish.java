package com.chris.gamelife.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.Activity.WishCenter;
import com.chris.gamelife.Model.User;
import com.chris.gamelife.Model.Wish;
import com.chris.gamelife.R;

public class DialogForWish {
    private static AppCompatActivity app;
    private Wish wish;
    private static User user;

    public void AddWish(final AppCompatActivity app) {
        this.app = app;

        final AlertDialog.Builder add = new AlertDialog.Builder(app);
        final View view = LayoutInflater.from(app).inflate(R.layout.add_wish, null);
        add.setTitle("添加愿望");
        add.setView(view);
        add.setMessage("请按照提示输入愿望及对应的花费")
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText wish_et = view.findViewById(R.id.etWish);
                        EditText goal_et = view.findViewById(R.id.etGoal);

                        if (!isStrToInt(goal_et.getText().toString())) {
                            Toast.makeText(app, "愿望添加失败，请确定输入的悬赏为数字。", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int goal = Integer.valueOf(goal_et.getText().toString());
                        String wish = wish_et.getText().toString();

                        if (wish.equals("")) {
                            Toast.makeText(app, "愿望添加失败，请输入愿望内容。", Toast.LENGTH_SHORT).show();
                        } else if (goal <= 0) {
                            Toast.makeText(app, "愿望添加失败，请保证愿望花销大于 0", Toast.LENGTH_SHORT).show();
                        } else {
                            new wishUtil().addWish(wish, goal);
                            Toast.makeText(app, "愿望添加成功", Toast.LENGTH_SHORT).show();
                        }

                        app.startActivity(new Intent(app, WishCenter.class));
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(app, "已取消添加愿望", Toast.LENGTH_SHORT).show();

                // 如果 wishes 为空，需要保证第一行的 TextView 存在
                if (new wishUtil().getWishes().isEmpty())
                    app.startActivity(new Intent(app, WishCenter.class));
            }
        });

        AlertDialog focus = add.create();
        focus.setCanceledOnTouchOutside(false);
        focus.show();
    }

    int choice = 0;
    public void DialogItemClick(final AppCompatActivity app, final int position) {
        // 获取当前愿望
        this.wish = new wishUtil().getWishes().get(position);

        AlertDialog.Builder select = new AlertDialog.Builder(app);
        select.setTitle("选项");
        select.setMessage("请点击需要的操作。")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogDelete(app, position);
                    }
                }).setNeutralButton("编辑", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogEdit(app, position);
            }
        }).setNegativeButton("兑换", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogExchange(app, position);
            }
        }).show();
    }

    private void  DialogEdit(final AppCompatActivity app, final int position) {
        AlertDialog.Builder detail = new AlertDialog.Builder(app);
        this.wish = new wishUtil().getWishes().get(position);
        final View view = LayoutInflater.from(app).inflate(R.layout.edit_wish, null);

        detail.setView(view);
        detail.setTitle("编辑")
                .setMessage("原愿望内容：" + wish.getWish() + "\n费用：" + wish.getCost() + "\n请根据提示输入信息：")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText etWish = view.findViewById(R.id.editWish);
                        EditText etGoal = view.findViewById(R.id.editGoal);

                        if (!isStrToInt(etGoal.getText().toString())) {
                            Toast.makeText(app, "愿望添加失败，请确定输入的费用为数字。", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int goal = Integer.valueOf(etGoal.getText().toString());
                        String wish = etWish.getText().toString();

                        if (wish.equals("")) {
                            Toast.makeText(app, "愿望编辑失败，请输入愿望内容。", Toast.LENGTH_SHORT).show();
                        } else if (goal <= 0) {
                            Toast.makeText(app, "愿望编辑失败，请保证愿望费用大于 0", Toast.LENGTH_SHORT).show();
                        } else {
                            new wishUtil().replaceWish(new Wish(wish, goal), position);
                            Toast.makeText(app, "愿望编辑成功", Toast.LENGTH_SHORT).show();
                        }

                        app.startActivity(new Intent(app, WishCenter.class));
                    }
                }).show();
    }

    private void DialogDelete(final AppCompatActivity app, final int position) {
        AlertDialog.Builder delete = new AlertDialog.Builder(app);
        delete.setTitle("删除愿望")
                .setMessage("是否删除本愿望？\n\'" + wish.getWish() + "\'")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new wishUtil().deleteWish(position);

                        app.startActivity(new Intent(app, WishCenter.class));
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogItemClick(app, position);
            }
        }).show();
    }

    private void DialogExchange(final AppCompatActivity app, final int position) {
        user = new userUtil().getUser();
        AlertDialog.Builder exchange = new AlertDialog.Builder(app);
        exchange.setTitle("兑换愿望")
                .setMessage("您的金币余额为" + user.getGold() + "\n\n是否确定花费 " + wish.getCost() + " 个金币兑换本愿望(\'" + wish.getWish() + "\')?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (user.getGold() >= wish.getCost()) {
                            user.addGold(-wish.getCost());
                            user.addTotal_use(wish.getCost());
                            user.addCount_wish();
                            new userUtil().storageUser(user);

                            Toast toast = Toast.makeText(app, "兑换成功，您的金币余额为：" + user.getGold(), Toast.LENGTH_LONG/2);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            new wishUtil().deleteWish(position);
                            app.startActivity(new Intent(app, WishCenter.class));
                        } else {
                            Toast toast = Toast.makeText(app, "抱歉，您的余额不足，不能兑换此愿望。", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogItemClick(app, position);
            }
        }).show();
    }

    public static boolean isStrToInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
