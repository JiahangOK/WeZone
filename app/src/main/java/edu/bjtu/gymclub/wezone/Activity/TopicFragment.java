package edu.bjtu.gymclub.wezone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.bjtu.gymclub.wezone.R;

public class TopicFragment extends Fragment {
    @Nullable

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.topic_fragment,null);
         TextView txt_right_title = view.findViewById(R.id.txt_right_title);
         txt_right_title.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent;
                intent = new Intent();
                intent.setClass(getActivity(), SendTopicDetailActivity.class);
                startActivity(intent);
             }
         });

//        Button send = view.findViewById(R.id.send);
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent;
//                intent = new Intent();
//                intent.setClass(getActivity(), SendTopicDetailActivity.class);
//                startActivity(intent);
//            }
//        });
        return view;
    }
}
