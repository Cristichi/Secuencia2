package org.iesmurgi.cristichi;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import java.util.List;

public interface StylePack<E> {

    Button[] getBotonera(Context contexto);

    void setNombre(@StringRes int nuevoNombre);
    @StringRes
    int getNombre();

    void setIcono(@DrawableRes int nuevoIcono);
    @DrawableRes
    int getIcono();

    List<E> generateRandomSentence(Difficulty difficulty);
}
