package edu.bjtu.gymclub.wezone.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import edu.bjtu.gymclub.wezone.Adapter.OnRecyclerViewListener;
import edu.bjtu.gymclub.wezone.Adapter.SearchUserAdapter;
import edu.bjtu.gymclub.wezone.Entity.User;
import edu.bjtu.gymclub.wezone.Model.BaseModel;
import edu.bjtu.gymclub.wezone.Model.UserModel;
import edu.bjtu.gymclub.wezone.R;

public class AddFriendActivity extends BaseActivity {

    private EditText add_friend_username;
    private Button add_friend_btn;
    private SwipeRefreshLayout sw_refresh;
    private RecyclerView rc_view;
    private LinearLayoutManager layoutManager;
    private SearchUserAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);
        add_friend_username = (EditText)findViewById(R.id.add_friend_username);
        add_friend_btn = (Button) findViewById(R.id.add_friend_btn);
        add_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sw_refresh.setRefreshing(true);
                query();
            }
        });
        sw_refresh = (SwipeRefreshLayout)findViewById(R.id.sw_refresh);
        rc_view = (RecyclerView)findViewById(R.id.rc_view);
        adapter = new SearchUserAdapter();
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        rc_view.setAdapter(adapter);//绑定adapter
        sw_refresh.setEnabled(true);
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                toast("Oops!");
            }

            @Override
            public boolean onItemLongClick(int position) {
                return true;
            }
        });


    }

    public void query() {
        String name = add_friend_username.getText().toString();
        if (TextUtils.isEmpty(name)) {
            sw_refresh.setRefreshing(false);
            toast("请填写用户名");
            return;
        }
        UserModel.getInstance().queryUsers(name, BaseModel.DEFAULT_LIMIT,
                new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            sw_refresh.setRefreshing(false);
                            adapter.setDatas(list);
                            adapter.notifyDataSetChanged();
                        } else {
                            sw_refresh.setRefreshing(false);
                            adapter.setDatas(null);
                            adapter.notifyDataSetChanged();
                            Logger.e(e);
                        }
                    }
                }


        );
    }
}
