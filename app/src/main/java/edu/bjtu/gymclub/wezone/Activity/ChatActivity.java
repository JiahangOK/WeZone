package edu.bjtu.gymclub.wezone.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
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


    private void initSwipeLayout() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        record_list.setLayoutManager(layoutManager);
        myAdapter = new MyListAdapter1(this, mConversationManager);
        record_list.setAdapter(myAdapter);
        queryMessages(null);
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
            Logger.i("onProgress：" + value);
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
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.CAMERA}, CAMERA_OK);
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
        } else {
            Log.e("niaho", Integer.toString(resultCode));
        }
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        Logger.i("聊天页面接收到消息：" + list.size());
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
                myAdapter.addMessage(msg);
                //更新该会话下面的已读状态
                mConversationManager.updateReceiveStatus(msg);
            }
            scrollToBottom();
        } else {
            Logger.i("不是与当前聊天对象的消息");
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
