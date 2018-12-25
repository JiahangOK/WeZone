package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;

import java.text.SimpleDateFormat;

import butterknife.BindView;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.util.ImageLoaderFactory;

/**
 * 发送的文本类型
 */
public class SendTextHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private LinearLayout layout1;
    private LinearLayout layout2;
    private BubbleTextView bubbleTextView1;
    private BubbleTextView bubbleTextView2;


    BmobIMConversation c;

    public SendTextHolder(Context context, ViewGroup root, BmobIMConversation c, OnRecyclerViewListener listener) {
        super(context, root, R.layout.record_item, listener);
        this.c = c;
    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        layout1 = itemView.findViewById(R.id.left_record);
        layout2 = itemView.findViewById(R.id.right_record);
        bubbleTextView1 = itemView.findViewById(R.id.receiver_left);
        bubbleTextView2 = itemView.findViewById(R.id.receiver_right);

        String content = message.getContent();

        layout2.setVisibility(View.VISIBLE);
        layout1.setVisibility(View.GONE);
        bubbleTextView2.setText(content);
    }

}
