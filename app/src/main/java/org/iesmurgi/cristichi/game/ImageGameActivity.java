package org.iesmurgi.cristichi.game;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.iesmurgi.cristichi.Difficulty;
import org.iesmurgi.cristichi.R;
import org.iesmurgi.cristichi.stylePacks.ImageStylePack;

import java.util.List;

public class ImageGameActivity extends AppCompatActivity {

    private float screenWidth;
    private float btnSize;
    private float imageSize;

    private ImageStylePack sp;
    private Difficulty diff;

    private ScrollView scrollView;
    private LinearLayout llSerialView;
    private TableLayout tlButtons;

    private List<Integer> secuence;

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


        try{
            Bundle extras = getIntent().getExtras();
            sp = ImageStylePack.values()[extras.getInt("stylePack", -1)];
            diff = Difficulty.values()[extras.getInt("difficulty", -1)];

            btnSize = getResources().getDimension(R.dimen.btn_image_width);
            imageSize = getResources().getDimension(R.dimen.image_width);

            /// Y AQUÍ OCURRE LA MAGIA

            TextView title = findViewById(R.id.tvTitle);
            title.setText(sp.getName());

            scrollView = findViewById(R.id.svSerial);

            llSerialView = findViewById(R.id.llSerialView);
            tlButtons = findViewById(R.id.tlButtons);

            secuence = sp.generateRandomSentence(diff);
            for(Integer img : secuence){
                ImageView tv = new ImageView(this);
                tv.setPadding(5,5,5,5);
                tv.setImageResource(img);
                llSerialView.addView(tv);
                ViewGroup.LayoutParams lp = tv.getLayoutParams();
                lp.height = lp.width = (int) imageSize;
            }

            randomizeBtns();
        }catch (NullPointerException | IndexOutOfBoundsException e){
            Log.e("CRISTICHIEX", e.toString());
            Log.getStackTraceString(e);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error_access_game_tit);
            builder.setMessage(R.string.error_access_game_msg);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ImageGameActivity.this.finish();
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
            android.widget.TableRow.LayoutParams p = new android.widget.TableRow.LayoutParams();
            p.rightMargin = 5;
            btn.setLayoutParams(p);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer car = (int) v.getTag();
                    if (secuence.get(0).equals(car)){
                        llSerialView.removeViewAt(0);
                        secuence.remove(0);
                        if (secuence.isEmpty()){
                            ImageGameActivity.this.finish();
                        }
                    }else{
                        secuence.add(car);
                        ImageView tv = new ImageView(ImageGameActivity.this);
                        tv.setImageResource(car);
                        llSerialView.addView(tv);
                        ViewGroup.LayoutParams lp = tv.getLayoutParams();
                        lp.height = lp.width = (int) imageSize;
                        tv.setPadding(5,5,5,5);
                        scrollView.fullScroll(View.FOCUS_UP);
                    }
                    randomizeBtns();
                }
            });
            rowWidth+=btnSize;
            if (rowWidth + 40 > screenWidth){
                //Log.d("CRISTICHIEX", "IF btn btnSize="+btnSize+", rowWidth="+rowWidth+", screenWidth="+ screenWidth +", realBtnSize="+btn.getWidth()+", i="+i);
                tlButtons.addView(row);
                row = new TableRow(this);
                row.setGravity(Gravity.CENTER);
                row.addView(btn);

                rowWidth=btnSize;
            }else{
                //Log.d("CRISTICHIEX", "ELSE btn btnSize="+btnSize+", rowWidth="+rowWidth+", screenWidth="+ screenWidth +", realBtnSize="+btn.getWidth()+", i="+i);
                row.addView(btn);
            }
            ViewGroup.LayoutParams lp = btn.getLayoutParams();
            lp.height = lp.width = (int) btnSize;
        }
        tlButtons.addView(row);

    }
}
