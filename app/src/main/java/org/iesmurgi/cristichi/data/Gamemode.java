package org.iesmurgi.cristichi.data;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface Gamemode<E> extends Serializable {

    /**
     *
     * @return El código de este Modo de Juego en la Base de Datos, usado para identificarlo en
     * la base de datos.
     */
    String getCode();

    /**
     *
     * @return El recurso que representa el nombre de este Modo de Juego.
     */
    @StringRes
    int getName();

    /**
     *
     * @return El recurso que representa el icono de este Modo de Juego.
     */
    @DrawableRes
    int getIcon();

    /**
     *
     * @param ctxt Actividad donde se mostrarán los botones.
     * @return La lista de botones de orden aleatorizado, que guardan su valor de la secuencia
     * en su Tag. Contiene cada valor de este Modo de Juego sin repetir.
     */
    List<Button> getButtons(Context ctxt);

    /**
     *
     * @param difficulty Dificultad de la secuencia
     * @return Una lista aleatorizada cuyos valores son siempre valores de este Modo de Juego.
     */
    List<E> generateRandomSecuence(Difficulty difficulty);
}
