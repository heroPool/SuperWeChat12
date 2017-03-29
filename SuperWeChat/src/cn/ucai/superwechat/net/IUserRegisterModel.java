package cn.ucai.superwechat.net;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/29.
 */

public interface IUserRegisterModel {
    void register(Context context, String username, String nick, String password, OnCompleteListener<String> listener);
    void login(Context context, String username,  String password, OnCompleteListener<String> listener);
    void unregister(Context context, String username, OnCompleteListener<String> listener);
}
