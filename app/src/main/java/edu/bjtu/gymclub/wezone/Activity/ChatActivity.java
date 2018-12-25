package edu.bjtu.gymclub.wezone.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;
import edu.bjtu.gymclub.wezone.Adapter.MyListAdapter;
import edu.bjtu.gymclub.wezone.Adapter.MyListAdapter1;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.util.Util;

public class ChatActivity extends BaseActivity implements MessageListHandler {


    private static final int CAMERA_OK = 100;
    public static TextView tv_message;
    private RecyclerView record_list;
    private MyListAdapter1 myAdapter;
    private EditText message;
    private Button send_button;
    BmobIMConversation mConversationManager;
    SwipeRefreshLayout sw_refresh;
    protected LinearLayoutManager layoutManager;

    LinearLayout ll_chat;
    BmobRecordManager recordManager;
    Button btn_speak;
    private String mFileName;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private Uri videoUri;
    private Uri photeUri;
    private Uri fileUri;
    private String mFileName1;
    private String mFileName2;
    private Uri fileUri2;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        BmobIMConversation conversationEntrance = (BmobIMConversation) getBundle().getSerializable("c");
        //TODO 消息：5.1、根据会话入口获取消息管理，聊天页面
        mConversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);

        sw_refresh = findViewById(R.id.sw_refresh);
        ll_chat = findViewById(R.id.root_View);
        btn_speak = findViewById(R.id.button10);
        final Context context = this;

        btn_speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    onRecord();
                } else if (action == MotionEvent.ACTION_UP) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    Toast.makeText(context, "录音结束", Toast.LENGTH_SHORT).show();

                    getRedpermission();
                    sendVoiceMessage(mFileName);
                }
                return false;
            }
        });

        //设置标题为用户名
        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText(mConversationManager.getConversationTitle());


        //发送按钮的绑定
        send_button = (Button) findViewById(R.id.send_button);


        //聊天记录绑定
        record_list = findViewById(R.id.record_list);


        //文本框的绑定
        message = findViewById(R.id.editText2);

        //List<List<String>> record = new ArrayList<>();
        //       record = getData("nihao");
//      initRecyclerView(record);
        getSupportActionBar().hide();

        initSwipeLayout();
        initBottomView();


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
            Toast.makeText(this, "还没有进行授权!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 1);
        } else {
            Toast.makeText(this, "已经授权成功了!", Toast.LENGTH_SHORT).show();
        }


        //发送按钮的响应事件
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                    toast("尚未连接IM服务器");
                    return;
                }
                sendMessage();
//                List<String> newdate = new ArrayList<>();
//                String newmessages = message.getText().toString();
//                if (newmessages != null) {
//                    newdate.add(newmessages);
//                    newdate.add("send");
//                    myAdapter.addData(myAdapter.getItemCount(), newdate);
//                    message.setText("");
//                    record_list.scrollToPosition(myAdapter.getItemCount() - 1);
//                } else {
//
//                }
            }
        });

    }

    public void getRedpermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
            Toast.makeText(this, "还没有进行授权!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 1);
        } else {
            Toast.makeText(this, "已经授权成功了!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 发送文本消息
     */
    private void sendMessage() {
        String text = message.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            toast("请输入内容");
            return;
        }
        //TODO 发送消息：6.1、发送文本消息
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        //可随意设置额外信息
        Map<String, Object> map = new HashMap<>();
        map.put("level", "1");
        msg.setExtraMap(map);
        msg.setExtra("OK");
        mConversationManager.sendMessage(msg, listener);
    }

    /**
     * 发送语音消息
     *
     * @param local
     * @return void
     * @Title: sendVoiceMessage
     */
    private void sendVoiceMessage(String local) {
        //TODO 发送消息：6.5、发送本地音频文件消息
        BmobIMAudioMessage audio = new BmobIMAudioMessage(local);
        mConversationManager.sendMessage(audio, listener);
    }


    private void initSwipeLayout() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        record_list.setLayoutManager(layoutManager);
        myAdapter = new MyListAdapter1(this, mConversationManager);
        record_list.setAdapter(myAdapter);

        ll_chat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_chat.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                //自动刷新
                queryMessages(null);
            }
        });
        //下拉加载
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = myAdapter.getFirstMessage();
                queryMessages(msg);
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initBottomView() {
        message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                    scrollToBottom();
                }
                return false;
            }
        });

    }

    /**
     * 消息发送监听器
     */
    public MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            //Logger.i("onProgress：" + value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            myAdapter.addMessage(msg);
            message.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            myAdapter.notifyDataSetChanged();
            message.setText("");
            //java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.TextView.setText(java.lang.CharSequence)' on a null object reference
            scrollToBottom();
            if (e != null) {
                toast(e.getMessage());
            }
        }
    };

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(myAdapter.getItemCount() - 1, 0);
    }

    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     *
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg) {
        //TODO 消息：5.2、查询指定会话的消息记录
        //最多6条
        mConversationManager.queryMessages(msg, 5, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                sw_refresh.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        myAdapter.addMessages(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    private void initRecyclerView(List<List<String>> mDatas) {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        record_list.setLayoutManager(linearLayoutManager);
        record_list.setHasFixedSize(true);
        myAdapter = new MyListAdapter1(this, mDatas);
        record_list.setAdapter(myAdapter);
        record_list.setItemAnimator(new DefaultItemAnimator());
        record_list.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.HORIZONTAL));
    }

    private List<List<String>> getData(String username) {

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


    public void goback(View view) {
        this.finish();
    }

    public void Vedio(View view) {
        Intent intent = new Intent();
        //指定动作，启动相机
        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        mFileName2 = getExternalCacheDir().getAbsolutePath();
        mFileName2 += "/" + Calendar.getInstance().getTimeInMillis() + ".mp4";
        File mVideoFile = new File(mFileName2);
        fileUri2 = FileProvider.getUriForFile(this, "edu.bjtu.gymclub.wezone.fileprovider", mVideoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri2);

        //启动相机并要求返回结果
        startActivityForResult(intent, 1);

    }

    public void Photo(View view) {
        Intent intent = new Intent();
//指定动作，启动相机
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        mFileName1 = getExternalCacheDir().getAbsolutePath();
        mFileName1 += "/" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        File mVideoFile = new File(mFileName1);
        fileUri = FileProvider.getUriForFile(this, "edu.bjtu.gymclub.wezone.fileprovider", mVideoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, 2);
    }

    public void onRecord() {
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/" + Calendar.getInstance().getTimeInMillis() + ".amr";

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(this, "创建录音失败", Toast.LENGTH_SHORT).show();
        }

        mRecorder.start();
        Toast.makeText(this, "开始录制!", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1 && resultCode == RESULT_OK) {


            if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                toast("尚未连接IM服务器");
                return;
            }
            sendLocalVideoMessage(mFileName2);

            //mVideoView.setVideoURI(videoUri);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                toast("尚未连接IM服务器");
                return;
            }
            sendLocalImageMessage(mFileName1);
        } else {
            Log.e("niaho", Integer.toString(resultCode));
        }
    }

    /**
     * 发送本地图片文件
     */
    public void sendLocalImageMessage(String photeUrl) {
        //TODO 发送消息：6.2、发送本地图片消息
        //正常情况下，需要调用系统的图库或拍照功能获取到图片的本地地址，开发者只需要将本地的文件地址传过去就可以发送文件类型的消息
        BmobIMImageMessage image = new BmobIMImageMessage(photeUrl);
        mConversationManager.sendMessage(image, listener);
    }

    /**
     * 发送本地视频文件
     */
    private void sendLocalVideoMessage(String videoUrl) {
        BmobIMVideoMessage video = new BmobIMVideoMessage(videoUrl);
        //TODO 发送消息：6.6、发送本地视频文件消息
        mConversationManager.sendMessage(video, listener);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        //Logger.i("聊天页面接收到消息：" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i = 0; i < list.size(); i++) {
            addMessage2Chat(list.get(i));
        }

    }

    /**
     * 添加消息到聊天界面中
     *
     * @param event
     */
    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (mConversationManager != null && event != null && mConversationManager.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            if (myAdapter.findPosition(msg) < 0) {//如果未添加到界面中
                int last = myAdapter.getItemCount();
                myAdapter.addMessage(last, msg);
                //更新该会话下面的已读状态
                mConversationManager.updateReceiveStatus(msg);
            }
            scrollToBottom();
        } else {

        }
    }


    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //清理资源
        if (recordManager != null) {
            recordManager.clear();
        }
        //TODO 消息：5.4、更新此会话的所有消息为已读状态
        if (mConversationManager != null) {
            mConversationManager.updateLocalCache();
        }
        hideSoftInputView();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //同意权限申请
                    Toast.makeText(this, "权限被接受了", Toast.LENGTH_SHORT).show();
                } else { //拒绝权限申请
                    Toast.makeText(this, "权限被拒绝了", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
