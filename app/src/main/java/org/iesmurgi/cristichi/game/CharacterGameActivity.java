package org.iesmurgi.cristichi.game;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.iesmurgi.cristichi.Difficulty;
import org.iesmurgi.cristichi.R;
import org.iesmurgi.cristichi.stylePacks.CharacterStylePack;

import java.util.List;

public class CharacterGameActivity extends AppCompatActivity {

    private int screenWidth;
    private int btnSize;
    private float charSize;

    private CharacterStylePack sp;
    private Difficulty diff;

    private ScrollView scrollView;
    private LinearLayout llSerialView;
    private TableLayout tlButtons;

    private List<Character> secuence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        //screenWidth = outMetrics.widthPixels / density;
        screenWidth = outMetrics.widthPixels;

        btnSize = (int) getResources().getDimension(R.dimen.btn_char_width);
        charSize = getResources().getDimension(R.dimen.char_size)/density;

        try{
            Bundle extras = getIntent().getExtras();
            sp = CharacterStylePack.values()[extras.getInt("stylePack", -1)];
            diff = Difficulty.values()[extras.getInt("difficulty", -1)];

            /// Y AQUÍ OCURRE LA MAGIA

            TextView title = findViewById(R.id.tvTitle);
            title.setText(sp.getName());

            scrollView = findViewById(R.id.svSerial);

            llSerialView = findViewById(R.id.llSerialView);
            tlButtons = findViewById(R.id.tlButtons);

            secuence = sp.generateRandomSentence(diff);
            for(Character car : secuence){
                TextView tv = new TextView(this);
                tv.setPadding(5,5,5,5);
                tv.setText(car.toString());
                tv.setTextSize(charSize);
                llSerialView.addView(tv);
            }

            randomizeBtns();
        }catch (NullPointerException | IndexOutOfBoundsException e){
            Log.e("CRISTICHIEX", "Error: "+e);
            Log.e("CRISTICHIEX", "Error: "+e);
            Log.e("CRISTICHIEX", "Error: "+e);
            Log.e("CRISTICHIEX", "Error: "+e);
            Log.e("CRISTICHIEX", "Error: "+e);
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error_access_game_tit);
            builder.setMessage(R.string.error_access_game_msg);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    CharacterGameActivity.this.finish();
                }
            });
            builder.show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        /*
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
        }
        */


        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

        randomizeBtns();
        Log.d("CRISTICHIEX", "RANDOMIZANDO");
    }

    private void randomizeBtns(){
        List<Button> buttons = sp.getButtons(this);
        tlButtons.removeAllViews();

        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER);

        float rowWidth = 0;
        for(int i=0; i<buttons.size(); i++){
            Button btn = buttons.get(i);
            btn.setPadding(5,5,5,5);
            btn.setWidth(btnSize);
            btn.setAllCaps(false);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Character car = (char)v.getTag();
                    if (secuence.get(0).equals(car)){
                        llSerialView.removeViewAt(0);
                        secuence.remove(0);
                        if (secuence.isEmpty()){
                            CharacterGameActivity.this.finish();
                        }
                    }else{
                        secuence.add(car);
                        TextView tv = new TextView(CharacterGameActivity.this);
                        tv.setPadding(5,5,5,5);
                        tv.setText(car.toString());
                        llSerialView.addView(tv);
                        tv.setTextSize(charSize);
                        scrollView.fullScroll(View.FOCUS_UP);
                    }
                    randomizeBtns();
                }
            });
            rowWidth+=btnSize;
            if (rowWidth> screenWidth){
                //Log.d("CRISTICHIEX", "btn screenWidth="+btnSize+", row screenWidth="+rowWidth+", screen screenWidth="+ screenWidth +", i="+i+", getMaxWidth="+btn.getMaxWidth());
                tlButtons.addView(row);
                row = new TableRow(this);
                row.setGravity(Gravity.CENTER);
                row.addView(btn);

                rowWidth=btnSize;
            }else{
                //Log.d("CRISTICHIEX", "i="+i);
                row.addView(btn);
            }
            btn.setMaxWidth(btnSize);
            btn.setMaxHeight(btnSize);
            ViewGroup.LayoutParams lp = btn.getLayoutParams();
            lp.width = btnSize;
        }
        tlButtons.addView(row);
    }
}