package edu.bjtu.gymclub.wezone.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import edu.bjtu.gymclub.wezone.Adapter.MyListAdapter;
import edu.bjtu.gymclub.wezone.Adapter.OnRecyclerViewListener;
import edu.bjtu.gymclub.wezone.Entity.Conversation;
import edu.bjtu.gymclub.wezone.Entity.NewFriendConversation;
import edu.bjtu.gymclub.wezone.Entity.PrivateConversation;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.db.NewFriend;
import edu.bjtu.gymclub.wezone.db.NewFriendManager;
import edu.bjtu.gymclub.wezone.event.RefreshEvent;

public class AllNewsList extends Fragment {

    private RecyclerView newslist;

    private Context context;
    private String choice;


    private SwipeRefreshLayout sw_refresh;
    private MyListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_news_list, null);
        context = getActivity();

        newslist = view.findViewById(R.id.news_list);
        sw_refresh = view.findViewById(R.id.sw_refresh);


        Bundle bundle = getArguments();
        String choice = bundle.getString("choice");


        //List<List<String>> data = getData(choice);
        List<Conversation> data = new ArrayList<Conversation>();
        adapter = new MyListAdapter(getActivity(), data);
        newslist.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        newslist.setLayoutManager(linearLayoutManager);
        query();
        sw_refresh.setEnabled(true);
        setListener();
        //initRecyclerView(data);
        return view;
    }

//    private void initRecyclerView(List<List<String>> mDatas) {
//        //设置布局管理器
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        newslist.setLayoutManager(linearLayoutManager);
//        newslist.setHasFixedSize(true);
//        newslist.setAdapter(new MyListAdapter(context, mDatas));
//        newslist.addItemDecoration(new DividerItemDecoration(
//                getContext(), DividerItemDecoration.HORIZONTAL));
//    }
//
//    private List<List<String>> getData(String choice) {
//
//        List<List<String>> mDatas = new ArrayList<List<String>>();
//
//
//        if (choice.equals("now")) {
//            List<String> user1 = new ArrayList<String>();
//            user1.add("中型大猩猩");
//            user1.add("猴子你在干什么？");
//            user1.add("15:10");
//            List<String> user2 = new ArrayList<String>();
//            user2.add("巨型大猩猩");
//            user2.add("猴子你不回我消息你死定了！！！！！！！！！！！！");
//            user2.add("16:10");
//            mDatas.add(user1);
//            mDatas.add(user2);
//        } else {
//            List<String> user1 = new ArrayList<String>();
//            user1.add("老母猿");
//            user1.add("快给我滚回来");
//            user1.add("12:10");
//            mDatas.add(user1);
//        }
//
//        return mDatas;
//    }

    private void setListener() {
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
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        query();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 查询本地会话
     */
    public void query() {
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
        sw_refresh.setRefreshing(false);
    }

    /**
     * 获取会话列表的数据：增加新朋友会话
     *
     * @return
     */
    private List<Conversation> getConversations() {
        //添加会话
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        //TODO 会话：4.2、查询全部会话
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if (list != null && list.size() > 0) {
            for (BmobIMConversation item : list) {
                switch (item.getConversationType()) {
                    case 1://私聊
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }
        //添加新朋友会话-获取好友请求表中最新一条记录
        List<NewFriend> friends = NewFriendManager.getInstance(getActivity()).getAllNewFriend();
        if (friends != null && friends.size() > 0) {
            conversationList.add(new NewFriendConversation(friends.get(0)));
        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        com.orhanobut.logger.Logger.i("---会话页接收到自定义消息---");
        //因为新增`新朋友`这种会话类型
//        adapter.bindDatas(getConversations());
//        adapter.notifyDataSetChanged();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        //重新刷新列表
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }

    /**
     * 注册消息接收事件
     *
     * @param event 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     *              2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        //重新获取本地消息并刷新列表
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }
}
