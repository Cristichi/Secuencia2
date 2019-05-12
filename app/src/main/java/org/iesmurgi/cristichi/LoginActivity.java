package org.iesmurgi.cristichi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.iesmurgi.cristichi.ddbb.DDBBConstraints;
import org.iesmurgi.cristichi.ddbb.Session;
import org.iesmurgi.cristichi.ddbb.User;
import org.iesmurgi.cristichi.storage.StorageHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.security.auth.login.LoginException;

public class LoginActivity extends AppCompatActivity {

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
        etPass = findViewById(R.id.etPassword);

        tvError = findViewById(R.id.tvError);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        int color = ResourcesCompat.getColor(getResources(), R.color.primaryTextColor, getTheme());
        int colorHint = ResourcesCompat.getColor(getResources(), R.color.primaryHintTextColor, getTheme());
        etEmail.setTextColor(color);
        etEmail.setHintTextColor(colorHint);
        etPass.setTextColor(color);
        etPass.setHintTextColor(colorHint);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvError.setText("");
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                final String email = etEmail.getText().toString(),
                        pass = etPass.getText().toString();
                if (email.isEmpty() || pass.isEmpty()){
                    tvError.setText(R.string.login_error_empty);
                } else {
                    LoginTask login = new LoginTask(LoginActivity.this, email, pass){
                        @Override
                        protected void onPostExecute(ReturnLogin sol) {
                            super.onPostExecute(sol);
                            if (sol.e!=null){
                                tvError.setText(sol.e.getMessage());
                            }else{
                                Session.login(LoginActivity.this, sol.user, pass);
                                finish();
                            }
                        }
                    };
                    login.execute();

                    /* *
                    Throwable e = Session.login(LoginActivity.this, email, pass);
                    if (e==null){
                        Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        if (e instanceof LoginException){
                            tvError.setText(R.string.error_login);
                        }else{
                            tvError.setText(R.string.error_net);
                        }
                    }
                    /* */
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}

class ReturnLogin{
    Throwable e = null;
    User user = null;
}

class LoginTask extends AsyncTask<Void, Void, ReturnLogin> {

    private AlertDialog dial;
    private Context ctxt;
    private String email, pass;

    LoginTask(Context ctxt, String email, String pass){
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
        ReturnLogin sol = new ReturnLogin();

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.setLoginTimeout(2);
            con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT Nickname, Email from Users where Email='" + email + "' and Pass='" + pass + "'");

            if (rs.first()) {
                sol.user = new User(rs.getString(1), rs.getString(2));
            }else{
                sol.e = new LoginException();
            }
        }catch (Exception e) {
            e.printStackTrace();
            sol.e = new ServerException(ctxt.getString(R.string.error_net));
        }
        if (con != null){
            try{
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        return sol;
    }

    @Override
    protected void onPostExecute(ReturnLogin sol) {
        super.onPostExecute(sol);
        dial.dismiss();
    }
}

class ServerException extends RuntimeException{
    public ServerException(String msg) {
        super(msg);
    }
}