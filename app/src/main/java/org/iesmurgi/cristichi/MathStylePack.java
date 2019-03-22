package org.iesmurgi.cristichi;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import java.util.List;

public enum MathStylePack implements StylePack<Character>{
    TEST(R.string.app_name, R.drawable.ic_launcher_foreground)
            ;
    @StringRes
    private int nombre;
    @DrawableRes
    private int icono;
    @DrawableRes
    private int[] botonera;

    MathStylePack(int nombre, int icono, @DrawableRes int... botonera){
        this.nombre = nombre;
        this.icono = icono;
        this.botonera = botonera;
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
    public List<Character> generateRandomSentence(Difficulty difficulty) {
        return null;
    }
}
