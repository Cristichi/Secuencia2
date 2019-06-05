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

    List<Button> getButtons(Context ctxt);

    String getCode();

    @StringRes
    int getName();

    @DrawableRes
    int getIcon();

    List<E> generateRandomSecuence(Difficulty difficulty);
}
