package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.library.bubbleview.BubbleTextView;

import butterknife.BindView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import edu.bjtu.gymclub.wezone.Activity.ChatActivity;
import edu.bjtu.gymclub.wezone.Entity.Friend;
import edu.bjtu.gymclub.wezone.Entity.User;
import edu.bjtu.gymclub.wezone.Model.UserModel;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.util.ImageLoaderFactory;

/**
 * 接收到的文本类型
 */
public class Friend_Holder extends BaseViewHolder {

    //显示名字的textView
    TextView username;

    CardView cardView;


    ImageView avatar;

    public Friend_Holder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.friend_item, onRecyclerViewListener);
    }


    @Override
    public void bindData(Object o) {
        username = itemView.findViewById(R.id.usernamess);
        cardView = itemView.findViewById(R.id.record);
        avatar = itemView.findViewById(R.id.imageView7);
        final Friend friend = (Friend) o;
        User user = friend.getFriendUser();

        ImageLoaderFactory.getLoader().loadAvator(avatar, user.getAvatar(), R.mipmap.touxiang);
        username.setText(user.getUsername());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = friend.getFriendUser();
                BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                //TODO 会话：4.1、创建一个常态会话入口，好友聊天
                BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("c", conversationEntrance);
                Intent intent;
                intent = new Intent();
                intent.setClass(context, ChatActivity.class);
                intent.putExtra(context.getPackageName(), bundle);
                context.startActivity(intent);
            }
        });
    }

}