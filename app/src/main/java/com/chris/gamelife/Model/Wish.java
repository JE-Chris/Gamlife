package com.chris.gamelife.Model;

import java.io.Serializable;

public class Wish implements Serializable {
    private String wish;
    private int cost;

    public Wish(String wish, int cost) {
        this.wish = wish;
        this.cost = cost;
    }

    public void setWish(String wish) {
        this.wish = wish;
}

    public void setCost(int cost) {
        this.cost = cost;
}

    public String getWish() {
        return wish;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Wish{" +
                "wish='" + wish + '\'' +
                ", cost=" + cost +
                '}';
    }
}
