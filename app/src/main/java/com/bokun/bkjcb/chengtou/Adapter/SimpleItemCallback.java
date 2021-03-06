package com.bokun.bkjcb.chengtou.Adapter;

/**
 * Created by shihao on 2017/3/9.
 */

public abstract class SimpleItemCallback<T, H> extends ItemCallback<T> {
    public abstract void onItemClick(int position, T model, int tag, H holder);


    public abstract void onItemLongClick(int position, T model, int tag, H holder);
}
