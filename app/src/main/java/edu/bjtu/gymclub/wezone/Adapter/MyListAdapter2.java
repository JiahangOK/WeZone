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
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.v3.BmobUser;
import edu.bjtu.gymclub.wezone.Activity.ChatActivity;
import edu.bjtu.gymclub.wezone.R;

public class MyListAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //文本
    private final int TYPE_RECEIVER_TXT = 0;
    private final int TYPE_SEND_TXT = 1;
    //图片
    private final int TYPE_SEND_IMAGE = 2;
    private final int TYPE_RECEIVER_IMAGE = 3;
    //位置
    private final int TYPE_SEND_LOCATION = 4;
    private final int TYPE_RECEIVER_LOCATION = 5;
    //语音
    private final int TYPE_SEND_VOICE = 6;
    private final int TYPE_RECEIVER_VOICE = 7;
    //视频
    private final int TYPE_SEND_VIDEO = 8;
    private final int TYPE_RECEIVER_VIDEO = 9;

    //同意添加好友成功后的样式
    private final int TYPE_AGREE = 10;

    /**
     * 显示时间间隔:10分钟
     */
    private final long TIME_INTERVAL = 10 * 60 * 1000;

    private Context mContext;
    private List<List<String>> mDatas;
    private String currentUid = "";
    BmobIMConversation c;
    private List<BmobIMMessage> msgs = new ArrayList<>();

    public MyListAdapter2(Context context, List<List<String>> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    public void addMessages(List<BmobIMMessage> messages) {
        msgs.addAll(0, messages);
        notifyDataSetChanged();
    }

    public void addMessage(BmobIMMessage message) {
        msgs.addAll(Arrays.asList(message));
        notifyDataSetChanged();
    }

    public int findPosition(BmobIMMessage message) {
        int index = this.getCount();
        int position = -1;
        while (index-- > 0) {
            if (message.equals(this.getItem(index))) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int getCount() {
        return this.msgs == null ? 0 : this.msgs.size();
    }

    /**
     * 获取消息
     *
     * @param position
     * @return
     */
    public BmobIMMessage getItem(int position) {
        return this.msgs == null ? null : (position >= this.msgs.size() ? null : this.msgs.get(position));
    }


    public BmobIMMessage getFirstMessage() {
        if (null != msgs && msgs.size() > 0) {
            return msgs.get(0);
        } else {
            return null;
        }
    }

    public MyListAdapter2(ChatActivity context, BmobIMConversation mConversationManager) {
        try {
            currentUid = BmobUser.getCurrentUser().getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.c = mConversationManager;
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }



    @Override
    public int getItemCount() {
        return msgs.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Friend_Holder(parent.getContext(),parent,onRecyclerViewListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Friend_Holder) holder).bindData(msgs.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return -1;
    }

}
