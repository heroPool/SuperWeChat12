/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.net.IUnRegisterModel;
import cn.ucai.superwechat.net.OnCompleteListener;
import cn.ucai.superwechat.net.UnRegisterModel;
import cn.ucai.superwechat.net.UserRegisterModel;
import cn.ucai.superwechat.utils.CommonUtils;
import cn.ucai.superwechat.utils.Result;

/**
 * register screen
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.image_login_luancher)
    ImageView imageLoginLuancher;
    @BindView(R.id.iv_nick)
    ImageView ivNick;
    @BindView(R.id.nick)
    EditText userNickEditText;
    @BindView(R.id.iv_username)
    ImageView ivUsername;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.iv_password)
    ImageView ivPassword;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.iv_password2)
    ImageView ivPassword2;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;

    UserRegisterModel usergisterModel;
    IUnRegisterModel ungisterModel;


    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText confirmPwdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_register);
        ButterKnife.bind(this);

        usergisterModel = new UserRegisterModel();
        ungisterModel = new UnRegisterModel();

        userNameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        confirmPwdEditText = (EditText) findViewById(R.id.confirm_password);
    }

    public void register(View view) {
        final String usernick = userNickEditText.getText().toString().trim();
        final String username = userNameEditText.getText().toString().trim();
        final String pwd = passwordEditText.getText().toString().trim();
        String confirm_pwd = confirmPwdEditText.getText().toString().trim();

        if (TextUtils.isEmpty(usernick)) {
            CommonUtils.showShortToast("昵称为空！");
            userNickEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            userNameEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            passwordEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            confirmPwdEditText.requestFocus();
            return;
        } else if (!pwd.equals(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage(getResources().getString(R.string.Is_the_registered));
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    //先进行superwechat端注册
                    usergisterModel.register(RegisterActivity.this, username, usernick, pwd, new OnCompleteListener() {
                        @Override
                        public void onSuccess(Object r) {
                            Result result = (Result) r;

                            if (result != null && result.isRetMsg()) {
                                //superwechat端注册成功再进行环信端注册
                                EMCClientregister(username, pwd, pd);
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(RegisterActivity.this, "注册失败" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();

        }
    }

    private void EMCClientregister(final String username, String pwd, final ProgressDialog pd) {
        try {
            // call method in SDK
            EMClient.getInstance().createAccount(username, pwd);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (!RegisterActivity.this.isFinishing())
                        pd.dismiss();
                    // save current user
                    SuperWeChatHelper.getInstance().setCurrentUserName(username);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } catch (final HyphenateException e) {
            runOnUiThread(new Runnable() {
                public void run() {
                    //环信注册失败，超级微信端再取消注册
                    SuperWeChatClientungister(username);

                    if (!RegisterActivity.this.isFinishing())
                        pd.dismiss();
                    int errorCode = e.getErrorCode();
                    if (errorCode == EMError.NETWORK_ERROR) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                    } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                    } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                    } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void SuperWeChatClientungister(String username) {
        ungisterModel.register(RegisterActivity.this, username, new OnCompleteListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }


    public void back(View view) {
        finish();
    }

}
