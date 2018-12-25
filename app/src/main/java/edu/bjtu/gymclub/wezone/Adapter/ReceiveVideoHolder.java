package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import butterknife.BindView;

import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;
import edu.bjtu.gymclub.wezone.R;

/**
 * 同意添加好友的agree类型
 */
public class ReceiveVideoHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {

    protected ImageView imageView9;

    private String currentUid = "";

    public ReceiveVideoHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_photo, onRecyclerViewListener);
        try {
            currentUid = BmobUser.getCurrentUser().getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindData(Object o) {
        imageView9 = itemView.findViewById(R.id.imageView9);


        BmobIMMessage msg = (BmobIMMessage) o;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
        final BmobIMUserInfo info = msg.getBmobIMUserInfo();

        //可使用buildFromDB方法转化为指定类型的消息
        final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(false, msg);

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Bitmap b = null;
        // 设置数据源，有多种重载，这里用本地文件的绝对路径
        try {
            //根据url获取缩略图
            mmr.setDataSource(message.getRemoteUrl(), new HashMap());
            //获得第一帧图片
            b = mmr.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            mmr.release();
        }

        imageView9.setImageBitmap(b);

        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.addCategory(Intent.CATEGORY_DEFAULT);
                intent1.setDataAndType(Uri.parse(message.getRemoteUrl()), "video/*");
                context.startActivity(intent1);
            }
        });


    }
}