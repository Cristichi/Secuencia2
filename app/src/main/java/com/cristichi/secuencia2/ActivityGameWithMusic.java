package com.cristichi.secuencia2;

import android.support.v7.app.AppCompatActivity;

/**
 * Toda actividad que herede de ActivityGameWithMusic tendrá música de fondo y un sonido al pulsar
 * la tecla Back destinados para reproducirse durante las partidas
 */
public class ActivityGameWithMusic extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        SoundSystem.playMusicBackgroundGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundSystem.pauseMusicBackgroundGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundSystem.resetMusicBackgroundGame();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SoundSystem.playCartoonFail();
    }
}
