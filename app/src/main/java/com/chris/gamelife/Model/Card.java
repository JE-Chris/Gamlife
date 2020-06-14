package com.chris.gamelife.Model;

import android.content.Context;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chris.gamelife.R;

import java.io.Serializable;

public class Card extends FrameLayout implements Serializable {

    private TextView label;
    private View background;
    private int num = 0;

    public Card(Context context) {
        super(context);
        LayoutParams lp = null;

        background = new View(getContext());
        lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        background.setBackgroundColor(getResources().getColor(R.color.normalCardBack));
        addView(background, lp);

        label = new TextView(getContext());
        label.setTextSize(28);
        label.setGravity(Gravity.CENTER);

        TextPaint tp = label.getPaint();
        tp.setFakeBoldText(true);

        lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        addView(label, lp);

        setNum(0);
    }

    public int getNum() {
        return num;
    }

    public boolean equals(Card o) {
        return getNum() == o.getNum();
    }

    protected Card clone() {
        Card c = new Card(getContext());
        c.setNum(getNum());
        return c;
    }

    public TextView getLabel() {
        return label;
    }

    public void setNum(int num) {
        this.num = num;

        if (num <= 0) {
            label.setText("");
        } else {
            label.setText(num + "");
        }
        switch (num) {
            case 0:
                label.setBackgroundResource(R.color.normalCardBack);
                break;
            case 2:
                label.setTextColor(getResources().getColor(R.color._2Font));
                label.setBackgroundResource(R.color._2Back);
                break;
            case 4:
                label.setTextColor(getResources().getColor(R.color._4Font));
                label.setBackgroundResource(R.color._4Back);
                break;
            case 8:
                label.setTextColor(getResources().getColor(R.color.otherFont));
                label.setBackgroundResource(R.color._8Back);
                break;
            case 16:
                label.setTextColor(getResources().getColor(R.color.otherFont));
                label.setBackgroundResource(R.color._16Back);
                break;
            case 32:
                label.setTextColor(getResources().getColor(R.color.otherFont));
                label.setBackgroundResource(R.color._32Back);
                break;
            case 64:
                label.setTextColor(getResources().getColor(R.color.otherFont));
                label.setBackgroundResource(R.color._64Back);
                break;
            case 128:
                label.setTextColor(getResources().getColor(R.color.otherFont));
                label.setBackgroundResource(R.color._128Back);
                break;
            case 256:
                label.setTextColor(getResources().getColor(R.color.otherFont));
                label.setBackgroundResource(R.color._256Back);
                break;
            case 512:
                label.setTextColor(getResources().getColor(R.color.otherFont));
                label.setBackgroundResource(R.color._512Back);
                break;
            case 1024:
                label.setTextColor(getResources().getColor(R.color.otherFont));
                label.setBackgroundResource(R.color._1024Back);
                break;
            case 2048:
                label.setTextColor(getResources().getColor(R.color.otherFont));
                label.setBackgroundResource(R.color._2048Back);
                break;
            default:
                label.setTextColor(getResources().getColor(R.color._2Font));
                label.setBackgroundResource(R.color._2Back);
                break;
        }
    }
}


/*
package com.game_2048.Model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Card extends TextView {
    */
/**
     * 此 View 上的数字
     *//*

    private int num;
    private int id;
    private String numValue;
    private Paint paint = new Paint();

    */
/**
     * 绘制文字区域
     *//*

    private Rect mBound;

    public Card(Context context) {
        super(context, null);
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public Card(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    public void setNum(int number) {
        num = number;
        numValue = num + "";

        paint.setTextSize(100.0f);
        mBound = new Rect();

        paint.getTextBounds(numValue, 0, numValue.length(), mBound);
        invalidate();
    }

    public int getNum() {
        return num;
    }


    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String bg_color;

        switch(num) {
            case 2:
                bg_color="#feefdc";
                break;
            case 4:
                bg_color="#fee7bb";
                break;
            case 8:
                bg_color="#ff8f6a";
                break;
            case 16:
                bg_color="#fe7251";
                break;
            case 32:
                bg_color="#ff6342";
                break;
            case 64:
                bg_color="#fe5c43";
                break;
            case 128:
                bg_color="#fad177";
                break;
            case 256:
                bg_color="#f9d067";
                break;
            case 512:
                bg_color="#f9ca58";
                break;
            case 1024:
                bg_color="#f3be3c";
                break;
            case 2048:
                bg_color="#f6bc2b";
                break;
            case 4096:
                bg_color="#ff4f3d";
                break;
            case 8192:
                bg_color="#ff341d";
                break;
            case 16384:
                bg_color="#ff331f";
                break;
            case 32768:
                bg_color="#71b8d8";
                break;
            case 65536:
                bg_color="#5ea1e5";
                break;
            case 131072:
                bg_color="#007fc2";
                break;
            case 262144:
                bg_color="#262144";
                break;
            default:
                bg_color="#FFFAFA";
                break;
        }

        paint.setColor(Color.parseColor(bg_color));
        paint.setStyle(Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        if (num != 0) drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        paint.setColor(Color.BLACK);
        float x = (getWidth() - mBound.width()) / 2;
        float y = (getHeight() - mBound.height()) / 2;
        canvas.drawText(numValue, x, y, paint);
    }
}
*/
