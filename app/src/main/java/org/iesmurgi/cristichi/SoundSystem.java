package org.iesmurgi.cristichi;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundSystem {
    private static Activity context;

    private static MediaPlayer background;
    private static MediaPlayer gameBackground;
    private static SoundPool soundPool = new SoundPool.Builder().setAudioAttributes(new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()).build();

    private static int soundIdCartoonPunch;
    private static int soundIdCartoonSlipFall;
    private static int soundIdCartoonSlipUp;
    private static int soundIdCartoonDrumRoll;
    private static int soundIdCartoonFail;
    private static int soundIdCartoonHonkHorn;
    private static int soundIdRecordedCluk;
    private static int soundIdRecordedPop;

    private static boolean destroyed = true;

    public static void Init(MainActivity context){
        if (!destroyed){
            return;
        }
        background = MediaPlayer.create(context, R.raw.background);
        background.setLooping(true);
        background.setVolume(.08f, .08f);

        gameBackground = MediaPlayer.create(context, R.raw.background_game);
        gameBackground.setLooping(true);
        gameBackground.setVolume(.1f, .1f);

        soundIdCartoonPunch  = soundPool.load(context, R.raw.cartoon_punch_1, 1);
        soundIdCartoonSlipFall  = soundPool.load(context, R.raw.cartoon_slip_fall_impact, 1);
        soundIdCartoonSlipUp  = soundPool.load(context, R.raw.cartoon_slip_up_short, 1);
        soundIdCartoonDrumRoll  = soundPool.load(context, R.raw.cartoon_drum_roll_ta_da, 1);
        soundIdCartoonFail  = soundPool.load(context, R.raw.cartoon_fail_negative, 1);
        soundIdCartoonHonkHorn  = soundPool.load(context, R.raw.cartoon_honk_horn, 1);
        soundIdRecordedCluk  = soundPool.load(context, R.raw.recorded_cluck, 1);
        soundIdRecordedPop  = soundPool.load(context, R.raw.recorded_pop, 1);

        destroyed = false;
    }

    public static void playMusicBackground(){
        if (!destroyed) {
            background.start();
        }
    }

    public static void pauseMusicBackground(){
        if (!destroyed) {
            background.pause();
        }
    }

    public static void playMusicBackgroundGame(){
        if (!destroyed) {
            gameBackground.start();
        }
    }

    public static void pauseMusicBackgroundGame(){
        if (!destroyed) {
            gameBackground.pause();
        }
    }

    public static void stopMusicBackgroundGame(){
        if (!destroyed) {
            gameBackground.pause();
            gameBackground.seekTo(0);
        }
    }

    public static void playCartoonPunch(){
        if (!destroyed){
            soundPool.play(soundIdCartoonPunch, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    public static void playCartoonSlipFall(){
        if (!destroyed){
            soundPool.play(soundIdCartoonSlipFall, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    public static void playCartoonSlipUp(){
        if (!destroyed){
            soundPool.play(soundIdCartoonSlipUp, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    public static void playCartoonDrumRoll(){
        if (!destroyed){
            soundPool.play(soundIdCartoonDrumRoll, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    public static void playCartoonFail(){
        if (!destroyed){
            soundPool.play(soundIdCartoonFail, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    public static void playCartoonHonkHorn(){
        if (!destroyed){
            soundPool.play(soundIdCartoonHonkHorn, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    public static void playRecordedCluk(){
        if (!destroyed){
            soundPool.play(soundIdRecordedCluk, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    public static void playRecordedPop(){
        if (!destroyed){
            soundPool.play(soundIdRecordedPop, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    public static void destroy(){
        destroyed = true;

        background.stop();
        background.release();
        background = null;

        gameBackground.stop();
        gameBackground.release();
        gameBackground = null;

        soundPool.release();
        soundPool = null;
    }
}