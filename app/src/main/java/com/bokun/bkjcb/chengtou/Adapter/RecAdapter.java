package com.bokun.bkjcb.chengtou.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bokun.bkjcb.chengtou.Domain.Detail;
import com.bokun.bkjcb.chengtou.R;
import com.bokun.bkjcb.chengtou.Util.L;

import java.util.List;

/**
 * Created by DengShuai on 2017/7/24.
 */

public class RecAdapter extends SimpleRecAdapter<Detail, RecAdapter.MyViewHolder> {
    public RecAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Detail detail = data.get(position);
        holder.id.setText((position + 1) + "");
        holder.name.setText(detail.getXiangmumingcheng());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i(position);
                if (getSimpleItemClick() != null) {
                    getSimpleItemClick().onItemClick(position, detail, 0, holder);
                }
            }
        });
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
    public MyViewHolder newViewHolder(View itemView) {
        return new MyViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.detail_view;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.detail_id);
            name = (TextView) itemView.findViewById(R.id.detail_name);
        }
    }
}
