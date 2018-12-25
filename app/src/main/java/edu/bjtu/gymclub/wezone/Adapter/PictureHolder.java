package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.SimpleDateFormat;

import butterknife.BindView;

import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.util.ImageLoaderFactory;

/**
 * 同意添加好友的agree类型
 */
public class PictureHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {


    protected ImageView imageView9;


    private String currentUid = "";


    public PictureHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
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
        //显示图片
        ImageLoaderFactory.getLoader().load(imageView9, message.getRemoteUrl(), R.mipmap.ic_launcher, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }
        });

        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.addCategory(Intent.CATEGORY_DEFAULT);
                intent1.setDataAndType(Uri.parse(message.getRemoteUrl()), "image/*");
                context.startActivity(intent1);

            }
        });
    }


}
