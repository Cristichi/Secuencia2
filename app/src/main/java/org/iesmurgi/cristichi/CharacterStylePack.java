package org.iesmurgi.cristichi;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public enum CharacterStylePack implements StylePack<Character> {
    LETRAS_SIMPLES(R.string.app_name, R.drawable.ic_launcher_foreground, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I')
    ;
    @StringRes
    private int nombre;
    @DrawableRes
    private int icono;
    private char[] botonera;

    CharacterStylePack(int nombre, int icono, char... botonera){
        this.nombre = nombre;
        this.icono = icono;
        this.botonera = botonera;
    }

    @Override
    public Button[] getBotonera(Context contexto) {
        Button[] sol = new Button[botonera.length];
        for (int i = 0; i < sol.length; i++) {
            Button uno = new Button(contexto);
            uno.setText(Character.toString(botonera[i]));
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
        int elementos = difficulty.getNumElementos();

        ArrayList<Character> sol = new ArrayList<>();
        return null;
    }
}
