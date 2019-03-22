package org.iesmurgi.cristichi;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum WordStylePack implements StylePack<Integer> {
    PECADOS_CAPITALES(R.string.app_name, R.drawable.ic_launcher_foreground, R.string.app_name, R.string.placeholder)
            ;

    @StringRes
    private int name;
    @DrawableRes
    private int icon;
    @StringRes
    private int[] values;

    private Random rng;

    WordStylePack(int name, int icon, @StringRes int... values) {
        this.name = name;
        this.icon = icon;
        this.values = values;
        rng = new Random();
        if (values.length==0){
            throw new IllegalArgumentException("Available values can't be empty");
        }
    }

    @Override
    public Button[] getButtons(Context context) {
        Button[] sol = new Button[values.length];
        for (int i = 0; i < sol.length; i++) {
            Button uno = new Button(context);
            uno.setText(values[i]);
            sol[i] = uno;
        }
        return sol;
    }

    @Override
    public void setName(@StringRes int name) {
        this.name = name;
    }

    @Override
    @StringRes
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
        elementos += (rng.nextBoolean() ? 1 : -1) * rng.nextInt(Math.max(elementos / 4, 1));
        ArrayList<Integer> sol = new ArrayList<>(elementos);
        for (int i = 0; i < elementos; i++) {
            int word = values[rng.nextInt(values.length)];
            sol.add(word);
        }
        return sol;
    }
}