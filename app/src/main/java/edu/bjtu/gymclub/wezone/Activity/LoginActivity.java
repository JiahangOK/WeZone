package edu.bjtu.gymclub.wezone.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import edu.bjtu.gymclub.wezone.Model.UserModel;
import edu.bjtu.gymclub.wezone.R;

public class LoginActivity extends BaseActivity {
    private EditText user_name_edit;
    private EditText password_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        user_name_edit= (EditText) findViewById(R.id.user_name);
        password_edit= (EditText) findViewById(R.id.password_edit);

        Button login_button = (Button) findViewById(R.id.login_button_id);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel.getInstance().login(user_name_edit.getText().toString(), password_edit.getText().toString(), new LogInListener() {

                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            //登录成功
                            startActivity(MainActivity.class, null, true);
                        } else {
                            toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                        }
                    }
                });

            }
        });

        Button register_button = (Button) findViewById(R.id.register_button_id);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
}
