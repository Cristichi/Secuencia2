package org.iesmurgi.cristichi;

import android.support.annotation.StringRes;

public enum  Difficulty {
    /*
    EASIEST(R.string.diff_easiest, 6), EASY(R.string.diff_easy, 10),
    MEDIUM(R.string.diff_medium, 15),
    HARD(R.string.diff_hard, 20), EXTREME(R.string.diff_extreme, 30);
    */
    EASIEST(R.string.diff_easiest, 2),
    EASY(R.string.diff_easy, 5),
    MEDIUM(R.string.diff_medium, 10),
    HARD(R.string.diff_hard, 15),
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
