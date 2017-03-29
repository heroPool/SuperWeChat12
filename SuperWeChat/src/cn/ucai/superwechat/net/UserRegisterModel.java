package cn.ucai.superwechat.net;

import android.content.Context;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.utils.OkHttpUtils;
import cn.ucai.superwechat.utils.Result;


/**
 * Created by Administrator on 2017/3/29.
 */

public class UserRegisterModel implements IUserRegisterModel {
    @Override
    public void register(Context context, String username, String nick, String password, OnCompleteListener listener) {
        OkHttpUtils<Result> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.NICK, nick)
                .addParam(I.User.USER_NAME, username)
                .addParam(I.User.PASSWORD, password)
                .targetClass(Result.class)
                .post()
                .execute(listener);

    }
}
