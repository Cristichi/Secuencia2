package org.iesmurgi.cristichi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.iesmurgi.cristichi.ddbb.Session;
import org.iesmurgi.cristichi.ddbb.LocalStorage;

public class MainActivity extends AppCompatActivity {

    private Button btnPlay;
    private Button btnAccount;
    private Button btnHighscores;
    private Button btnOptions;

    private ImageView ivNewPoint;

    private boolean firstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(MainActivity.this, TabbedMenuActivity.class);
                startActivity(intento);
                //finish();
            }
        });

        btnAccount = findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.isLogged()){
                    Intent intento = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(intento);
                }else{
                    Intent intento = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intento);
                }
            }
        });

        btnHighscores = findViewById(R.id.btnHighScores);
        btnHighscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(MainActivity.this, HighscoresActivity.class);
                startActivity(intento);
            }
        });

        btnOptions = findViewById(R.id.btnOptions);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intento);
                //finish();
            }
        });

        ivNewPoint = findViewById(R.id.ivNew);

        firstStart = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firstStart){
            firstStart = false;
            LocalStorage.tryLoginFromFile(MainActivity.this);
            if (Session.isLogged()){
                //String str = getString(R.string.account) + " " + Session.getUser().nick;
                String str = Session.getUser().nick;
                btnAccount.setText(str);
                ivNewPoint.setVisibility(View.INVISIBLE);
            }else{
                btnAccount.setText(R.string.login);
                ivNewPoint.setVisibility(View.VISIBLE);
            }
        }
    }
}
