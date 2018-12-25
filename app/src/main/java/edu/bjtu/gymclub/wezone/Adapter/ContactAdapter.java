package edu.bjtu.gymclub.wezone.Adapter;


import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.github.library.bubbleview.BubbleTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.v3.BmobUser;
import edu.bjtu.gymclub.wezone.Activity.ChatActivity;
import edu.bjtu.gymclub.wezone.Entity.Conversation;
import edu.bjtu.gymclub.wezone.Entity.Friend;
import edu.bjtu.gymclub.wezone.Entity.User;
import edu.bjtu.gymclub.wezone.R;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Friend> friends = new ArrayList<>();

    public ContactAdapter() {
    }

    public void setDatas(List<Friend> list) {
        friends.clear();
        if (null != list) {
            friends.addAll(list);
        }
    }
    /**
     * 删除数据
     * @param position
     */
    public void remove(int position) {
        int more = getItemCount() - friends.size();
        friends.remove(position - more);
        notifyDataSetChanged();
    }

    /**
     * 绑定数据
     *
     * @param datas
     * @return
     */
    public ContactAdapter bindDatas(Collection<Friend> datas) {
        this.friends = datas == null ? new ArrayList<Friend>() : new ArrayList<Friend>(datas);
        notifyDataSetChanged();
        return this;
    }

    /**获取用户
     * @param position
     * @return
     */
    public Friend getItem(int position){
        return friends.get(position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Friend_Holder(parent.getContext(), parent, onRecyclerViewListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(friends.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

}
