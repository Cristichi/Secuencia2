package com.cristichi.secuencia2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cristichi.secuencia2.ddbb.ReturnLogin;
import com.cristichi.secuencia2.ddbb.Session;

public class LoginActivity extends ActivityWithMusic {

    private EditText etEmail;
    private EditText etPass;

    private TextView tvError;

    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etOldPass1);

        tvError = findViewById(R.id.tvError);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundSystem.playRecordedCluk();
                tvError.setText("");
                try{
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (NullPointerException e){

                }
                final String email = etEmail.getText().toString(),
                        pass = etPass.getText().toString();
                if (email.isEmpty() || pass.isEmpty()){
                    tvError.setText(R.string.login_error_empty);
                } else {
                    LoginTask login = new LoginTask(LoginActivity.this, email, pass){
                        @Override
                        protected void onPostExecute(ReturnLogin sol) {
                            super.onPostExecute(sol);
                            if (sol.e==null){
                                finish();
                            }else{
                                tvError.setText(sol.e.getMessage());
                            }
                        }
                    };
                    login.execute();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundSystem.playRecordedCluk();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Session.isLogged()){
            finish();
        }
    }

    public static class LoginTask extends AsyncTask<Void, Void, ReturnLogin> {

        private AlertDialog dial;
        private Context ctxt;
        private String email, pass;

        public LoginTask(Context ctxt, String email, String pass){
            this.ctxt = ctxt;
            this.email = email;
            this.pass = pass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dial = new AlertDialog.Builder(ctxt)
                    .setTitle(R.string.login_loging_dial_title)
                    .setMessage(R.string.login_loging_dial_msg)
                    .setCancelable(false)
                    .show();
        }

        @Override
        protected ReturnLogin doInBackground(Void... params) {
            return Session.login(ctxt, email, pass);
        }

        @Override
        protected void onPostExecute(ReturnLogin sol) {
            super.onPostExecute(sol);
            dial.dismiss();
        }
    }
}