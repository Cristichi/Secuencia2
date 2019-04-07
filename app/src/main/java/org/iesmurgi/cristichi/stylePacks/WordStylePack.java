package org.iesmurgi.cristichi.stylePacks;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import org.iesmurgi.cristichi.Difficulty;
import org.iesmurgi.cristichi.R;
import org.iesmurgi.cristichi.stylePacks.StylePack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum WordStylePack implements StylePack<Integer> {
    ERASMUS(R.string.wsp_erasmus_name, R.drawable.icon_wsp_week_days,
            R.string.wsp_erasmus_greece, R.string.wsp_erasmus_italy,
            R.string.wsp_erasmus_turkey, R.string.wsp_erasmus_poland,
            R.string.wsp_erasmus_spain),
    WEEK_DAYS(R.string.wsp_week_days_name, R.drawable.icon_wsp_week_days,
            R.string.wsp_week_days_monday, R.string.wsp_week_days_tuesday,
            R.string.wsp_week_days_wednesday, R.string.wsp_week_days_thursday,
            R.string.wsp_week_days_friday, R.string.wsp_week_days_saturday,
            R.string.wsp_week_days_sunday),
    DEADLY_SINS(R.string.wsp_deadly_sins_name, R.drawable.icon_wsp_deadly_sins,
            R.string.wsp_deadly_sins_envy, R.string.wsp_deadly_sins_gluttony,
            R.string.wsp_deadly_sins_greed, R.string.wsp_deadly_sins_lust,
            R.string.wsp_deadly_sins_pride, R.string.wsp_deadly_sins_sloth,
            R.string.wsp_deadly_sins_wrath),
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
    public ArrayList<Button> getButtons(Context context) {
        ArrayList<Button> sol = new ArrayList<>(values.length);
        for (int i = 0; i < values.length; i++) {
            Button uno = new Button(context);
            uno.setText(values[i]);
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