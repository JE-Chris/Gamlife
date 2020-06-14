package com.chris.gamelife.Model;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.chris.gamelife.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardLayout extends RelativeLayout {
    // 设置 Card 的数量，默认 4*4
    private int column = 4;
    // 存放所有的 Card
    private List<Card> cards;
    // 游戏框的横纵向边距
    private int margin = 10;
    // 游戏面板的 padding 值
    private int padding;
    // 检测用户滑动手势
    private GestureDetector gestureDetector;
    // 确认是否合并
    private boolean isMergeHappen = true;
    // 判断是否移动
    private boolean isMoveHappen = true;
    // 记录分数
    private long score = 0;
    // 播放背景音
    MediaPlayer mediaPlayer;

    private OnLayoutListener layoutListener;

    // 提供接口，改变显示的 score
    public interface OnLayoutListener {

        void onScoreChange(long score);

        void onGameOver(long score, boolean isTimeUp);
    }

    public void setOnLayoutListener(OnLayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    // 运动方向枚举
    private enum ACTION {
        LEFT, RIGHT, UP, DOWM;
    }


    public CardLayout(Context context) {
        this(context, null);
    }

    public CardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                margin, getResources().getDisplayMetrics());
        // 设置Layout的内边距，四边一致，设置为四内边距中的最小值
        padding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                getPaddingBottom());
        mediaPlayer = MediaPlayer.create(context, R.raw.move);

        gestureDetector = new GestureDetector(context , new MyGestureDetector());
    }

    // 根据用户运动，整体进行 移动、合并值 等操作
    private void action(ACTION action) {
        // 行|列
        for (int i = 0; i < column; i++) {
            List<Card> row = new ArrayList<>();
            // 行|列
            // 记录不为零的数
            for (int j = 0; j < column; j++) {
                // 获得下标
                int index = getIndexByAction(action, i, j);
                Card card = cards.get(index);
                // 记录不为零的数
                if (card.getNum() != 0) {
                    row.add(card);
                }
            }

            // 判断是否发生移动
            for (int j = 0; j < column &&  j < row.size(); j++) {
                int index = getIndexByAction(action, i, j);
                Card card = cards.get(index);

                if (card.getNum() != row.get(j).getNum()) {
                    isMoveHappen = true;
                }
            }

            // 合并相同的
            mergeCard(row);

            // 设置合并后的值
            for (int j = 0; j < column; j++) {
                int index = getIndexByAction(action, i, j);
                if (row.size() > j) {
                    cards.get(index).setNum(row.get(j).getNum());
                } else {
                    cards.get(index).setNum(0);
                }
            }
        }
        // 生成数字
        generateNum();
    }

    // 根据 Action, i, j 得到下标
    private int getIndexByAction(ACTION action, int x, int y) {
        int index = -1;

        switch (action) {
            case LEFT:
                index = x * column + y;
                break;
            case RIGHT:
                index = x * column + column - y - 1;
                break;
            case UP:
                index = x + y * column;
                break;
            case DOWM:
                index = x + (column - y - 1) * column;
        }

        return index;
    }

    // 合并相同的 Card
    private void mergeCard(List<Card> row) {
        if (row.size() < 2) return;

        for (int i = 0; i < row.size() - 1; i++) {
            Card card1 = row.get(i);
            Card card2 = row.get(i + 1);

            if (card2.getNum() == card1.getNum()) {
                isMergeHappen = true;

                int val = card1.getNum() + card2.getNum();
                card1.setNum(val);

                // 加分
                score += val;
                //Log.i("GOAL", "********GOAL!!!********");
                if (layoutListener != null) {
                    //Log.i("ADD", "****Add Score!!!****");
                    layoutListener.onScoreChange(score);
                }

                // 向前移动
                for (int j = i + 1; j < row.size() - 1; j++) {
                    row.get(j).setNum(row.get(j + 1).getNum());
                }

                row.get(row.size() - 1).setNum(0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private boolean once;

    // 测量 Layout 的宽和高，以及设置 Card 的宽和高，这里忽略 wrap_content，以宽高中的最小值绘制正方形
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取正方形的边长
        int length = Math.min(getMeasuredWidth(), getMeasuredHeight());
        // 获得 Card 的边长
        int cardWidth = (length - padding * 2 - margin * (column - 1)) / column;

        if (!once) {
            if (cards == null) {
                cards = new ArrayList<>(column*column);
                Card c = new Card(getContext());
                c.setNum(0);
                for (int i = 0; i < column * column; i++) {
                    cards.add(c);
                }
            }

            // 放置 Card
            for (int i = 0; i < cards.size(); i++) {
                Card card = new Card(getContext());

                cards.remove(i);
                cards.add(i, card);
                
                card.setId(i + 1);
                LayoutParams lp = new LayoutParams(cardWidth, cardWidth);
                // 设置横向非最后一行的边距
                if ((i+1) % column != 0) {
                    lp.rightMargin = margin;
                }
                // 如果不是第一列
                if (i % column != 0) {
                    lp.addRule(RelativeLayout.RIGHT_OF, cards.get(i - 1).getId());
                }

                // 如果不是第一行，设置纵向非最后一行的边距
                if ((i + 1) > column) {
                    lp.topMargin = margin;
                    lp.addRule(RelativeLayout.BELOW, cards.get(i - column).getId());
                }
                addView(card, lp);
            }
            generateNum();
        }
        once = true;
        setMeasuredDimension(length, length);
    }

    private int min(int... params) {
        int min = params[0];

        for (int param : params)
            if (min > param)
                min = param;

        return min;
    }

    // 确定是否填满数字
    public boolean isFull() {
        // 检查是否所有位置都有数字
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getNum() == 0) {
                return false;
            }
        }
        //Log.i("Finish", "********FULL********");
        return true;
    }

    public boolean checkOverLimit(int time) {
        if (time > 0)
            return false;
        return checkOver();
    }

    // 检查是否每一位都有数字，且相邻位没有相同的数字
    public boolean checkOver() {
        if (!isFull()) return false;

        for (int i = 0; i < column; i++) {
            for (int j = 0; j < column; j++) {
                int index = i * column + j;

                // 当前的 Card
                Card card = cards.get(index);

                // 上边
                if ((index + 1) > column) {
                    //Log.e("TAG", "TOP");
                    Card topCard = cards.get(index - column);
                    if (topCard.getNum() == card.getNum())
                        return false;
                }

                // 下边
                if ((index + column) < column * column) {
                    //Log.e("TAG", "BOTTOM");
                    Card bottomCard = cards.get(index + column);
                    if (bottomCard.getNum() == card.getNum())
                        return false;
                }

                // 左边
                if (index % column != 0) {
                    //Log.e("TAG", "LEFT");
                    Card leftCard = cards.get(index - 1);
                    if (leftCard.getNum() == card.getNum())
                        return false;
                }

                // 右边
                if ((index + 1) % column != 0) {
                    //Log.e("TAG", "RIGHT");
                    Card rightCard = cards.get(index + 1);
                    if (rightCard.getNum() == card.getNum()) {
                        return false;
                    }
                }
            }
        }
        /*// 用两次循环
        for (int i = 0; i < cards.size() - 1; i++) {
            if (i % 4 != 3 && cards.get(i).getNum() == cards[i+1].getNum())
                return false;
        }

        for (int j = 4; j < cards.size(); j++) {
            if (cards[j].getNum() == cards[j-column].getNum())
                return false;
        }*/

        //Log.i("Complete", "********FINISH********");
        return true;
    }

    // 产生一个数
    private void generateNum() {
        if (checkOver()) {
            //Log.e("TAG", "GAME OVER");
            if (layoutListener != null) {
                layoutListener.onGameOver(score, false);
            }
            return;
        }

        if (!isFull()) {
            if (isMoveHappen || isMergeHappen) {
                Random random = new Random();
                int seek  = column * column;
                int next = random.nextInt(seek);
                Card card = cards.get(next);

                while (card.getNum() != 0) {
                    next = random.nextInt(seek);
                    card = cards.get(next);
                }

                card.setNum(Math.random() > 0.6 ? 4 : 2);
                isMergeHappen = isMoveHappen = false;
            }
        }
    }

    // 重新开始
    public void restart() {
        for (Card card : cards) {
            card.setNum(0);
        }
        score = 0;
        if (layoutListener != null) {
            layoutListener.onScoreChange(score);
        }
        isMoveHappen = isMergeHappen = true;
        generateNum();
    }

    public long getScore() {
        return this.score;
    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        final int FLING_MIN_DISTANCE = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            if (x > FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
                mediaPlayer.start();
                action(ACTION.RIGHT);
                // Toast.makeText(getContext(), "toRight", Toast.LENGTH_SHORT).show();
            } else if (x < -FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
                mediaPlayer.start();
                action(ACTION.LEFT);
                // Toast.makeText(getContext(), "toLeft", Toast.LENGTH_SHORT).show();
            } else if (y > FLING_MIN_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {
                mediaPlayer.start();
                action(ACTION.DOWM);
                // Toast.makeText(getContext(), "toDown", Toast.LENGTH_SHORT).show();
            } else if (y < -FLING_MIN_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {
                mediaPlayer.start();
                action(ACTION.UP);
                // Toast.makeText(getContext(), "toUp", Toast.LENGTH_SHORT).show();
            }
            return true;

        }
    }
}
