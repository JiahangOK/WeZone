package edu.bjtu.gymclub.wezone.Bmob;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import edu.bjtu.gymclub.wezone.Activity.ChatActivity;

//集成一个消息接收类，进行消息的接收
public class ImMessageHandler extends BmobIMMessageHandler{

    @Override
    public void onMessageReceive(MessageEvent messageEvent) {
        super.onMessageReceive(messageEvent);
        //在线消息
        ChatActivity.tv_message.append("接收到："+messageEvent.getMessage().getContent()+"\n");
    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);
    }
}