package edu.bjtu.gymclub.wezone.Model;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;
import edu.bjtu.gymclub.wezone.Entity.User;

/**
 * @author :smile
 * @project:QueryUserListener
 * @date :2016-02-01-16:23
 */
public abstract class QueryUserListener extends BmobListener1<User> {

    public abstract void done(User s, BmobException e);

    @Override
    protected void postDone(User o, BmobException e) {
        done(o, e);
    }
}
