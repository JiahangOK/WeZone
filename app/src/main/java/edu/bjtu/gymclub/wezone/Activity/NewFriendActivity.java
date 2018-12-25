package edu.bjtu.gymclub.wezone.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;

import edu.bjtu.gymclub.wezone.Adapter.NewFriendAdapter;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.db.NewFriendManager;

/**新朋友
 * @author :smile
 * @project:NewFriendActivity
 * @date :2016-01-25-18:23
 */
public class NewFriendActivity extends BaseActivity {



    RecyclerView rc_view;

    SwipeRefreshLayout sw_refresh;
    NewFriendAdapter adapter;
    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friends_list);

        rc_view =(RecyclerView) findViewById(R.id.rc_view);
        sw_refresh = (SwipeRefreshLayout)findViewById(R.id.sw_refresh);

        adapter = new NewFriendAdapter(this,null);
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        //批量更新未读未认证的消息为已读状态
        NewFriendManager.getInstance(this).updateBatchStatus();
        setListener();
    }

    private void setListener(){
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
      查询本地会话
     */
    public void query(){
        adapter.bindDatas(NewFriendManager.getInstance(this).getAllNewFriend());
        adapter.notifyDataSetChanged();
        sw_refresh.setRefreshing(false);
    }

}
