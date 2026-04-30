package com.ubx.uhf.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ubx.rfid.bean.TagScan;
import com.ubx.uhf.R;

import java.util.List;

public class AdapterManageList extends RecyclerView.Adapter<AdapterManageList.ViewHolder> {

    private List<TagScan> data;
    private Context context;
    private onItemSelectedListener onItemSelectedListener;
    private int currentItem = -1;
    private int temp = -1;

    public AdapterManageList(List<TagScan> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public List<TagScan> getData() {
        return data;
    }

    public void setData(List<TagScan> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public AdapterManageList.onItemSelectedListener getOnItemSelectedListener() {
        return onItemSelectedListener;
    }

    public void setOnItemSelectedListener(AdapterManageList.onItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public AdapterManageList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.tag_manage_item, parent, false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final AdapterManageList.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.refreshView(position, data.get(position));
        holder.itemView.setSelected(holder.getLayoutPosition() == currentItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.setSelected(true);
                temp = currentItem;
                currentItem = holder.getLayoutPosition();
                notifyItemChanged(temp);
                onItemSelectedListener.onItemSelected(holder.itemView, position, data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != data ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        TextView manageEpcText;
        TextView manageDataText;
        public ViewHolder( View view) {
            super(view.getRootView());
            this.mView = view;
            manageEpcText = mView.findViewById(R.id.manage_epc_text);
            manageDataText = mView.findViewById(R.id.manage_data_text);
        }

        private void refreshView(int position, TagScan data) {
            Log.d("usdk", "refreshView: data = " + data + ", i = " + position);
            manageEpcText.setText(data.getEpc());
            manageDataText.setText(data.getTid());
        }
    }

    interface onItemSelectedListener {

        void onItemSelected(View v, int position, TagScan data);

    }
}
