package org.iesmurgi.cristichi.game;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
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
import org.iesmurgi.cristichi.stylePacks.WordStylePack;

import java.util.List;

public class WordGameActivity extends AppCompatActivity {

    private int screenWidth;
    private int btnSize;
    private float wordSize;

    private WordStylePack sp;
    private Difficulty diff;

    private ScrollView scrollView;
    private LinearLayout llSerialView;
    private TableLayout tlButtons;

    private List<Integer> secuence;

    @SuppressLint("ClickableViewAccessibility")
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

        btnSize = (int) getResources().getDimension(R.dimen.btn_word_width);
        wordSize = getResources().getDimension(R.dimen.word_size)/density;

        try{
            Bundle extras = getIntent().getExtras();
            sp = WordStylePack.values()[extras.getInt("stylePack", -1)];
            diff = Difficulty.values()[extras.getInt("difficulty", -1)];

            /// Y AQUÍ OCURRE LA MAGIA

            TextView title = findViewById(R.id.tvTitle);
            title.setText(sp.getName());

            scrollView = findViewById(R.id.svSerial);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            llSerialView = findViewById(R.id.llSerialView);

            tlButtons = findViewById(R.id.tlButtons);

            secuence = sp.generateRandomSentence(diff);
            for(Integer car : secuence){
                TextView tv = new TextView(this);
                tv.setText(car);
                tv.setTextSize(wordSize);
                tv.setPadding(5,5,10,5);
                llSerialView.addView(tv);
                tv.measure(0,0);
                scrollView.getLayoutParams().height = tv.getMeasuredHeight();
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
                    WordGameActivity.this.finish();
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
            btn.setWidth(btnSize);
            btn.setAllCaps(false);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer car = (int)v.getTag();
                    if (secuence.get(0).equals(car)){
                        llSerialView.removeViewAt(0);
                        secuence.remove(0);
                        if (secuence.isEmpty()){
                            WordGameActivity.this.finish();
                        }
                    }else{
                        secuence.add(car);
                        TextView tv = new TextView(WordGameActivity.this);
                        tv.setText(car);
                        llSerialView.addView(tv);
                        tv.setTextSize(wordSize);
                        scrollView.fullScroll(View.FOCUS_UP);
                    }
                    randomizeBtns();
                }
            });
            rowWidth+=btnSize;
            if (rowWidth> screenWidth){
                btn.requestLayout();
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
            //lp.width = btnSize;
        }
        tlButtons.addView(row);
    }
}
