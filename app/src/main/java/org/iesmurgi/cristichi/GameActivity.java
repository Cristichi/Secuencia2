package org.iesmurgi.cristichi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
            tv.setText(sp.getName());
        }catch (NullPointerException e){
            tv.setText("ES LA NOCHE FATAL");
        }
    }
}
