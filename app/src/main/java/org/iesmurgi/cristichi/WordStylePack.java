package org.iesmurgi.cristichi;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum WordStylePack implements StylePack<String> {
    PECADOS_CAPITALES(R.string.app_name, R.drawable.ic_launcher_foreground, "Avaricia", "Envidia", "Lujuria", "Ira", "Soberbia", "Pereza","Gula")
            ;

    @StringRes
    private int nombre;
    @DrawableRes
    private int icono;
    private String[] botonera;

    private Random rng;

    WordStylePack(int nombre, int icono, String... botonera) {
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
            uno.setText(botonera[i]);
            sol[i] = uno;
        }
        return sol;
    }

    @Override
    public void setNombre(@StringRes int nombre) {
        this.nombre = nombre;
    }

    @Override
    @StringRes
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
    public List<String> generateRandomSentence(Difficulty difficulty) {
        int elementos = difficulty.getNumElementos();
        Log.d("CRISTICHIEX", "elementos: " + elementos);
        elementos += (rng.nextBoolean() ? 1 : -1) * rng.nextInt(Math.max(elementos / 4, 1));
        Log.d("CRISTICHIEX", "elementos: " + elementos);
        ArrayList<String> sol = new ArrayList<>(elementos);
        for (int i = 0; i < elementos; i++) {
            String word = botonera[rng.nextInt(botonera.length)];
            sol.add(word);
            Log.d("CRISTICHIEX", "Número " + i + ": " + word);
        }
        return sol;
    }
}