package cn.ucai.superwechat.net;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/29.
 */

public interface IUnRegisterModel {
    void register(Context context, String username, OnCompleteListener listener);
}
