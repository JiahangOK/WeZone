package edu.bjtu.gymclub.wezone.Adapter;


import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

import edu.bjtu.gymclub.wezone.Activity.ChatActivity;
import edu.bjtu.gymclub.wezone.R;

public class MyListAdapter1 extends RecyclerView.Adapter<MyListAdapter1.MyViewHolder> {

    private Context mContext;
    private List<List<String>> mDatas;

    public MyListAdapter1(Context context, List<List<String>> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.record_item, parent, false));
        return holder;
    }

    public void addData(int position,List<String> newItem) {
        mDatas.add(position,newItem);
        Log.e("闪退", mDatas.toString());
        //添加动画
        notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String content = mDatas.get(position).get(0);
        String record_type = mDatas.get(position).get(1);
        if(record_type.equals("receive")){
            holder.layout1.setVisibility(View.VISIBLE);
            holder.layout2.setVisibility(View.GONE);
            holder.bubbleTextView1.setText(mDatas.get(position).get(0));
        }else{
            holder.layout2.setVisibility(View.VISIBLE);
            holder.layout1.setVisibility(View.GONE);
            holder.bubbleTextView2.setText(mDatas.get(position).get(0));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layout1;
        private LinearLayout layout2;
        private BubbleTextView bubbleTextView1;
        private BubbleTextView bubbleTextView2;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout1 = itemView.findViewById(R.id.left_record);
            layout2 = itemView.findViewById(R.id.right_record);
            bubbleTextView1 = itemView.findViewById(R.id.receiver_left);
            bubbleTextView2 = itemView.findViewById(R.id.receiver_right);
        }
    }
}
