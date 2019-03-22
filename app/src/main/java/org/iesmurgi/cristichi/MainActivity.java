package org.iesmurgi.cristichi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WordStylePack.PECADOS_CAPITALES.generateRandomSentence(Difficulty.NOOB);
        WordStylePack.PECADOS_CAPITALES.generateRandomSentence(Difficulty.EASY);
        WordStylePack.PECADOS_CAPITALES.generateRandomSentence(Difficulty.MEDIUM);
        WordStylePack.PECADOS_CAPITALES.generateRandomSentence(Difficulty.EXTREME);
    }
}
