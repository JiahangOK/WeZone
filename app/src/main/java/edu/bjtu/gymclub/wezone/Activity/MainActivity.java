package edu.bjtu.gymclub.wezone.Activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import edu.bjtu.gymclub.wezone.R;

public class MainActivity extends AppCompatActivity{
    private FragmentManager manager;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.contentContainer,new TalkFragment());

        transaction.commit();


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab1:{
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentContainer,new TalkFragment());
                        transaction.commit();
                        break;
                    }
                    case R.id.tab2:{
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentContainer,new ContactFragment());
                        transaction.commit();
                        break;
                    }
                    case R.id.tab3:{
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentContainer,new TopicFragment());
                        transaction.commit();
                        break;
                    }
                    case R.id.tab4:{
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentContainer,new PersonalFragment());
                        transaction.commit();
                        break;
                    }
                }

            }
        });

        //去掉系统丑陋的ActionBar
        getSupportActionBar().hide();
    }
}
