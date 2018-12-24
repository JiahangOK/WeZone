package edu.bjtu.gymclub.wezone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import edu.bjtu.gymclub.wezone.Model.BaseModel;
import edu.bjtu.gymclub.wezone.Model.UserModel;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.event.FinishEvent;

public class RegisterActivity extends BaseActivity {
    private TextView et_username;
    private TextView et_pw;
    private TextView et_ensure;
    private Button register_ok_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        et_username = (TextView) findViewById(R.id.et_username);
        et_pw = (TextView) findViewById(R.id.et_pw);
        et_ensure = (TextView) findViewById(R.id.et_ensure);
        register_ok_btn = (Button)findViewById(R.id.register_ok_btn);
        register_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String pw = et_pw.getText().toString();
                String pw_en = et_ensure.getText().toString();
                UserModel.getInstance().register(et_username.getText().toString(), et_pw.getText().toString(), et_ensure.getText().toString(), new LogInListener() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            EventBus.getDefault().post(new FinishEvent());
                            startActivity(LoginActivity.class, null, true);
                        } else {
                            if (e.getErrorCode() == BaseModel.CODE_NOT_EQUAL) {
                                et_ensure.setText("");
                            }
                            toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                        }
                    }
                });
            }
        });

    }
}
