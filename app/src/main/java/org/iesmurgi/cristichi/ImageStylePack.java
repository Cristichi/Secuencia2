package org.iesmurgi.cristichi;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ImageStylePack implements StylePack<Integer>{
    TEST(R.string.app_name, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground)
    ;
    @StringRes
    private int nombre;
    @DrawableRes
    private int icono;
    @DrawableRes
    private int[] botonera;

    private Random rng;

    ImageStylePack(int nombre, int icono, @DrawableRes int... botonera){
        this.nombre = nombre;
        this.icono = icono;
        this.botonera = botonera;
        rng = new Random();
    }

    @Override
    public Button[] getBotonera(Context contexto) {
        Button[] sol = new Button[botonera.length];
        for (int i = 0; i < sol.length; i++) {
            Button uno = new Button(contexto);
            uno.setBackground(contexto.getResources().getDrawable(botonera[i]));
            sol[i] = uno;
        }
        return sol;
    }

    @Override
    public void setNombre(@StringRes int nombre) {
        this.nombre = nombre;
    }

    @Override @StringRes
    public int getNombre() {
        return nombre;
    }

    @Override
    public void setIcono(@DrawableRes int icono) {
        this.icono = icono;
    }

    @Override
    public int getIcono() {
        return icono;
    }

    @Override
    public List<Integer> generateRandomSentence(Difficulty difficulty) {
        int elementos = difficulty.getNumElementos();
        Log.d("CRISTICHIEX", "elementos: " + elementos);
        elementos += (rng.nextBoolean() ? 1 : -1) * rng.nextInt(elementos / 4);
        Log.d("CRISTICHIEX", "elementos: " + elementos);
        ArrayList<Integer> sol = new ArrayList<>(elementos);
        for (int i = 0; i < elementos; i++) {
            int imagen = botonera[rng.nextInt(botonera.length)];
            sol.add(imagen);
            Log.d("CRISTICHIEX", "Número " + i + ": " + imagen);
        }
        return sol;
    }
}
