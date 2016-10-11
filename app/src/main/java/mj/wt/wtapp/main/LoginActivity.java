package mj.wt.wtapp.main;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.basic.utils.NetWorkUtils;
import com.example.basic.utils.SnackbarUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import mj.wt.wtapp.R;
import mj.wt.wtapp.base.TActivity;
import mj.wt.wtapp.huanxin.utils.PreferenceManager;

public class LoginActivity extends TActivity {

    private CoordinatorLayout coordinatorLayout;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private boolean progressShow;
    private boolean autoLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // enter the main activity if already logged in
        if (EMClient.getInstance().isLoggedInBefore()) {
            autoLogin = true;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            return;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }
    @Override
    public void initView() {
        super.initView();
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        // if user changed, clear the password
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (PreferenceManager.getInstance().getCurrentUsername()!= null) {
            usernameEditText.setText(PreferenceManager.getInstance().getCurrentUsername());
        }
    }


    /**
     * login
     * @param view
     */
    public void login(View view) {
        if (!NetWorkUtils.isNetWorkConnected(this)) {
            SnackbarUtil.showSnackBar(coordinatorLayout, "The network is not available");
            return;
        }
        String currentUsername = usernameEditText.getText().toString().trim();
        String currentPassword = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(currentUsername)) {
            SnackbarUtil.showSnackBar(coordinatorLayout, "User name cannot be empty!");
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            SnackbarUtil.showSnackBar(coordinatorLayout, "Pass word cannot be empty!");
            return;
        }

        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage("logining..");
        pd.show();


        // reset current user name before login
        PreferenceManager.getInstance().setCurrentUserName(currentUsername);

        final long start = System.currentTimeMillis();
        // call login method
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);

                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        SnackbarUtil.showSnackBar(coordinatorLayout, "Load Failed: "+message);
                    }
                });
            }
        });
    }


    /**
     * register
     *
     * @param view
     */
    public void register(View view) {
        startActivityForResult(new Intent(this, RegisterActivity.class), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }
}
