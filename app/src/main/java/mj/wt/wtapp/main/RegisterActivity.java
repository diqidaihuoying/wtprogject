package mj.wt.wtapp.main;

import android.app.ProgressDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.basic.utils.SnackbarUtil;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import mj.wt.wtapp.R;
import mj.wt.wtapp.base.TActivity;
import mj.wt.wtapp.huanxin.utils.PreferenceManager;

public class RegisterActivity extends TActivity {
    private CoordinatorLayout coordinatorLayout;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText confirmPwdEditText;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        super.initView();
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        userNameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        confirmPwdEditText = (EditText) findViewById(R.id.confirm_password);
    }
    public void register(View view) {
        final String username = userNameEditText.getText().toString().trim();
        final String pwd = passwordEditText.getText().toString().trim();
        String confirm_pwd = confirmPwdEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            SnackbarUtil.showSnackBar(coordinatorLayout,"User name cannot be empty");
            userNameEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            SnackbarUtil.showSnackBar(coordinatorLayout,"Pass word cannot be empty");
            passwordEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            SnackbarUtil.showSnackBar(coordinatorLayout,"Confirm_pwd cannot be empty");
            confirmPwdEditText.requestFocus();
            return;
        } else if (!pwd.equals(confirm_pwd)) {
            SnackbarUtil.showSnackBar(coordinatorLayout,"Inconsistent password, please enter again!");
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Register..");
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        // call method in SDK
                        EMClient.getInstance().createAccount(username, pwd);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegisterActivity.this.isFinishing())
                                    pd.dismiss();
                                // save current user
                                PreferenceManager.getInstance().setCurrentUserName(username);
                                SnackbarUtil.showSnackBar(coordinatorLayout,"Regiseter Successfully");
                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegisterActivity.this.isFinishing())
                                    pd.dismiss();
                                int errorCode=e.getErrorCode();
                                if(errorCode== EMError.NETWORK_ERROR){
                                    SnackbarUtil.showSnackBar(coordinatorLayout,"network_anomalies");
                                }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                    SnackbarUtil.showSnackBar(coordinatorLayout,"User_already_exists");
                                }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                    SnackbarUtil.showSnackBar(coordinatorLayout,"registration_failed_without_permission");
                                }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                    SnackbarUtil.showSnackBar(coordinatorLayout,"illegal_user_name");
                                }else{
                                    SnackbarUtil.showSnackBar(coordinatorLayout,"Registration_failed");
                                }
                            }
                        });
                    }
                }
            }).start();

        }
    }

    public void back(View view) {
        finish();
    }
}
