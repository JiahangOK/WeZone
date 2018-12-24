package edu.bjtu.gymclub.wezone.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;
import edu.bjtu.gymclub.wezone.Adapter.MyListAdapter;
import edu.bjtu.gymclub.wezone.Adapter.MyListAdapter1;
import edu.bjtu.gymclub.wezone.R;

public class ChatActivity extends AppCompatActivity{
    private static final int CAMERA_OK = 100;
    public static TextView tv_message;
    private RecyclerView record_list;
    private MyListAdapter1 myAdapter;
    private EditText message;
    private Button send_button;

    BmobIMConversation mBmobIMConversation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        //发送按钮的绑定
        send_button = (Button) findViewById(R.id.send_button);


        //聊天记录绑定
        record_list = findViewById(R.id.record_list);

        //文本框的绑定
        message = findViewById(R.id.editText2);

        List<List<String>> record = new ArrayList<>();
        record = getData("nihao");
        initRecyclerView(record);
        getSupportActionBar().hide();

        //发送按钮的响应事件
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> newdate = new ArrayList<>();
                String newmessages = message.getText().toString();
                if( newmessages!= null){
                    newdate.add(newmessages);
                    newdate.add("send");
                    myAdapter.addData(myAdapter.getItemCount(),newdate);
                    message.setText("");
                    record_list.scrollToPosition(myAdapter.getItemCount() - 1);
                }else{

                }
            }
        });
    }

    private void initRecyclerView(List<List<String>> mDatas){
        //设置布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        record_list.setLayoutManager(linearLayoutManager);
        record_list.setHasFixedSize(true);
        myAdapter = new MyListAdapter1(this,mDatas);
        record_list.setAdapter(myAdapter);
        record_list.setItemAnimator(new DefaultItemAnimator());
        record_list.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.HORIZONTAL));
    }

    private List<List<String>> getData(String username){

        List<List<String>> mDatas = new ArrayList<List<String>>();
        List<String> user1 = new ArrayList<String>();
        user1.add("存了半年积蓄");
        user1.add("receive");
        List<String> user2 = new ArrayList<String>();
        user2.add("买了门票一对");
        user2.add("send");
        mDatas.add(user1);
        mDatas.add(user1);
        mDatas.add(user1);
        mDatas.add(user1);
        mDatas.add(user2);

        return mDatas;
    }



    public void goback(View view){
        this.finish();
    }

    public void Vedio(View view){
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.CAMERA},CAMERA_OK);
        startVideo();
    }


    /**
     * 启动相机，创建文件，并要求返回uri
     */


    private void startVideo() {
        Intent intent = new Intent();
        //指定动作，启动相机
        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //启动相机并要求返回结果
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1 && resultCode == -1) {
            Uri videoUri = intent.getData();
            Log.e("niaho", videoUri.toString());
            //mVideoView.setVideoURI(videoUri);
        }else{
            Log.e("niaho",Integer.toString(resultCode));
        }
    }


//     int count = 0;
//    @Override\\    /    public void onClick(View v) {
//
//        if (v.getId() == R.id.send_button) {
//            count++;
//            BmobIMUserInfo info = new BmobIMUserInfo();
//            info.setAvatar("填写接收者的头像");
//            info.setUserId("1111");
//            info.setName("填写接收者的名字");
//            BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
//                @Override
//                public void done(BmobIMConversation c, BmobException e) {
//                    if (e == null) {
//                        isOpenConversation = true;
//                        //在此跳转到聊天页面或者直接转化
//                        mBmobIMConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
//                        tv_message.append("发送者：" + "你好"+Integer.toString(count) + "\n");
//                        BmobIMTextMessage msg = new BmobIMTextMessage();
//                        msg.setContent("你好"+Integer.toString(count));
//                        mBmobIMConversation.sendMessage(msg, new MessageSendListener() {
//                            @Override
//                            public void done(BmobIMMessage msg, BmobException e) {
//                                if (e != null) {
//
//                                } else {
//                                }
//                            }
//                        });
//                    } else {
//                        Toast.makeText(ChatActivity.this, "开启会话出错", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }
}
