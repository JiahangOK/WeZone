package edu.bjtu.gymclub.wezone.Activity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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

import java.util.ArrayList;
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

//        final View view_Bottom;
//        final View root_View;
//        view_Bottom = findViewById(R.id.view_button);
//        root_View = findViewById(R.id.root_View);
//        root_View.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect r = new Rect();
//                root_View.getWindowVisibleDisplayFrame(r);
//                int screenHeight = root_View.getRootView().getHeight();
//                int heightDifference = screenHeight - (r.bottom - r.top);
//                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,0);
//                if(heightDifference>150) params.height=heightDifference;
//                else params.height=2;
//                view_Bottom.setLayoutParams(params);
//            }
//        });




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
