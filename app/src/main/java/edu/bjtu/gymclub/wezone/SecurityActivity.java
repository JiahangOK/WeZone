package edu.bjtu.gymclub.wezone;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SecurityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security);
        Button updateOk = findViewById(R.id.update_ok);
        updateOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecurityActivity.this.finish();
            }
        });
    }
}
