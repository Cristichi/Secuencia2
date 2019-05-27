package org.iesmurgi.cristichi;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.iesmurgi.cristichi.ddbb.Session;
import org.iesmurgi.cristichi.ddbb.LocalStorage;

public class MainActivity extends ActivityWithMusic {

    private Button btnPlay;
    private Button btnAccount;
    private Button btnHighscores;
    private Button btnCustom;

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
                SoundSystem.playCartoonSlipUp();
                Intent intento = new Intent(MainActivity.this, TabbedMenuActivity.class);
                startActivity(intento);
                //finish();
            }
        });

        btnAccount = findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundSystem.playCartoonSlipFall();
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
                SoundSystem.playCartoonDrumRoll();
                Intent intento = new Intent(MainActivity.this, HighscoresActivity.class);
                startActivity(intento);
            }
        });

        btnCustom = findViewById(R.id.btnCustom);
        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundSystem.playRecordedCluk();
                Intent intento = new Intent(MainActivity.this, CustomMenuActivity.class);
                startActivity(intento);
                //finish();
            }
        });

        ivNewPoint = findViewById(R.id.ivNew);

        firstStart = true;
        LocalStorage.tryLoginFromFile(MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firstStart){
            firstStart = false;
            SoundSystem.Init(this);
            SoundSystem.playMusicBackground();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundSystem.destroy();
    }
}
