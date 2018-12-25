package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import edu.bjtu.gymclub.wezone.Activity.ChatActivity;
import edu.bjtu.gymclub.wezone.Config;
import edu.bjtu.gymclub.wezone.Entity.AgreeAddFriendMessage;
import edu.bjtu.gymclub.wezone.Entity.Conversation;
import edu.bjtu.gymclub.wezone.Entity.User;
import edu.bjtu.gymclub.wezone.Model.UserModel;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.db.NewFriend;
import edu.bjtu.gymclub.wezone.db.NewFriendManager;


public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.NewFriendHolder> {

    private Context mContext;
    private List<NewFriend> mDatas;

    public NewFriendAdapter(Context context, List<NewFriend> data) {
        this.mContext = context;
        this.mDatas = data;
    }


    @Override
    public NewFriendAdapter.NewFriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //小元素
        NewFriendAdapter.NewFriendHolder holder = new NewFriendAdapter.NewFriendHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_newfriend, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final NewFriendAdapter.NewFriendHolder holder, final int position) {
        holder.username.setText(mDatas.get(position).getName());
        holder.content.setText(mDatas.get(position).getMsg());
        Integer status = mDatas.get(position).getStatus();
        //当状态是未添加或者是已读未添加
        if (status == null || status == Config.STATUS_VERIFY_NONE || status == Config.STATUS_VERIFY_READED) {
            holder.btn_accept.setText("接受");
            holder.btn_accept.setEnabled(true);

            holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agreeAdd(mDatas.get(position), new SaveListener<Object>() {
                        @Override
                        public void done(Object o, BmobException e) {
                            if (e == null) {
                                holder.btn_accept.setText("已添加");
                                holder.btn_accept.setEnabled(false);
                            } else {
                                holder.btn_accept.setEnabled(true);
                                Logger.e("添加好友失败:" + e.getMessage());
                                Toast.makeText(mContext, "添加好友失败:", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } else {
            holder.btn_accept.setText("已添加");
            holder.btn_accept.setEnabled(false);
        }

        Long time = mDatas.get(position).getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(time);
        holder.date.setText(sdf.format(date));
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
    public NewFriendAdapter bindDatas(Collection<NewFriend> datas) {
        this.mDatas = datas == null ? new ArrayList<NewFriend>() : new ArrayList<NewFriend>(datas);
        notifyDataSetChanged();
        return this;
    }


    public class NewFriendHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView content;
        TextView date;
        CardView cardView;
        Button btn_accept;





        public NewFriendHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.usernames);
            cardView = itemView.findViewById(R.id.record);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            btn_accept = itemView.findViewById(R.id.btn_accept);


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

    /**
     * TODO 好友管理：9.10、添加到好友表中再发送同意添加好友的消息
     *
     * @param add
     * @param listener
     */
    private void agreeAdd(final NewFriend add, final SaveListener<Object> listener) {
        User user = new User();
        user.setObjectId(add.getUid());
        UserModel.getInstance()
                .agreeAddFriend(user, new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            sendAgreeAddFriendMessage(add, listener);
                        } else {
                            Logger.e(e.getMessage());
                            listener.done(null, e);
                        }
                    }
                });
    }

    /**
     * 发送同意添加好友的消息
     */
    //TODO 好友管理：9.8、发送同意添加好友
    private void sendAgreeAddFriendMessage(final NewFriend add, final SaveListener<Object> listener) {
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            Toast.makeText(mContext, "尚未连接IM服务器", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobIMUserInfo info = new BmobIMUserInfo(add.getUid(), add.getName(), add.getAvatar());
        //TODO 会话：4.1、创建一个暂态会话入口，发送同意好友请求
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //TODO 消息：5.1、根据会话入口获取消息管理，发送同意好友请求
        BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        //而AgreeAddFriendMessage的isTransient设置为false，表明我希望在对方的会话数据库中保存该类型的消息
        AgreeAddFriendMessage msg = new AgreeAddFriendMessage();
        final User currentUser = BmobUser.getCurrentUser(User.class);
        msg.setContent("我通过了你的好友验证请求，我们可以开始 聊天了!");//这句话是直接存储到对方的消息表中的
        Map<String, Object> map = new HashMap<>();
        map.put("msg", currentUser.getUsername() + "同意添加你为好友");//显示在通知栏上面的内容
        map.put("uid", add.getUid());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
        map.put("time", add.getTime());//添加好友的请求时间
        msg.setExtraMap(map);
        messageManager.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    NewFriendManager.getInstance(mContext).updateNewFriend(add, Config.STATUS_VERIFIED);
                    listener.done(msg, e);
                } else {//发送失败
                    Logger.e(e.getMessage());
                    listener.done(msg, e);
                }
            }
        });
    }
}
