package com.chris.gamelife.Model;

import java.io.Serializable;

public class Player implements Serializable, Comparable {
    private String name;
    private long score;
    private int order;

    public Player(String name, long score) {
        this(name, score, 0);
    }

    public Player(String name, long score, int order) {
        this.name = name;
        this.score = score;
        this.order = order;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setOrder(int o) {
        this.order = o;
    }

    public String getName() {
        return this.name;
    }

    public long getScore() {
        return this.score;
    }

    public int getOrder() {
        return this.order;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", order=" + order +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        Player p = (Player) o;
        if (this.getScore() >= p.getScore()) {
            return 1;
        } else {
            return 0;
        }
    }
}
