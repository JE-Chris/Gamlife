package com.chris.gamelife.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<String> list;
    private LayoutInflater inflater;
    private int itemCount = 3;

    public MyAdapter(Context context, List<String> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 如果数据数量大于限额，则显示限额数，否则全部显示
     * @return
     */
    @Override
    public int getCount() {
        if (list.size() > 3) {
            return itemCount;
        } else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
