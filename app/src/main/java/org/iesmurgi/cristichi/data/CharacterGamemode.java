package org.iesmurgi.cristichi.data;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import org.iesmurgi.cristichi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum CharacterGamemode implements Gamemode<Character> {
    ALPHABET("ABC_AZ", R.string.csp_alphabet_name, R.drawable.icon_csp_alphabet,
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'N', 'M', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'),
    ALPHABET_1("ABC_AI", R.string.csp_alphabet1_name, R.drawable.icon_csp_alphabet1,
            'A', 'B', 'C',
            'D', 'E', 'F',
            'G', 'H', 'I'),
    ALPHABET_2("ABC_JR", R.string.csp_alphabet2_name, R.drawable.icon_csp_alphabet2,
            'J', 'K', 'L',
            'N', 'M', 'O',
            'P', 'Q', 'R'),
    ALPHABET_3("ABC_SZ", R.string.csp_alphabet3_name, R.drawable.icon_csp_alphabet3,
            'S', 'T', 'U',
            'V', 'W', 'X',
            'Y', 'Z'),
    GREEK("GREEK", R.string.csp_greek_name, R.drawable.icon_csp_greek,
            '\u03b1', '\u03b2', '\u03b3',
            '\u03b4', '\u03b5', '\u03b6', '\u03b7'),
    FORMULAS("FORMULAS", R.string.ssp_formulas_name, R.drawable.icon_ssp_formulas,
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
            final int maxDigits = 4;
            int contDigits = 0;
            boolean operation = false;
            boolean lastWasOp = true;
            for (int i = 0; i < elementos; i++) {
                if (i==elementos-1){
                    operation= false;
                }
                char car;
                if (operation){
                    car = operations[rng.nextInt(operations.length)];
                    lastWasOp = true;
                    operation = false;
                }else{
                    if (lastWasOp){
                        car = numbers[rng.nextInt(numbers.length-1)+1];
                    }else{
                        car = numbers[rng.nextInt(numbers.length)];
                    }
                    operation = rng.nextBoolean();
                    lastWasOp = false;
                    contDigits++;
                    if (contDigits>=maxDigits){
                        contDigits = 0;
                        operation = true;
                    }
                }
                sol.add(car);
            }
            return sol;
        }
    }
    ;

    protected String code;
    @StringRes
    protected int name;
    @DrawableRes
    protected int icon;
    protected char[] values;

    protected Random rng;

    CharacterGamemode(String code, int name, int icon, char... values){
        this.code = code;
        if (code.length()>10){
            throw new IllegalArgumentException("Gamemode's code cannot be more than 10");
        }
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

        for (char car : values) {
            Button uno = new Button(context);
            uno.setText(Character.toString(car));
            uno.setTag(car);
            sol.add(uno);
        }
        Collections.shuffle(sol);
        return sol;
    }

    protected void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setName(@StringRes int name) {
        this.name = name;
    }

    @Override @StringRes
    public int getName() {
        return name;
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
