package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;

import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;
import edu.bjtu.gymclub.wezone.R;

//import com.squareup.picasso.Picasso;

/**
 * 同意添加好友的agree类型
 */
public class SendPictureHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {


    LinearLayout left_record;
    LinearLayout right_record;
    ImageView image1;
    ImageView image2;


    private String currentUid = "";


    public SendPictureHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_photo, onRecyclerViewListener);
        try {
            currentUid = BmobUser.getCurrentUser().getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindData(Object o) {
        image1 = itemView.findViewById(R.id.image1);
        image2 = itemView.findViewById(R.id.image2);
        left_record = itemView.findViewById(R.id.left_record);
        right_record = itemView.findViewById(R.id.right_record);

        right_record.setVisibility(View.VISIBLE);
        left_record.setVisibility(View.GONE);


        BmobIMMessage msg = (BmobIMMessage) o;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
        final BmobIMUserInfo info = msg.getBmobIMUserInfo();

        //可使用buildFromDB方法转化为指定类型的消息
        final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(false, msg);

        //显示图片

        String remote = message.getRemoteUrl();
        String[] path = remote.split("&");
        File file = new File(path[0]);
        Picasso.with(context).load(file).placeholder(image2.getDrawable()).into(image2);



        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.addCategory(Intent.CATEGORY_DEFAULT);
                intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                String remote = message.getRemoteUrl();
                String[] path = remote.split("&");
                File file = new File(path[0]);
                Uri uri = FileProvider.getUriForFile(context, "edu.bjtu.gymclub.wezone.fileprovider", file);
                intent1.setDataAndType(uri, "image/*");
                context.startActivity(intent1);
                intent1.getData();
            }
        });
    }


}
