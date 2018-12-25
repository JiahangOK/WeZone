package edu.bjtu.gymclub.wezone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.github.promeg.pinyinhelper.Pinyin;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import edu.bjtu.gymclub.wezone.Adapter.ContactAdapter;
import edu.bjtu.gymclub.wezone.Adapter.OnRecyclerViewListener;
import edu.bjtu.gymclub.wezone.Adapter.SearchUserAdapter;
import edu.bjtu.gymclub.wezone.Entity.Friend;
import edu.bjtu.gymclub.wezone.Entity.User;
import edu.bjtu.gymclub.wezone.Model.UserModel;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.event.RefreshEvent;


public class ContactFragment extends Fragment {
    RecyclerView rc_view;

    SwipeRefreshLayout sw_refresh;

    ContactAdapter adapter;
    LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, null);
        sw_refresh = (SwipeRefreshLayout) view.findViewById(R.id.sw_refresh);
        rc_view = (RecyclerView) view.findViewById(R.id.rc_view);
        adapter = new ContactAdapter();
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        setListener();

        return view;
    }

    private void setListener() {
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Friend friend = adapter.getItem(position);
                User user = friend.getFriendUser();
                BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                //TODO 会话：4.1、创建一个常态会话入口，好友聊天
                BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("c", conversationEntrance);
                Intent intent;
                intent = new Intent();
                intent.setClass(getActivity(), ChatActivity.class);
                intent.putExtra(getActivity().getPackageName(), bundle);
                getActivity().startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(final int position) {
                com.orhanobut.logger.Logger.i("长按" + position);
                if (position == 0) {
                    return true;
                }

                Friend friend = adapter.getItem(position);

                UserModel.getInstance().deleteFriend(friend,
                        new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getActivity(), "好友删除成功", Toast.LENGTH_SHORT).show();
                                    adapter.remove(position);
                                } else {
                                    Toast.makeText(getActivity(), "好友删除失败：" + e.getErrorCode() + ",s =" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                return true;
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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        sw_refresh.setRefreshing(true);
        query();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        //重新刷新列表
        com.orhanobut.logger.Logger.i("---联系人界面接收到自定义消息---");
        adapter.notifyDataSetChanged();
    }

    /**
     * 查询本地会话
     */
    public void query() {
        UserModel.getInstance().queryFriends(
                new FindListener<Friend>() {
                    @Override
                    public void done(List<Friend> list, BmobException e) {
                        if (e == null) {
                            List<Friend> friends = new ArrayList<>();
                            friends.clear();
                            //添加首字母
                            for (int i = 0; i < list.size(); i++) {
                                Friend friend = list.get(i);
                                String username = friend.getFriendUser().getUsername();
                                if (username != null) {
                                    String pinyin = Pinyin.toPinyin(username.charAt(0));
                                    friend.setPinyin(pinyin.substring(0, 1).toUpperCase());
                                    friends.add(friend);
                                }
                            }
                            adapter.bindDatas(friends);
                            adapter.notifyDataSetChanged();
                            sw_refresh.setRefreshing(false);
                        } else {
                            adapter.bindDatas(null);
                            adapter.notifyDataSetChanged();
                            sw_refresh.setRefreshing(false);
                            Logger.e(e);
                        }
                    }
                }


        );
    }
}
