package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.SimpleDateFormat;

import butterknife.BindView;

import cn.bmob.newim.bean.BmobIMMessage;
import edu.bjtu.gymclub.wezone.R;

/**
 * 同意添加好友的agree类型
 */
public class VideoHolder extends BaseViewHolder implements View.OnClickListener,View.OnLongClickListener {

    @BindView(R.id.videoView)
    protected VideoView videoView;

    public VideoHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
        super(context, root, R.layout.item_vedio, listener);
    }

    @Override
    public void bindData(Object o) {
//        videoView.setVideoURI();
    }
}
