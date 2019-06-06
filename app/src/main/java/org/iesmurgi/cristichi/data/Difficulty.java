package org.iesmurgi.cristichi.data;

import android.support.annotation.StringRes;

import org.iesmurgi.cristichi.R;

/**
 * Representa la dificultad de la secuencia generada para una partida
 */
public enum  Difficulty {
    EASIEST(0, R.string.diff_easiest, 2),
    EASY(1, R.string.diff_easy, 5),
    MEDIUM(2, R.string.diff_medium, 10),
    HARD(3, R.string.diff_hard, 17),
    EXTREME(4, R.string.diff_extreme, 25);

    /**
     *
     * @param id Id de una Dificultad
     * @return La Dificultad dada por esa Id
     * @throws IllegalArgumentException Si la Id dada no es encontrada
     */
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

    /**
     *
     * @return La id que representa esta Dificultad en la Base de Datos
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return El recurso que representa el nombre de esta Dificultad
     */
    @StringRes
    public int getName() {
        return name;
    }

    /**
     *
     * @return El número de elementos que debe tener aproximadamente una secuencia generada
     * con esta Dificultad
     */
    public int getNumElementos() {
        return numElementos;
    }
}
