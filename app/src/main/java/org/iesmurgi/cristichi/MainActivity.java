package org.iesmurgi.cristichi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnPlay;
    private Button btnOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(MainActivity.this, TabbedMenuActivity.class);
                startActivity(intento);
                //finish();
            }
        });

        btnOptions = findViewById(R.id.btnOptions);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intento);
                //finish();
            }
        });
    }
}
