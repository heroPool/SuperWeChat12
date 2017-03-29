package cn.ucai.superwechat.net;

import android.content.Context;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.utils.OkHttpUtils;
import cn.ucai.superwechat.utils.Result;

/**
 * Created by Administrator on 2017/3/29.
 */

public class UnRegisterModel implements IUnRegisterModel {
    @Override
    public void register(Context context, String username, OnCompleteListener listener) {
        OkHttpUtils<Result> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.setRequestUrl(I.REQUEST_UNREGISTER)
                .addParam(I.User.USER_NAME, username)
                .targetClass(Result.class)
                .execute(listener);
    }
}
