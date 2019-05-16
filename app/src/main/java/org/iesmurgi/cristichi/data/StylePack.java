package org.iesmurgi.cristichi.data;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface StylePack<E> extends Serializable {

    ArrayList<Button> getButtons(Context ctxt);

    void setName(@StringRes int name);
    @StringRes
    int getName();

    @DrawableRes
    int getIcon();

    List<E> generateRandomSentence(Difficulty difficulty);
}
