package org.iesmurgi.cristichi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.iesmurgi.cristichi.ddbb.Session;
import org.iesmurgi.cristichi.storage.StorageHelper;

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setEnabled(false);
                String email = etEmail.getText().toString(),
                        pass = etPass.getText().toString();
                if (email.isEmpty() || pass.isEmpty()){
                    tvError.setText(R.string.login_error_empty);
                }else {
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
                }
                btnLogin.setEnabled(true);
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
