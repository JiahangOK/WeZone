package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import edu.bjtu.gymclub.wezone.Entity.AddFriendMessage;
import edu.bjtu.gymclub.wezone.Entity.User;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.util.ImageLoaderFactory;

public class SearchUserHolder extends BaseViewHolder {

    @BindView(R.id.avatar)
    public ImageView avatar;
    @BindView(R.id.name)
    public TextView name;
    @BindView(R.id.btn_add)
    public Button btn_add;

    public SearchUserHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_search_user, onRecyclerViewListener);
    }

    @Override
    public void bindData(Object o) {
        final User user = (User) o;
        ImageLoaderFactory.getLoader().loadAvator(avatar, user.getAvatar(), R.mipmap.touxiang);
        name.setText(user.getUsername());
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//添加好友
                Bundle bundle = new Bundle();
                sendAddFriendMessage(user);
            }
        });
    }

    /**
     * 发送添加好友的请求
     */
    //TODO 好友管理：9.7、发送添加好友请求
    private void sendAddFriendMessage(final User user) {
        BmobIMUserInfo info;
        info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            toast("尚未连接IM服务器");
               return;
        }
        //TODO 会话：4.1、创建一个暂态会话入口，发送好友请求
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //TODO 消息：5.1、根据会话入口获取消息管理，发送好友请求
        BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        AddFriendMessage msg = new AddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(User.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String, Object> map = new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名
        map.put("avatar", currentUser.getAvatar());//发送者的头像
        map.put("uid", currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        messageManager.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    toast("好友请求发送成功，等待验证");
                } else {//发送失败
                    toast("发送失败:" + e.getMessage());
                }
            }
        });
    }
}