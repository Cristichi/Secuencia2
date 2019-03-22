package org.iesmurgi.cristichi;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum MathStylePack implements StylePack<Character>{
    FORMULAS(R.string.app_name, R.drawable.ic_launcher_foreground);

    private static char[] operaciones;
    private static char[] numeros;
    static {
        operaciones = new char[]{'+', '-', '*', '/'};
        numeros = new char[]{'0','1','2','3','4','5','6','7','8','9'};
    }

    @StringRes
    private int nombre;
    @DrawableRes
    private int icono;

    private Random rng;

    MathStylePack(int nombre, int icono){
        this.nombre = nombre;
        this.icono = icono;
        rng = new Random();
    }

    @Override
    public Button[] getBotonera(Context contexto) {
        Button[] sol = new Button[operaciones.length+numeros.length];
        int i;
        for (i = 0; i < operaciones.length; i++) {
            Button uno = new Button(contexto);
            uno.setText(Character.toString(operaciones[i]));
            sol[i] = uno;
        }
        for (int j = 0; j < numeros.length; j++) {
            Button uno = new Button(contexto);
            uno.setText(Character.toString(numeros[j]));
            sol[i+j] = uno;
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
        Log.d("CRISTICHIEX", "elementos: "+elementos);
        elementos+= (rng.nextBoolean()?1:-1) * rng.nextInt(elementos/4);
        Log.d("CRISTICHIEX", "elementos: "+elementos);
        ArrayList<Character> sol = new ArrayList<>(elementos);
        boolean operacion = false;
        for (int i = 0; i < elementos; i++) {
            if (i==elementos-1){
                operacion= false;
            }
            char car;
            if (operacion){
                car = operaciones[rng.nextInt(operaciones.length)];
                operacion = false;
            }else{
                car = numeros[rng.nextInt(numeros.length)];
                operacion = rng.nextBoolean();
            }
            sol.add(car);
            Log.d("CRISTICHIEX", "Número "+i+": "+car);
        }
        return sol;
    }
}
