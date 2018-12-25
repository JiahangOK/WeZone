package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import edu.bjtu.gymclub.wezone.R;

/**
 * 接收到的文本类型
 */
public class ReceiveTextHolder extends BaseViewHolder {

    private LinearLayout layout1;
    private LinearLayout layout2;
    private BubbleTextView bubbleTextView1;
    private BubbleTextView bubbleTextView2;

    public ReceiveTextHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.record_item, onRecyclerViewListener);
    }


    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;


        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        layout1 = itemView.findViewById(R.id.left_record);
        layout2 = itemView.findViewById(R.id.right_record);
        bubbleTextView1 = itemView.findViewById(R.id.receiver_left);
        bubbleTextView2 = itemView.findViewById(R.id.receiver_right);

        String content = message.getContent();

        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.GONE);
        bubbleTextView1.setText(content);
    }

}