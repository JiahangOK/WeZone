package edu.bjtu.gymclub.wezone;

import android.content.Intent;
import android.support.annotation.MainThread;
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

public class LoginActivity extends AppCompatActivity {
    EditText user_name_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        user_name_edit= findViewById(R.id.user_name);

        Button login_button = findViewById(R.id.login_button_id);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = user_name_edit.getText().toString();
                if(user_name.equals("用户名")||user_name.equals("")){
                    Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();

                }else{
                    Intent intent;
                    intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    BmobIM.connect(user_name_edit.getText().toString(), new ConnectListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                Toast.makeText(LoginActivity.this, "服务器连接成功", Toast.LENGTH_SHORT).show();
                                Log.i("TAG","服务器连接成功");
                            }else {
                                Log.i("TAG",e.getMessage()+"  "+e.getErrorCode());
                            }
                        }
                    });
                    startActivity(intent);
                }

            }
        });

        Button register_button = findViewById(R.id.register_button_id);
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
