package org.iesmurgi.cristichi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_menu);

        Button btnMM = findViewById(R.id.btnMyGamemodes);
        btnMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomMenuActivity.this, MyCustomGamemodesActivity.class);
                startActivity(intent);
            }
        });
    }
}
