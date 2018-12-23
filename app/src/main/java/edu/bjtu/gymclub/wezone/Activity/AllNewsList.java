package edu.bjtu.gymclub.wezone.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.bjtu.gymclub.wezone.Adapter.MyListAdapter;
import edu.bjtu.gymclub.wezone.R;

public class AllNewsList extends Fragment {
    private RecyclerView newslist;
    private Context context;
    private String choice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.all_news_list,null);
        context = getActivity();

        Bundle bundle = getArguments();
        String choice = bundle.getString("choice");

        newslist = view.findViewById(R.id.news_list);

        List<List<String>> data = getData(choice);
        initRecyclerView(data);
        return view;
    }

    private void initRecyclerView(List<List<String>> mDatas){
        //设置布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newslist.setLayoutManager(linearLayoutManager);
        newslist.setHasFixedSize(true);
        newslist.setAdapter(new MyListAdapter(context,mDatas));
        newslist.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.HORIZONTAL));
    }

    private List<List<String>> getData(String choice){

        List<List<String>> mDatas = new ArrayList<List<String>>();


        if(choice.equals("now")){
            List<String> user1 = new ArrayList<String>();
            user1.add("中型大猩猩");
            user1.add("猴子你在干什么？");
            user1.add("15:10");
            List<String> user2 = new ArrayList<String>();
            user2.add("巨型大猩猩");
            user2.add("猴子你不回我消息你死定了！！！！！！！！！！！！");
            user2.add("16:10");
            mDatas.add(user1);
            mDatas.add(user2);
        }else{
            List<String> user1 = new ArrayList<String>();
            user1.add("老母猿");
            user1.add("快给我滚回来");
            user1.add("12:10");
            mDatas.add(user1);
        }

        return mDatas;
    }
}
