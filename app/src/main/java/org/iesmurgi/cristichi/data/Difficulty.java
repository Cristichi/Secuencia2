package org.iesmurgi.cristichi.data;

import android.support.annotation.StringRes;

import org.iesmurgi.cristichi.R;

public enum  Difficulty {
    EASIEST(R.string.diff_easiest, 4),
    EASY(R.string.diff_easy, 8),
    MEDIUM(R.string.diff_medium, 13),
    HARD(R.string.diff_hard, 17),
    EXTREME(R.string.diff_extreme, 20);

    @StringRes
    private int name;
    private int numElementos;

    Difficulty(@StringRes int name, int numElementos){
        this.name = name;
        this.numElementos = numElementos;
    }

    @StringRes
    public int getName() {
        return name;
    }

    public int getNumElementos() {
        return numElementos;
    }
}
