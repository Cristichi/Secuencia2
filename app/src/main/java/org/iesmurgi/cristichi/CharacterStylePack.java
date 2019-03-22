package org.iesmurgi.cristichi;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum CharacterStylePack implements StylePack<Character> {
    ALPHABET_1(R.string.app_name, R.drawable.ic_launcher_foreground,
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'),
    ALPHABET_2(R.string.app_name, R.drawable.ic_launcher_foreground,
            'J', 'K', 'L', 'N', 'M', 'O', 'P', 'Q', 'R'),
    ALPHABET_3(R.string.app_name, R.drawable.ic_launcher_foreground,
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'),
    GREEK(R.string.app_name, R.drawable.ic_launcher_foreground,
            '\u03b1', '\u03b2', '\u03b3', '\u03b4', '\u03b5', '\u03b6', '\u03b7'),
    ;

    @StringRes
    private int name;
    @DrawableRes
    private int icon;
    private char[] values;

    private Random rng;

    CharacterStylePack(int name, int icon, char... values){
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
            uno.setText(Character.toString(values[i]));
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
    public List<Character> generateRandomSentence(Difficulty difficulty) {
        int elementos = difficulty.getNumElementos();
        elementos+= (rng.nextBoolean()?1:-1) * rng.nextInt(elementos/4);
        ArrayList<Character> sol = new ArrayList<>(elementos);
        for (int i = 0; i < elementos; i++) {
            char car = values[rng.nextInt(values.length)];
            sol.add(car);
        }
        return sol;
    }
}
