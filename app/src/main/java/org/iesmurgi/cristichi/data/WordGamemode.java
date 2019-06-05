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

public enum WordGamemode implements Gamemode<Integer> {
    ERASMUS("ERASMUS", R.string.wsp_erasmus_name, R.drawable.icon_wsp_erasmus,
            R.string.wsp_erasmus_greece, R.string.wsp_erasmus_italy,
            R.string.wsp_erasmus_turkey, R.string.wsp_erasmus_poland,
            R.string.wsp_erasmus_spain),
    DEADLY_SINS("SINS", R.string.wsp_deadly_sins_name, R.drawable.icon_wsp_deadly_sins,
            R.string.wsp_deadly_sins_envy, R.string.wsp_deadly_sins_gluttony,
            R.string.wsp_deadly_sins_greed, R.string.wsp_deadly_sins_lust,
            R.string.wsp_deadly_sins_pride, R.string.wsp_deadly_sins_sloth,
            R.string.wsp_deadly_sins_wrath),
    WEEK_DAYS("WEEK_DAYS", R.string.wsp_week_days_name, R.drawable.icon_wsp_week_days,
            R.string.wsp_week_days_monday, R.string.wsp_week_days_tuesday,
            R.string.wsp_week_days_wednesday, R.string.wsp_week_days_thursday,
            R.string.wsp_week_days_friday, R.string.wsp_week_days_saturday,
            R.string.wsp_week_days_sunday),
    ;

    protected String code;
    @StringRes
    protected int name;
    @DrawableRes
    protected int icon;
    @StringRes
    protected int[] values;

    protected Random rng;

    WordGamemode(String code, int name, int icon, @StringRes int... values) {
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
        for (int value : values) {
            Button uno = new Button(context);
            uno.setText(value);
            uno.setTag(value);
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

    @Override
    @StringRes
    public int getName() {
        return name;
    }

    @Override
    public int getIcon() {
        return icon;
    }


    @Override
    public List<Integer> generateRandomSecuence(Difficulty difficulty) {
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