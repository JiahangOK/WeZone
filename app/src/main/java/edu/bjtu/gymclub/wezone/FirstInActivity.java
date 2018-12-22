package edu.bjtu.gymclub.wezone;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.exception.BmobException;

public class FirstInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.first_in);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(FirstInActivity.this, LoginActivity.class);

                startActivity(intent);
                FirstInActivity.this.finish();

            }
        }, 1000);
    }
}
