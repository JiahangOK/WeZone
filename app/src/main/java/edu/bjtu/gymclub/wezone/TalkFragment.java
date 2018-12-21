package edu.bjtu.gymclub.wezone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ChangeTransform;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TalkFragment extends Fragment {

    //“所有消息”按钮
    private Button allmessage;
    //“稍后处理”按钮
    private Button laterdeal;
    //“键盘”按钮
    private Button keyboard;
    //“录音”按钮
    private Button voice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.talk_fragment,null);
        Button talk = view.findViewById(R.id.talk_1);
        Button add_button = view.findViewById(R.id.add_button);
        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent();
                intent.setClass(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent();
                intent.setClass(getActivity(), AddFriendActivity.class);
                startActivity(intent);
            }
        });

        //绑定按钮
        allmessage = view.findViewById(R.id.allmessage);
        laterdeal = view.findViewById(R.id.laterdeal);
        keyboard = view.findViewById(R.id.keyboard);
        voice = view.findViewById(R.id.voice);

        //按钮”所有消息的点击函数“
        allmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allmessage.setBackgroundResource(R.drawable.radio_button1);
                laterdeal.setBackgroundResource(R.drawable.radio_button);
            }
        });

        //按钮“稍后处理”的点击函数
        laterdeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allmessage.setBackgroundResource(R.drawable.radio_button2);
                laterdeal.setBackgroundResource(R.drawable.radio_button3);
            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setBackgroundResource(R.drawable.list1);
                voice.setBackgroundResource(R.drawable.list3);
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setBackgroundResource(R.drawable.list);
                voice.setBackgroundResource(R.drawable.list2);
            }
        });
        return view;
    }

}
