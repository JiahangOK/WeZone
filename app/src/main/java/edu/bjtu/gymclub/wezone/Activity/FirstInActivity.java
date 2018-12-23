package edu.bjtu.gymclub.wezone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import edu.bjtu.gymclub.wezone.R;

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
