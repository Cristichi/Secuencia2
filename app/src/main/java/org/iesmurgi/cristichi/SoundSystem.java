package org.iesmurgi.cristichi;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Clase que engloba métodos que controlan el flujo de sonido, ya sea música o efectos de sonido.
 */
public class SoundSystem {
    private static MediaPlayer background;
    private static MediaPlayer gameBackground;
    private static SoundPool soundPool;

    private static int soundIdCartoonPunch;
    private static int soundIdCartoonSlipFall;
    private static int soundIdCartoonSlipUp;
    private static int soundIdCartoonDrumRoll;
    private static int soundIdCartoonFail;
    private static int soundIdCartoonHonkHorn;
    private static int soundIdRecordedCluk;
    private static int soundIdRecordedPop;

    private static boolean destroyed = true;

    /**
     * Debe ejecutarse este método antes de poder ejecutar cualquiera de los otros métodos.
     * @param context Actividad principal.
     */
    public static void Init(Context context){
        if (!destroyed){
            return;
        }
        soundPool = new SoundPool.Builder().setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()).build();

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

    /**
     * Reproduce la música de fondo.
     */
    public static void playMusicBackground(){
        if (!destroyed) {
            background.start();
        }
    }

    /**
     * Pausa la música de fondo.
     */
    public static void pauseMusicBackground(){
        if (!destroyed) {
            background.pause();
        }
    }

    /**
     * Reproduce la música de fondo de las partidas.
     */
    public static void playMusicBackgroundGame(){
        if (!destroyed) {
            gameBackground.start();
        }
    }

    /**
     * Pausa la música de fondo de las partidas.
     */
    public static void pauseMusicBackgroundGame(){
        if (!destroyed) {
            gameBackground.pause();
        }
    }

    /**
     * Reinicia la música de fondo de las partidas.
     */
    public static void resetMusicBackgroundGame(){
        if (!destroyed) {
            gameBackground.pause();
            gameBackground.seekTo(0);
        }
    }

    /**
     * Reproduce el sonido correspondiente.
     */
    public static void playCartoonPunch(){
        if (!destroyed){
            soundPool.play(soundIdCartoonPunch, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    /**
     * Reproduce el sonido correspondiente.
     */
    public static void playCartoonSlipFall(){
        if (!destroyed){
            soundPool.play(soundIdCartoonSlipFall, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    /**
     * Reproduce el sonido correspondiente.
     */
    public static void playCartoonSlipUp(){
        if (!destroyed){
            soundPool.play(soundIdCartoonSlipUp, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    /**
     * Reproduce el sonido correspondiente.
     */
    public static void playCartoonDrumRoll(){
        if (!destroyed){
            soundPool.play(soundIdCartoonDrumRoll, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    /**
     * Reproduce el sonido correspondiente.
     */
    public static void playCartoonFail(){
        if (!destroyed){
            soundPool.play(soundIdCartoonFail, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    /**
     * Reproduce el sonido correspondiente.
     */
    public static void playCartoonHonkHorn(){
        if (!destroyed){
            soundPool.play(soundIdCartoonHonkHorn, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    /**
     * Reproduce el sonido correspondiente.
     */
    public static void playRecordedCluk(){
        if (!destroyed){
            soundPool.play(soundIdRecordedCluk, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    /**
     * Reproduce el sonido correspondiente.
     */
    public static void playRecordedPop(){
        if (!destroyed){
            soundPool.play(soundIdRecordedPop, 0.2f, 0.2f, 0, 0, 1);
        }
    }

    /**
     * Destruye el sistema de sonido, debe ejecutarse al destruirse la última actividad de la
     * aplicación. Una vez eejcutado este método, debe ejecutarse de nuevo el método Init
     * para poder usar de nuevo el resto de métodos.
     */
    public static void destroy(){
        if (destroyed){
            return;
        }
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
