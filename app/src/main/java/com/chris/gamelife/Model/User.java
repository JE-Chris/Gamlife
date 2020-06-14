package com.chris.gamelife.Model;

import java.io.Serializable;

public class User implements Serializable {
    private int gold;
    private int count_task, count_wish, total_get, total_use;

    public int getCount_task() {
        return count_task;
    }

    public void addCount_task() {
        this.count_task++;
    }

    public int getCount_wish() {
        return count_wish;
    }

    public void addCount_wish() {
        this.count_wish++;
    }

    public int getTotal_get() {
        return total_get;
    }

    public void addTotal_get(int gold) {
        this.total_get += gold;
    }

    public int getTotal_use() {
        return total_use;
    }

    public void addTotal_use(int gold) {
        this.total_use += gold;
    }

    public User(int gold) {
        this.gold = gold;
        this.count_task = this.total_get = this.count_wish = this.total_use = 0;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    // 实现扣除和添加金币
    public void addGold(int goal) {
        this.gold += goal;
    }

    @Override
    public String toString() {
        return "User{" +
                "goal=" + gold +
                '}';
    }
}
