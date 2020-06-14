package com.chris.gamelife.util;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chris.gamelife.Model.Task;
import com.chris.gamelife.Model.User;
import com.chris.gamelife.Model.Wish;

public class ItemClick {
    private static User user;

    public void WishItemClick(AppCompatActivity app, final Wish wish) {
        AlertDialog.Builder click = new AlertDialog.Builder(app);
        click.setTitle("奖品兑换")
                .setMessage("是否花费 " + wish.getCost() + " 个金币兑换愿望‘" + wish.getWish() + "’")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user = new userUtil().getUser();
                        user.setGold(user.getGold() - wish.getCost());
                        new userUtil().storageUser(user);
                        // 存储已经实现的愿望
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).show();
    }

    public void TaskItemClick(AppCompatActivity app, final Task task) {
        AlertDialog.Builder click = new AlertDialog.Builder(app);
        click.setTitle("提交任务")
                .setMessage("是否已经完成任务‘" + task.getTask() + "’[可获得佣金 " + task.getGold() + " ]")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user = new userUtil().getUser();
                        user.setGold(user.getGold() + task.getGold());
                        new userUtil().storageUser(user);
                        // 存储已经实现的愿望
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).show();
    }
}
