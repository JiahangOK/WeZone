package edu.bjtu.gymclub.wezone;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    public static TextView tv_message;

    EditText receiver_id;
    EditText chat_message;

    Button send_button;
    boolean isOpenConversation = false;

    BmobIMConversation mBmobIMConversation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        tv_message = (TextView) findViewById(R.id.tv_message);

        receiver_id = (EditText) findViewById(R.id.receiver_id);
        chat_message = (EditText) findViewById(R.id.chat_message);

        send_button = (Button) findViewById(R.id.send_button);
        send_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_button) {

            BmobIMUserInfo info = new BmobIMUserInfo();
            info.setAvatar("填写接收者的头像");
            info.setUserId(receiver_id.getText().toString());
            info.setName("填写接收者的名字");
            BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                @Override
                public void done(BmobIMConversation c, BmobException e) {
                    if (e == null) {
                        isOpenConversation = true;
                        //在此跳转到聊天页面或者直接转化
                        mBmobIMConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                        tv_message.append("发送者：" + chat_message.getText().toString() + "\n");
                        BmobIMTextMessage msg = new BmobIMTextMessage();
                        msg.setContent(chat_message.getText().toString());
                        mBmobIMConversation.sendMessage(msg, new MessageSendListener() {
                            @Override
                            public void done(BmobIMMessage msg, BmobException e) {
                                if (e != null) {
                                    chat_message.setText("");
                                } else {
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ChatActivity.this, "开启会话出错", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }
}
