package org.iesmurgi.cristichi;

import android.content.Context;
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
    private int name;
    @DrawableRes
    private int icon;
    @DrawableRes
    private int[] values;

    private Random rng;

    ImageStylePack(int name, int icon, @DrawableRes int... values){
        this.name = name;
        this.icon = icon;
        this.values = values;
        rng = new Random();
        if (this.values.length==0){
            throw new IllegalArgumentException("Available values can't be empty");
        }
    }

    @Override
    public Button[] getButtons(Context contexto) {
        Button[] sol = new Button[values.length];
        for (int i = 0; i < sol.length; i++) {
            Button uno = new Button(contexto);
            uno.setBackground(contexto.getResources().getDrawable(values[i]));
            sol[i] = uno;
        }
        return sol;
    }

    @Override
    public void setName(@StringRes int name) {
        this.name = name;
    }

    @Override @StringRes
    public int getName() {
        return name;
    }

    @Override
    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    @Override
    public int getIcon() {
        return icon;
    }

    @Override
    public List<Integer> generateRandomSentence(Difficulty difficulty) {
        int elementos = difficulty.getNumElementos();
        Log.d("CRISTICHIEX", "elementos: " + elementos);
        elementos += (rng.nextBoolean() ? 1 : -1) * rng.nextInt(elementos / 4);
        Log.d("CRISTICHIEX", "elementos: " + elementos);
        ArrayList<Integer> sol = new ArrayList<>(elementos);
        for (int i = 0; i < elementos; i++) {
            int imagen = values[rng.nextInt(values.length)];
            sol.add(imagen);
            Log.d("CRISTICHIEX", "Número " + i + ": " + imagen);
        }
        return sol;
    }
}
