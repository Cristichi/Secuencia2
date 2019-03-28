package org.iesmurgi.cristichi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.iesmurgi.cristichi.stylePacks.StylePack;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView tv = findViewById(R.id.tvOUTPUT);
        Bundle extras = getIntent().getExtras();
        try{
            StylePack sp = (StylePack) extras.get("stylePack");
            Difficulty diff = Difficulty.values()[extras.getInt("difficulty", -1)];

            /// Y AQUÍ OCURRE LA MAGIA

        }catch (NullPointerException | IndexOutOfBoundsException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(e.getClass().getName());
            builder.setMessage(e.getMessage());
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    GameActivity.this.finish();
                }
            });
            builder.show();
        }
    }
}
