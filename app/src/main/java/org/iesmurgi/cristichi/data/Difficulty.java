package org.iesmurgi.cristichi.data;

import android.support.annotation.StringRes;

import org.iesmurgi.cristichi.R;

public enum  Difficulty {
    EASIEST(0, R.string.diff_easiest, 4),
    EASY(1, R.string.diff_easy, 8),
    MEDIUM(2, R.string.diff_medium, 14),
    HARD(3, R.string.diff_hard, 17),
    EXTREME(4, R.string.diff_extreme, 20);

    public static Difficulty getById(int id){
        for(Difficulty dif : values()){
            if (dif.id == id){
                return dif;
            }
        }
        throw new IllegalArgumentException("Unknown difficulty with id \""+id+"\"");
    }

    private int id;
    @StringRes
    private int name;
    private int numElementos;

    Difficulty(int id, @StringRes int name, int numElementos){
        this.id = id;
        this.name = name;
        this.numElementos = numElementos;
    }

    public int getId() {
        return id;
    }

    @StringRes
    public int getName() {
        return name;
    }

    public int getNumElementos() {
        return numElementos;
    }
}
