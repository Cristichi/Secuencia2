package com.cristichi.secuencia2;

import android.support.v7.app.AppCompatActivity;

/**
 * Toda actividad que herede de ActivityWithMusic tendrá música de fondo y un sonido al pulsar
 * la tecla Back destinados para reproducirse durante las actividades que no son partidas
 */
public class ActivityWithMusic extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        SoundSystem.playMusicBackground();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundSystem.pauseMusicBackground();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SoundSystem.playCartoonSlipFall();
    }
}
