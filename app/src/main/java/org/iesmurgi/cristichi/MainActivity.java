package org.iesmurgi.cristichi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Integer> lista = WordStylePack.PECADOS_CAPITALES.generateRandomSentence(Difficulty.MEDIUM);
        for (int inte: lista) {
            Log.d("CRISTICHIEX", getResources().getString(inte));
        }
    }
}
