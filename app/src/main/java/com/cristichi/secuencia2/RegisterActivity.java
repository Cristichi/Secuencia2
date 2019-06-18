package com.cristichi.secuencia2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cristichi.secuencia2.ddbb.DDBBConstraints;
import com.cristichi.secuencia2.ddbb.ReturnLogin;
import com.cristichi.secuencia2.ddbb.Session;
import com.cristichi.secuencia2.ddbb.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterActivity extends ActivityWithMusic {

    private TextView tvError;

    private EditText etNick;
    private EditText etEmail;
    private EditText etPass1;
    private EditText etPass2;

    private Button btnRegister;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvError = findViewById(R.id.tvError);

        etNick = findViewById(R.id.etNick);
        etEmail = findViewById(R.id.etEmail);
        etPass1 = findViewById(R.id.etOldPass1);
        etPass2 = findViewById(R.id.etOldPass2);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvError.setText("");
                String nick = etNick.getText().toString().trim(),
                email = etEmail.getText().toString().trim(),
                pass1 = etPass1.getText().toString(),
                pass2 = etPass2.getText().toString();

                if (nick.isEmpty()){
                    tvError.setText(R.string.register_error_nick_empty);
                }else if (email.isEmpty()){
                    tvError.setText(R.string.register_error_email_empty);
                }else if (pass1.isEmpty()){
                    tvError.setText(R.string.register_error_pass_empty);
                }else if (!pass1.equals(pass2)){
                    tvError.setText(R.string.register_error_pass_equal);
                }else{
                    RegisterMYSQL task = new RegisterMYSQL(email, nick, pass1);
                    task.execute();
                    try{
                        User res = task.get();
                        if (res==null){
                            tvError.setText(task.getError());
                        }else{
                            LoginActivity.LoginTask login = new LoginActivity.LoginTask(RegisterActivity.this, res.email, pass1){
                                @Override
                                protected void onPostExecute(ReturnLogin sol) {
                                    super.onPostExecute(sol);
                                    if (sol.e==null){
                                        Toast.makeText(RegisterActivity.this, R.string.register_registered, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, sol.e.getMessage(), Toast.LENGTH_SHORT).show();
                                        tvError.setText(R.string.register_error_generic);
                                    }
                                }
                            };
                            login.execute();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        tvError.setText(R.string.register_error_generic);
                    }
                }
            }
        });

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private static class RegisterMYSQL extends AsyncTask<Void, Void, User> {
        @StringRes
        private int error;

        private String email;
        private String nick;
        private String pass;

        RegisterMYSQL(String email, String nick, String pass){
            this.email = email;
            this.nick = nick;
            this.pass = pass;
        }

        @StringRes
        int getError() {
            return error;
        }

        @Override
        protected User doInBackground(Void... params) {
            User sol = null;
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DDBBConstraints.URL_DDBB,
                        DDBBConstraints.USER, DDBBConstraints.PASSWORD);

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(
                        "select Email from Users where Email='"+email+"'");

                if (rs.next()) {
                    error = R.string.register_error_email_exists;
                }else{
                    Statement st2 = con.createStatement();
                    ResultSet rs2 = st2.executeQuery(
                            "select Email from Users where Nickname='"+nick+"'");
                    if (rs2.next()) {
                        error = R.string.register_error_nick_exists;
                    }else{
                        Statement st3 = con.createStatement();
                        st3.execute("insert into Users(Email, Nickname, Pass) values('" + email + "', '" +
                                nick + "', '" + Session.encrypt(pass) + "')");
                        sol = new User(nick, email);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
    }
}
