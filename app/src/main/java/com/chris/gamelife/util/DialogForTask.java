package com.chris.gamelife.util;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.chris.gamelife.Activity.TaskCenter;
import com.chris.gamelife.Model.Task;
import com.chris.gamelife.Model.User;
import com.chris.gamelife.R;

public class DialogForTask {
    private static User user;
    private Task task;
    int choice = 0;

    public void AddTask(final TaskCenter app) {
        final AlertDialog.Builder add = new AlertDialog.Builder(app);
        // 设置选项
        String[] tasks = {"每日任务", "主线任务", "支线任务"};
        add.setTitle("选择任务类型");
        add.setSingleChoiceItems(tasks, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = which;
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addTask(app, choice);
            }
        }).show();
    }

    // 设置添加的主题内容
    public void addTask(final TaskCenter app, final int choice) {
        final AlertDialog.Builder add = new AlertDialog.Builder(app);
        final View view = LayoutInflater.from(app).inflate(R.layout.add_task, null);
        add.setTitle("添加" + getTitle(choice) + "悬赏");
        add.setView(view);
        add.setMessage("请根据题示输入相应的信息")
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText task_et = view.findViewById(R.id.etTask);
                        EditText xs_et = view.findViewById(R.id.etXs);

                        if (!DialogForWish.isStrToInt(xs_et.getText().toString())) {
                            Toast.makeText(app, "悬赏添加失败，请输入数字花销。", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int goal = Integer.valueOf(xs_et.getText().toString());
                        String task = task_et.getText().toString();

                        if (task.equals("")) {
                            Toast.makeText(app, "悬赏添加失败，请输入悬赏内容。", Toast.LENGTH_SHORT).show();
                        } else if (goal < 0) {
                            Toast.makeText(app, "悬赏添加失败，请保证愿望花销不小于 0", Toast.LENGTH_SHORT).show();
                        } else {
                            new taskUtil().addTask(task, goal, choice);
                            Toast.makeText(app, "悬赏添加成功", Toast.LENGTH_SHORT).show();
                            app.startActivity(new Intent(app, TaskCenter.class));
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(app, "已取消添加悬赏。", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置始终获取焦点
        AlertDialog focus = add.create();
        focus.setCanceledOnTouchOutside(false);
        focus.show();
    }

    private String getTitle(int choice) {
        switch (choice) {
            case 1:
                return "主线任务";
            case 2:
                return "支线任务";
            default:
                return "每日任务";
        }
    }

    public void DialogItemClick(final TaskCenter app, final int position, final int model) {
        // 获取当前愿望
        task = new taskUtil().getTasks(model).get(position);

        android.app.AlertDialog.Builder select = new android.app.AlertDialog.Builder(app);
        select.setTitle("选项");
        select.setMessage("请点击需要的操作。")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogDelete(app, position, model);
                    }
                }).setNeutralButton("编辑", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogEdit(app, position, model);
            }
        }).setNegativeButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogComplete(app, position, model);
            }
        }).show();
    }

    private void DialogComplete(final TaskCenter app, final int position, final int model) {
        user = new userUtil().getUser();
        android.app.AlertDialog.Builder exchange = new android.app.AlertDialog.Builder(app);
        exchange.setTitle("完成悬赏")
                .setMessage("是否确认完成本悬赏任务(\'" + task.getTask() + "\')?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.addGold(task.getGold());
                        user.addCount_task();
                        user.addTotal_get(task.getGold());
                        new userUtil().storageUser(user);

                        Toast toast = Toast.makeText(app, "兑换成功，您的金币余额为：" + user.getGold(), Toast.LENGTH_LONG/2);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        new taskUtil().deleteTask(position, model);
                        app.startActivity(new Intent(app, TaskCenter.class));
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogItemClick(app, position, model);
            }
        }).show();
    }

    private void DialogEdit(final TaskCenter app, final int position, final int model) {
        android.app.AlertDialog.Builder detail = new android.app.AlertDialog.Builder(app);
        final View view = LayoutInflater.from(app).inflate(R.layout.edit_task, null);
        detail.setView(view);
        detail.setTitle("更改悬赏")
                .setMessage("原悬赏内容：" + task.getTask() + "\n金币：" + task.getGold() + "\n请根据提示输入信息：")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText etTask = view.findViewById(R.id.editTask);
                        EditText etXs = view.findViewById(R.id.editXs);

                        if (!DialogForWish.isStrToInt(etXs.getText().toString())) {
                            Toast.makeText(app, "愿望添加失败，请确定输入的悬赏金币为数字。", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int xs = Integer.valueOf(etXs.getText().toString());
                        String task = etTask.getText().toString();

                        if (task.equals("")) {
                            Toast.makeText(app, "悬赏编辑失败，请输入悬赏内容。", Toast.LENGTH_SHORT).show();
                        } else if (xs <= 0) {
                            Toast.makeText(app, "悬赏编辑失败，请保证愿望悬赏金币大于 0", Toast.LENGTH_SHORT).show();
                        } else {
                            new taskUtil().replaceTask(new Task(task, xs), position, model);
                            Toast.makeText(app, "愿望编辑成功", Toast.LENGTH_SHORT).show();
                        }

                        app.startActivity(new Intent(app, TaskCenter.class));
                    }
                }).show();
    }

    private void DialogDelete(final TaskCenter app, final int position, final int model) {
        android.app.AlertDialog.Builder delete = new android.app.AlertDialog.Builder(app);
        delete.setTitle("删除愿望")
                .setMessage("是否删除本愿望？\n\'" + task.getTask() + "\'")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new taskUtil().deleteTask(position, model);

                        app.startActivity(new Intent(app, TaskCenter.class));
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogItemClick(app, position, model);
            }
        }).show();
    }
}
