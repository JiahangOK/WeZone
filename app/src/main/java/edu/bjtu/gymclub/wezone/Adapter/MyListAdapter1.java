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

public class MyListAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    public MyListAdapter1(Context context, List<List<String>> data) {
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

    public MyListAdapter1(ChatActivity context, BmobIMConversation mConversationManager) {
        try {
            currentUid = BmobUser.getCurrentUser().getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.c = mConversationManager;
    }

//       @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.record_item, parent, false));
//        return holder;
//    }

//    public void addData(int position, List<String> newItem) {
//        mDatas.add(position, newItem);
//        Log.e("闪退", mDatas.toString());
//        //添加动画
//        notifyItemInserted(position);
//    }

//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        String content = mDatas.get(position).get(0);
//        String record_type = mDatas.get(position).get(1);
//        if (record_type.equals("receive")) {
//            holder.layout1.setVisibility(View.VISIBLE);
//            holder.layout2.setVisibility(View.GONE);
//            holder.bubbleTextView1.setText(mDatas.get(position).get(0));
//        } else {
//            holder.layout2.setVisibility(View.VISIBLE);
//            holder.layout1.setVisibility(View.GONE);
//            holder.bubbleTextView2.setText(mDatas.get(position).get(0));
//        }
//    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }



    @Override
    public int getItemCount() {
        return msgs.size();
    }

//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_SEND_TXT) {
//            return new SendTextHolder(parent.getContext(), parent, c, onRecyclerViewListener);
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEND_TXT) {
            return new SendTextHolder(parent.getContext(), parent, c, onRecyclerViewListener);
        } else if (viewType == TYPE_RECEIVER_TXT) {
            return new ReceiveTextHolder(parent.getContext(), parent, onRecyclerViewListener);
        } else if(viewType ==TYPE_AGREE) {
            return new AgreeHolder(parent.getContext(),parent,onRecyclerViewListener);
        } else {//开发者自定义的其他类型，可自行处理
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).bindData(msgs.get(position));
        if (holder instanceof AgreeHolder) {//同意添加好友成功后的消息
            ((AgreeHolder)holder).showTime(shouldShowTime(position));
        }
//        if (holder instanceof ReceiveTextHolder) {
//            ((ReceiveTextHolder) holder).showTime(shouldShowTime(position));
//        } else if (holder instanceof SendTextHolder) {
//            ((SendTextHolder) holder).showTime(shouldShowTime(position));
//        }
    }

    @Override
    public int getItemViewType(int position) {
        BmobIMMessage message = msgs.get(position);
        if (message.getMsgType().equals(BmobIMMessageType.IMAGE.getType())) {
            return message.getFromId().equals(currentUid) ? TYPE_SEND_IMAGE : TYPE_RECEIVER_IMAGE;
        } else if (message.getMsgType().equals(BmobIMMessageType.LOCATION.getType())) {
            return message.getFromId().equals(currentUid) ? TYPE_SEND_LOCATION : TYPE_RECEIVER_LOCATION;
        } else if (message.getMsgType().equals(BmobIMMessageType.VOICE.getType())) {
            return message.getFromId().equals(currentUid) ? TYPE_SEND_VOICE : TYPE_RECEIVER_VOICE;
        } else if (message.getMsgType().equals(BmobIMMessageType.TEXT.getType())) {
            return message.getFromId().equals(currentUid) ? TYPE_SEND_TXT : TYPE_RECEIVER_TXT;
        } else if (message.getMsgType().equals(BmobIMMessageType.VIDEO.getType())) {
            return message.getFromId().equals(currentUid) ? TYPE_SEND_VIDEO : TYPE_RECEIVER_VIDEO;
        } else if (message.getMsgType().equals("agree")) {//显示欢迎
            return TYPE_AGREE;
        } else {
            return -1;
        }
    }

//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        private LinearLayout layout1;
//        private LinearLayout layout2;
//        private BubbleTextView bubbleTextView1;
//        private BubbleTextView bubbleTextView2;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            layout1 = itemView.findViewById(R.id.left_record);
//            layout2 = itemView.findViewById(R.id.right_record);
//            bubbleTextView1 = itemView.findViewById(R.id.receiver_left);
//            bubbleTextView2 = itemView.findViewById(R.id.receiver_right);
//        }
//    }

    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = msgs.get(position - 1).getCreateTime();
        long curTime = msgs.get(position).getCreateTime();
        return curTime - lastTime > TIME_INTERVAL;
    }
}
