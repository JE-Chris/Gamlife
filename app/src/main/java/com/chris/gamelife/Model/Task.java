package com.chris.gamelife.Model;

import java.io.Serializable;

public class Task implements Serializable {
    private String task;
    private int gold;

    public Task(String task, int gold) {
        this.task = task;
        this.gold = gold;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    @Override
    public String toString() {
        return "Task{" +
                "task='" + task + '\'' +
                ", goal=" + gold +
                '}';
    }
}
