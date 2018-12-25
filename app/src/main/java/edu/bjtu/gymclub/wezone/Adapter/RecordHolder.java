package edu.bjtu.gymclub.wezone.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.BindView;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.newim.listener.FileDownloadListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import edu.bjtu.gymclub.wezone.R;

/**
 * 同意添加好友的agree类型
 */
public class RecordHolder extends BaseViewHolder  {


    Button button;
    private String currentUid="";


    public RecordHolder(Context context, ViewGroup root,OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_voice,onRecyclerViewListener);
        try {
            currentUid = BmobUser.getCurrentUser().getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindData(Object o) {
        button = itemView.findViewById(R.id.button);
        BmobIMMessage msg = (BmobIMMessage) o;


        //使用buildFromDB方法转化成指定类型的消息
        final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(true, msg);
        boolean isExists = BmobDownloadManager.isAudioExist(currentUid, message);
        if(!isExists){//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
            BmobDownloadManager downloadTask = new BmobDownloadManager(getContext(),msg,new FileDownloadListener() {

                @Override
                public void onStart() {

                    button.setVisibility(View.INVISIBLE);//只有下载完成才显示播放的按钮
                }

                @Override
                public void done(BmobException e) {
                    if(e==null){
                        button.setVisibility(View.VISIBLE);
                    }else{

                        button.setVisibility(View.INVISIBLE);
                    }
                }
            });
            downloadTask.execute(message.getContent());
        }
        button.setOnClickListener(new NewRecordPlayClickListener(getContext(), message, button));



    }
}
