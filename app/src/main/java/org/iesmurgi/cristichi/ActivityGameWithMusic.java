package org.iesmurgi.cristichi;

import android.support.v7.app.AppCompatActivity;

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
        SoundSystem.stopMusicBackgroundGame();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SoundSystem.playCartoonFail();
    }
}
