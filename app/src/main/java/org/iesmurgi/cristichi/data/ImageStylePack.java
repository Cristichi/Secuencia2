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

public enum ImageStylePack implements StylePack<Integer> {
    CATS(R.string.isp_cats_name, R.drawable.icon_isp_cats,
            R.drawable.isp_cats_1, R.drawable.isp_cats_2,
            R.drawable.isp_cats_3, R.drawable.isp_cats_4),

    FORMS(R.string.isp_forms_name, R.drawable.icon_isp_forms,
            R.drawable.isp_form_circle, R.drawable.isp_form_line_horizontal,
            R.drawable.isp_form_line_vertical, R.drawable.isp_form_square,
            R.drawable.isp_form_star, R.drawable.isp_form_triangle),

    COLORS(R.string.isp_colors_name, R.drawable.icon_isp_colors,
            R.drawable.isp_color_blue, R.drawable.isp_color_aqua,
            R.drawable.isp_color_green, R.drawable.isp_color_orange,
            R.drawable.isp_color_purple, R.drawable.isp_color_red,
            R.drawable.isp_color_yellow)
    ;
    @StringRes
    protected int name;
    @DrawableRes
    protected int icon;
    @DrawableRes
    protected int[] values;

    protected Random rng;

    ImageStylePack(int name, @DrawableRes int icon, @DrawableRes int... values){
        this.name = name;
        this.icon = icon;
        this.values = values;
        rng = new Random();
        if (this.values.length==0){
            throw new IllegalArgumentException("Available values can't be empty");
        }
    }

    @Override
    public ArrayList<Button> getButtons(Context context) {
        ArrayList<Button> sol = new ArrayList<>(values.length);

        for (int num : values) {
            Button uno = new Button(context);
            uno.setBackground(context.getResources().getDrawable(num));
            uno.setTag(num);
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

    @Override @DrawableRes
    public int getIcon() {
        return icon;
    }

    @Override
    public List<Integer> generateRandomSentence(Difficulty difficulty) {
        int elementos = difficulty.getNumElementos();
        elementos += (rng.nextBoolean() ? 1 : -1) * rng.nextInt(Math.max(elementos / 4, 1));
        ArrayList<Integer> sol = new ArrayList<>(elementos);
        for (int i = 0; i < elementos; i++) {
            int imagen = values[rng.nextInt(values.length)];
            sol.add(imagen);
        }
        return sol;
    }
}