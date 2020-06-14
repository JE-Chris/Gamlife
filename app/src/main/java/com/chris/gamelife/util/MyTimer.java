package com.chris.gamelife.util;

import android.os.CountDownTimer;
import android.util.Log;

public class MyTimer {
    private MyCountDownTimer mc;
    private boolean isStart = false;

    /**
     * 设置默认构造
     * @param millisInFuture
     * @param countDownInterval
     */
    public void setMyCountdown(long millisInFuture, long countDownInterval) {
        if (mc == null) {
            mc = new MyCountDownTimer(millisInFuture, countDownInterval);
        }
    }

    public void setMyCountdown(int sec) {
        if (mc == null) {
            mc = new MyCountDownTimer(sec * 1000, 450);
        }
    }

    public void start() {
        if (!isStart) {
            isStart = true;
            mc.start();
        }
    }

    public void cancel() {
        if (isStart) {
            isStart = false;
            mc.cancel();
        }
    }

    public interface OnTimeChangeListener {
        double onSecChange(int sec);
        void onFinish();
    }

    private OnTimeChangeListener timeChangeListener;

    public void setTimeChangeListener(OnTimeChangeListener timeChangeListener) {
        this.timeChangeListener = timeChangeListener;
    }

    private class MyCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            double add = timeChangeListener.onSecChange((int) (millisUntilFinished / 1000));
            if (add != 0) {
                AddTimer(add, millisUntilFinished);
                add = 0;
            }
        }

        @Override
        public void onFinish() {
            timeChangeListener.onFinish();
        }
    }

    private void AddTimer(double add, long mill) {
        mill += (add * 1000);
        Log.i("TAG", "====增加" + add + "秒====");

        add = 0;
        mc.cancel();
        mc = new MyCountDownTimer(mill, 450);
        mc.start();
    }
}
