package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.library.bubbleview.BubbleTextView;

import java.io.File;
import java.io.IOException;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.newim.listener.FileDownloadListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import edu.bjtu.gymclub.wezone.R;

/**
 * 收到语音消息
 */
public class SendRecordHolder extends BaseViewHolder {

    private LinearLayout left_record;
    private LinearLayout right_record;
    BubbleTextView button1;
    BubbleTextView button2;
    private String currentUid = "";


    public SendRecordHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_voice, onRecyclerViewListener);
        try {
            currentUid = BmobUser.getCurrentUser().getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindData(Object o) {
        left_record =(LinearLayout) itemView.findViewById(R.id.left_record);
        right_record =(LinearLayout) itemView.findViewById(R.id.right_record);
        button1 = (BubbleTextView)itemView.findViewById(R.id.button1);
        button2 =(BubbleTextView) itemView.findViewById(R.id.button2);

        final BmobIMMessage msg = (BmobIMMessage) o;
        right_record.setVisibility(View.VISIBLE);
        left_record.setVisibility(View.GONE);


        //使用buildFromDB方法转化成指定类型的消息
        final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(true, msg);
        File audioFile = new File(msg.getContent().split("&")[0]);
        boolean isExists = BmobDownloadManager.isAudioExist(currentUid, message);
        if (!isExists) {//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
            BmobDownloadManager downloadTask = new BmobDownloadManager(getContext(), msg, new FileDownloadListener() {

                @Override
                public void onStart() {

//                    button2.setVisibility(View.INVISIBLE);//只有下载完成才显示播放的按钮
                }

                @Override
                public void done(BmobException e) {
                    if (e == null) {
//                        button2.setVisibility(View.VISIBLE);
                    } else {

//                        button2.setVisibility(View.INVISIBLE);
                    }
                }
            });
            downloadTask.execute(message.getContent());
        }
//        button.setOnClickListener(new NewRecordPlayClickListener(getContext(), message, button));
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.getFromId().equals(currentUid)) {// 如果是自己发送的语音消息，则播放本地地址
                    String localPath = msg.getContent().split("&")[0];
                    Log.e("nihao", localPath);
                    MediaPlayer mPlayer = new MediaPlayer();
                    try {
                        mPlayer.setDataSource(localPath);
                        mPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mPlayer.start();
                } else {// 如果是收到的消息，则需要先下载后播放
                    String localPath = BmobDownloadManager.getDownLoadFilePath(message);
                    Log.e("nihao", localPath);
                    MediaPlayer mPlayer = new MediaPlayer();
                    try {
                        mPlayer.setDataSource(localPath);
                        mPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mPlayer.start();
                }
            }
        });
    }
}
