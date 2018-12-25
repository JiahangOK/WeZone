package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
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
import edu.bjtu.gymclub.wezone.R;

/**
 * 接收到的文本类型
 */
public class Friend_Holder extends BaseViewHolder {

    //显示名字的textView
    @BindView(R.id.usernamess)
    TextView username;

    public Friend_Holder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.friend_item, onRecyclerViewListener);
    }


    @Override
    public void bindData(Object o) {

    }

}