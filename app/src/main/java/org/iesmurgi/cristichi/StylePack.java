package org.iesmurgi.cristichi;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import java.util.List;

public interface StylePack<E> {

    Button[] getButtons(Context contexto);

    void setName(@StringRes int name);
    @StringRes
    int getName();

    void setIcon(@DrawableRes int icon);
    @DrawableRes
    int getIcon();

    List<E> generateRandomSentence(Difficulty difficulty);
}
