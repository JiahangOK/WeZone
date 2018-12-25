package edu.bjtu.gymclub.wezone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.app.ActionBar;
import android.app.Activity;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;

import edu.bjtu.gymclub.wezone.R;



public class TopicDetialActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_detail);
        Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent();
                intent.setClass(TopicDetialActivity.this, SendTopicDetailActivity.class);
                startActivity(intent);
            }
        });


    }


}
