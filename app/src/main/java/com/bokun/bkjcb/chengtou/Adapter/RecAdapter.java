package com.bokun.bkjcb.chengtou.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Created by DengShuai on 2017/7/24.
 */

public class RecAdapter extends SimpleRecAdapter {
    public RecAdapter(Context context) {
        super(context);
    }

    public RecAdapter(Context context, ItemCallback callback) {
        super(context, callback);
    }

    public RecAdapter(Context context, List data) {
        super(context, data);
    }

    public RecAdapter(Context context, List data, ItemCallback callback) {
        super(context, data, callback);
    }

    @Override
    public RecyclerView.ViewHolder newViewHolder(View itemView) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
