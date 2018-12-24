package edu.bjtu.gymclub.wezone.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.util.IMLogger;
import edu.bjtu.gymclub.wezone.Entity.Conversation;
import edu.bjtu.gymclub.wezone.Entity.NewFriendConversation;
import edu.bjtu.gymclub.wezone.Entity.PrivateConversation;
import edu.bjtu.gymclub.wezone.R;
import edu.bjtu.gymclub.wezone.db.NewFriend;
import edu.bjtu.gymclub.wezone.db.NewFriendManager;
import edu.bjtu.gymclub.wezone.event.RefreshEvent;


public class TalkFragment extends Fragment {

    //获取内容
    private Context context;

    //“所有消息”按钮
    private Button allmessage;
    //“稍后处理”按钮
    private Button laterdeal;
    //“键盘”按钮
    private Button keyboard;
    //“录音”按钮
    private Button voice;
    //spinner
    private Button spinner;
    //“搜索”View
    private SearchView searchView;
    //fragment管理
    private FragmentManager manager;
    private FragmentTransaction transaction;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.talk_fragment,null);
        //绑定内容
        context = view.getContext();

        //fragment
        manager = getChildFragmentManager();
        transaction = manager.beginTransaction();
        AllNewsList newsList = new AllNewsList();
        Bundle bundle = new Bundle();
        bundle.putString("choice","now");
        newsList.setArguments(bundle);
        transaction.replace(R.id.news_list,newsList);
        transaction.commit();


        //绑定按钮
        allmessage = view.findViewById(R.id.allmessage);
        laterdeal = view.findViewById(R.id.laterdeal);
        keyboard = view.findViewById(R.id.keyboard);
        voice = view.findViewById(R.id.voice);

        //绑定spinner
        spinner = view.findViewById(R.id.spinner);

        //绑定View
        searchView = view.findViewById(R.id.search_view);

//        //创建popupWindws
//        View menu = LayoutInflater.from(context).inflate(R.layout.menu, null, false);
//        PopupWindow window=new PopupWindow(menu, 100, 100, true);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.setOutsideTouchable(true);
//        window.setTouchable(true);
//        // 显示PopupWindow，其中：
//        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//        window.showAsDropDown(anchor, xoff, yoff);
//        // 或者也可以调用此方法显示PopupWindow，其中：
//        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
//        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
//        // window.showAtLocation(parent, gravity, x, y);

        //按钮”所有消息的点击函数“
        allmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allmessage.setBackgroundResource(R.drawable.radio_button1);
                laterdeal.setBackgroundResource(R.drawable.radio_button);
                transaction = manager.beginTransaction();
                AllNewsList newsList = new AllNewsList();
                Bundle bundle = new Bundle();
                bundle.putString("choice","now");
                newsList.setArguments(bundle);
                transaction.replace(R.id.news_list,newsList);
                transaction.commit();
            }
        });

        //按钮“稍后处理”的点击函数
        laterdeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allmessage.setBackgroundResource(R.drawable.radio_button2);
                laterdeal.setBackgroundResource(R.drawable.radio_button3);
                transaction = manager.beginTransaction();
                AllNewsList newsList = new AllNewsList();
                Bundle bundle = new Bundle();
                bundle.putString("choice","wait");
                newsList.setArguments(bundle);
                transaction.replace(R.id.news_list,newsList);
                transaction.commit();
            }
        });

        //转换录音的按钮的监听函数
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setBackgroundResource(R.drawable.list1);
                voice.setBackgroundResource(R.drawable.list3);
            }
        });

        //转换成为键盘按钮监听函数
        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setBackgroundResource(R.drawable.list);
                voice.setBackgroundResource(R.drawable.list2);
            }
        });

        //添加好友的监听函数
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent();
                intent.setClass(getActivity(), AddFriendActivity.class);
                startActivity(intent);
            }
        });

        //搜索点击的view的监听函数
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(searchView != null){
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    }
                    searchView.clearFocus(); // 不获取焦点
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText)){

                }else{

                }
                return false;
            }
        });

        return view;
    }


}
