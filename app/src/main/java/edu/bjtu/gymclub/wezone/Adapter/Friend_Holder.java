package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;

import butterknife.BindView;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import edu.bjtu.gymclub.wezone.Entity.Friend;
import edu.bjtu.gymclub.wezone.Entity.User;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.util.ImageLoaderFactory;

/**
 * 接收到的文本类型
 */
public class Friend_Holder extends BaseViewHolder {

    //显示名字的textView
    @BindView(R.id.usernamess)
    TextView username;

    @BindView(R.id.imageView7)
    public ImageView avatar;

    public Friend_Holder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.friend_item, onRecyclerViewListener);
    }


    @Override
    public void bindData(Object o) {
        final Friend friend = (Friend) o;
        User user =friend.getFriendUser();

        ImageLoaderFactory.getLoader().loadAvator(avatar, user.getAvatar(), R.mipmap.touxiang);
        username.setText(user.getUsername());
    }

}