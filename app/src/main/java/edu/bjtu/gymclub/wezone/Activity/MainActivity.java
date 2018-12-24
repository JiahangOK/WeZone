package edu.bjtu.gymclub.wezone.Activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import edu.bjtu.gymclub.wezone.Entity.User;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.db.NewFriendManager;
import edu.bjtu.gymclub.wezone.event.RefreshEvent;
import edu.bjtu.gymclub.wezone.util.IMMLeaks;

public class MainActivity extends BaseActivity {
    private FragmentManager manager;
    private FragmentTransaction transaction;
    BottomBar bottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final User user = BmobUser.getCurrentUser(User.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.getAvatar()));
                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                        toast(e.getMessage());
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    toast(status.getMsg());
                    Logger.i(BmobIM.getInstance().getCurrentStatus().getMsg());
                }
            });
        }

        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());


        //去掉系统丑陋的ActionBar
        getSupportActionBar().hide();
    }

    @Override
    protected void initView() {
        super.initView();

        initTab();
    }


    //初始化bottomBar
    private void initTab() {
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);



        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.contentContainer, new TalkFragment());

        transaction.commit();



        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab1: {
                        BottomBarTab tab1 = bottomBar.getTabWithId(R.id.tab1);
                        tab1.removeBadge();
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentContainer, new TalkFragment());
                        transaction.commit();
                        break;
                    }
                    case R.id.tab2: {
                        BottomBarTab tab2 = bottomBar.getTabWithId(R.id.tab2);
                        tab2.removeBadge();
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentContainer, new ContactFragment());
                        transaction.commit();
                        break;
                    }
                    case R.id.tab3: {
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentContainer, new TopicFragment());
                        transaction.commit();
                        break;
                    }
                    case R.id.tab4: {
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentContainer, new PersonalFragment());
                        transaction.commit();
                        break;
                    }
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次进来应用都检查会话和好友请求的情况
        checkRedPoint();
        //进入应用后，通知栏应取消,就是那种离开应用的上方通知栏
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
    }

    /**
     * 注册在线消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.3、通知有在线消息接收
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.4、通知有离线消息接收
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.5、通知有自定义消息接收
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        checkRedPoint();
    }

    /**
     *
     */
    private void checkRedPoint() {
        //TODO 会话：4.4、获取全部会话的未读消息数量
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        if (count > 0) {
            BottomBarTab tab1 = bottomBar.getTabWithId(R.id.tab1);
            tab1.setBadgeCount(count);

        } else {
            BottomBarTab tab1 = bottomBar.getTabWithId(R.id.tab1);
            tab1.removeBadge();
        }
        //TODO 好友管理：是否有好友添加的请求
        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
            BottomBarTab tab2 = bottomBar.getTabWithId(R.id.tab2);
            tab2.setBadgeCount(count);
        } else {
            BottomBarTab tab2 = bottomBar.getTabWithId(R.id.tab2);
            tab2.removeBadge();
        }
    }
}
