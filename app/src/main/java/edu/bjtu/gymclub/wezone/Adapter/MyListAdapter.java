package edu.bjtu.gymclub.wezone.Adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMConversationType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import edu.bjtu.gymclub.wezone.Activity.ChatActivity;
import edu.bjtu.gymclub.wezone.Activity.NewFriendActivity;
import edu.bjtu.gymclub.wezone.Entity.Conversation;
import edu.bjtu.gymclub.wezone.Entity.User;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.db.NewFriend;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Conversation> mDatas;

    public MyListAdapter(Context context, List<Conversation> data) {
        this.mContext = context;
        this.mDatas = data;
    }


    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //小元素
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Conversation c = mDatas.get(position);


        holder.username.setText(mDatas.get(position).getcName());
        holder.content.setText(mDatas.get(position).getLastMessageContent());
        Long time = mDatas.get(position).getLastMessageTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(time);
        holder.date.setText(sdf.format(date));
        if (c.getcType() == null) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent();
                    intent.setClass(mContext, NewFriendActivity.class);
                    mContext.startActivity(intent);
                }

            });
        } else if (c.getcType().getName().equals("private")) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BmobIMUserInfo info = new BmobIMUserInfo(c.getcId(), c.getcName(), c.getAvatar().toString());
                    //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    Intent intent;
                    intent = new Intent();
                    intent.setClass(mContext, ChatActivity.class);
                    intent.putExtra(mContext.getPackageName(),bundle);
                    mContext.startActivity(intent);
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 绑定数据
     *
     * @param datas
     * @return
     */
    public MyListAdapter bindDatas(Collection<Conversation> datas) {
        this.mDatas = datas == null ? new ArrayList<Conversation>() : new ArrayList<Conversation>(datas);
        notifyDataSetChanged();
        return this;
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


        }
    }
}
