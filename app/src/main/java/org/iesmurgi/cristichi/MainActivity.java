package org.iesmurgi.cristichi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.button);
        btn.setBackground(getResources().getDrawable(R.drawable.ic_launcher_foreground));
        WordStylePack.PECADOS_CAPITALES.generateRandomSentence(Difficulty.EXTREME);
    }
}
