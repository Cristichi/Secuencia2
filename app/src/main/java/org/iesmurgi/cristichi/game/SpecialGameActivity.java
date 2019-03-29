package org.iesmurgi.cristichi.game;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.iesmurgi.cristichi.Difficulty;
import org.iesmurgi.cristichi.R;
import org.iesmurgi.cristichi.stylePacks.StylePack;

public class SpecialGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_game);

        try{
            Bundle extras = getIntent().getExtras();
            StylePack sp = (StylePack) extras.get("stylePack");
            Difficulty diff = Difficulty.values()[extras.getInt("difficulty", -1)];

            /// Y AQUÍ OCURRE LA MAGIA

        }catch (NullPointerException | IndexOutOfBoundsException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error_access_game_tit);
            builder.setMessage(R.string.error_access_game_msg);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    SpecialGameActivity.this.finish();
                }
            });
            builder.show();
        }
    }
}
