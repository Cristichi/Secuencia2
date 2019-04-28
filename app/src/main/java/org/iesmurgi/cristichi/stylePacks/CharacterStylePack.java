package org.iesmurgi.cristichi.stylePacks;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import org.iesmurgi.cristichi.Difficulty;
import org.iesmurgi.cristichi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum CharacterStylePack implements StylePack<Character> {
    ALPHABET(R.string.csp_alphabet_name, R.drawable.icon_csp_alphabet,
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'N', 'M', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'),
    ALPHABET_1(R.string.csp_alphabet1_name, R.drawable.icon_csp_alphabet1,
            'A', 'B', 'C',
            'D', 'E', 'F',
            'G', 'H', 'I'),
    ALPHABET_2(R.string.csp_alphabet2_name, R.drawable.icon_csp_alphabet2,
            'J', 'K', 'L',
            'N', 'M', 'O',
            'P', 'Q', 'R'),
    ALPHABET_3(R.string.csp_alphabet3_name, R.drawable.icon_csp_alphabet3,
            'S', 'T', 'U',
            'V', 'W', 'X',
            'Y', 'Z'),
    GREEK(R.string.csp_greek_name, R.drawable.icon_csp_greek,
            '\u03b1', '\u03b2', '\u03b3',
            '\u03b4', '\u03b5', '\u03b6', '\u03b7'),
    FORMULAS(R.string.ssp_formulas_name, R.drawable.icon_ssp_formulas,
            '0','1','2','3','4',
            '5','6','7','8','9',
            '+', '-', '*', '/'){

        char[] numbers = new char[]{'0','1','2','3','4','5','6','7','8','9'};
        char[] operations = new char[]{'+', '-', '*', '/'};

        @Override
        public List<Character> generateRandomSentence(Difficulty difficulty) {
            int elementos = difficulty.getNumElementos();
            elementos+= (rng.nextBoolean()?1:-1) * rng.nextInt(Math.max(elementos / 4, 1));
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
    ;

    @StringRes
    protected int name;
    @DrawableRes
    protected int icon;
    protected char[] values;

    protected Random rng;

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
    public ArrayList<Button> getButtons(Context context) {
        ArrayList<Button> sol = new ArrayList<>(values.length);

        for (int i = 0; i < values.length; i++) {
            Button uno = new Button(context);
            uno.setText(Character.toString(values[i]));
            uno.setTag(values[i]);
            sol.add(uno);
        }
        Collections.shuffle(sol);
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

    public char[] getValues() {
        return values;
    }

    @Override
    public List<Character> generateRandomSentence(Difficulty difficulty) {
        int elementos = difficulty.getNumElementos();
        elementos+= (rng.nextBoolean()?1:-1) * rng.nextInt(Math.max(elementos / 4, 1));
        ArrayList<Character> sol = new ArrayList<>(elementos);
        for (int i = 0; i < elementos; i++) {
            char car = values[rng.nextInt(values.length)];
            sol.add(car);
        }
        return sol;
    }
}
