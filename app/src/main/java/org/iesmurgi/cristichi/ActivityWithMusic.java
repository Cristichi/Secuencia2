package org.iesmurgi.cristichi;

import android.support.v7.app.AppCompatActivity;

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
}
