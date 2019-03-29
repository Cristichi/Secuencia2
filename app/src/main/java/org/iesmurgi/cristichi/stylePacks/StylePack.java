package org.iesmurgi.cristichi.stylePacks;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.Button;

import org.iesmurgi.cristichi.Difficulty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface StylePack<E> extends Serializable {

    ArrayList<Button> getButtons(Context ctxt);

    void setName(@StringRes int name);
    @StringRes
    int getName();

    void setIcon(@DrawableRes int icon);
    @DrawableRes
    int getIcon();

    List<E> generateRandomSentence(Difficulty difficulty);
}
