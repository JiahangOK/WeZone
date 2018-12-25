package edu.bjtu.gymclub.wezone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.bmob.newim.BmobIM;
import edu.bjtu.gymclub.wezone.Model.UserModel;
import edu.bjtu.gymclub.wezone.R;

public class PersonalFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment, null);
        ImageButton security = view.findViewById(R.id.securitySetting);
        ImageButton inform = view.findViewById(R.id.informSetting);
        Button logout = view.findViewById(R.id.logout);
        TextView textView5 = view.findViewById(R.id.textView5);
        textView5.setText(UserModel.getInstance().getCurrentUser().getUsername());
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent();
                intent.setClass(getActivity(), SecurityActivity.class);
                startActivity(intent);

            }
        });
        inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent();
                intent.setClass(getActivity(), InformActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel.getInstance().logout();
                //TODO 连接：3.2、退出登录需要断开与IM服务器的连接
                BmobIM.getInstance().disConnect();
                getActivity().finish();
                Intent intent;
                intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
