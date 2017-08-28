package com.bokun.bkjcb.chengtou;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.chengtou.Util.Constants;
import com.bokun.bkjcb.chengtou.Util.SPUtils;


public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private EditText mEmailView;
    private EditText mPasswordView;
    private ProgressDialog dialog;
    private CheckBox checkBox;
    private EditText newIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");
        mEmailView = (EditText) findViewById(R.id.username);
        checkBox = (CheckBox) findViewById(R.id.rem_btn);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoginActivity.this, "O(∩_∩)O", Toast.LENGTH_SHORT).show();

                attemptLogin();
            }
        });

        String userName = (String) SPUtils.get(this, "UserName", "");

        if (!TextUtils.isEmpty(userName)) {
            checkBox.setChecked(true);
            mEmailView.setText(userName);
            mPasswordView.setText((String) SPUtils.get(this, "Password", ""));
            button.requestFocus();
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            showProgress();
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mPasswordView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private boolean isEmailValid(String email) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main:
                creatDialog();
                break;
        }
        return true;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return 1;
        }

        @Override
        protected void onPostExecute(Integer success) {
            mAuthTask = null;
            hiddenProgress();
            if (success == 1) {
                remberInfo();
                toMainActivity();
                finish();
            } else if (success == 0) {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            } else {
                mEmailView.setError(getString(R.string.error_field_required));
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            hiddenProgress();
        }
    }

    private void showProgress() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("登录中");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    private void hiddenProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void toMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void remberInfo() {
        if (checkBox.isChecked()) {
            SPUtils.put(LoginActivity.this, "UserName", mEmailView.getText().toString());
            SPUtils.put(LoginActivity.this, "Password", mPasswordView.getText().toString());
        } else {
            SPUtils.put(LoginActivity.this, "UserName", "");
            SPUtils.put(LoginActivity.this, "Password", "");
        }
    }

    private void creatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更换ip");
        View view = getLayoutInflater().inflate(R.layout.change_ip, null);
        TextView old = (TextView) view.findViewById(R.id.old_ip);
        newIp = (EditText) view.findViewById(R.id.new_ip);
        old.setText("当前ip:" + (String) SPUtils.get(this, "IP", Constants.IP_1));
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = newIp.getText().toString();
                if (!s.equals("")){
                    SPUtils.put(LoginActivity.this, "IP", s);
                    Constants.IP_1 = s;
                    Toast.makeText(LoginActivity.this, "ip:" + Constants.IP_1, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}

