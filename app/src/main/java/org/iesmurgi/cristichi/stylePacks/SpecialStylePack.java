package org.iesmurgi.cristichi.stylePacks;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import org.iesmurgi.cristichi.Difficulty;
import org.iesmurgi.cristichi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum SpecialStylePack implements StylePack<Character> {
    FORMULAS(R.string.ssp_formulas_name, R.drawable.icon_ssp_formulas);

    private static char[] operations;
    private static char[] numbers;
    static {
        operations = new char[]{'+', '-', '*', '/'};
        numbers = new char[]{'0','1','2','3','4','5','6','7','8','9'};
    }

    @StringRes
    private int name;
    @DrawableRes
    private int icon;

    private Random rng;

    SpecialStylePack(int name, int icon){
        this.name = name;
        this.icon = icon;
        rng = new Random();
    }

    @Override
    public Button[] getButtons(Context context) {
        Button[] sol = new Button[operations.length+ numbers.length];
        int i;
        for (i = 0; i < operations.length; i++) {
            Button uno = new Button(context);
            uno.setText(Character.toString(operations[i]));
            sol[i] = uno;
        }
        for (int j = 0; j < numbers.length; j++) {
            Button uno = new Button(context);
            uno.setText(Character.toString(numbers[j]));
            sol[i+j] = uno;
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
        boolean operacion = false;
        for (int i = 0; i < elementos; i++) {
            if (i==elementos-1){
                operacion= false;
            }
            char car;
            if (operacion){
                car = operations[rng.nextInt(operations.length)];
                operacion = false;
            }else{
                car = numbers[rng.nextInt(numbers.length)];
                operacion = rng.nextBoolean();
            }
            sol.add(car);
        }
        return sol;
    }
}
