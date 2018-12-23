package edu.bjtu.gymclub.wezone.Adapter;


import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import edu.bjtu.gymclub.wezone.Activity.ChatActivity;
import edu.bjtu.gymclub.wezone.R;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyViewHolder> {

    private Context mContext;
    private List<List<String>> mDatas;

    public MyListAdapter(Context context, List<List<String>> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.username.setText(mDatas.get(position).get(0));
        holder.content.setText(mDatas.get(position).get(1));
        holder.date.setText(mDatas.get(position).get(2));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView content;
        TextView date;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.usernames);
            cardView = itemView.findViewById(R.id.record);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent();
                    intent.setClass(mContext, ChatActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
