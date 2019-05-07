package org.iesmurgi.cristichi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.iesmurgi.cristichi.ddbb.Session;
import org.iesmurgi.cristichi.ddbb.User;
import org.iesmurgi.cristichi.storage.StorageHelper;

public class AccountActivity extends AppCompatActivity {

    private TextView tvNick;
    private TextView tvEmail;

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (!Session.isLogged()){
            finish();
            return;
        }
        User user = Session.getUser();

        tvNick = findViewById(R.id.tvNickname);
        tvEmail = findViewById(R.id.tvEmail);

        btnLogout = findViewById(R.id.btnLogout);

        tvNick.setText(user.nick);
        tvEmail.setText(user.email);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StorageHelper.logout(AccountActivity.this))
                    finish();
                else
                    Toast.makeText(AccountActivity.this, "Error", Toast.LENGTH_LONG);
            }
        });
    }
}
