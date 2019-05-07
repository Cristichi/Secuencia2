package org.iesmurgi.cristichi;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.iesmurgi.cristichi.ddbb.DDBBConstraints;
import org.iesmurgi.cristichi.ddbb.Session;
import org.iesmurgi.cristichi.ddbb.User;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvGamemode;
    private TextView tvDifficulty;
    private TextView tvScore;

    private Button btnBack;
    private Button btnScreenshot;

    private double score;
    private boolean highScore;
    private CharSequence date;
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Date now = new Date();
        date = android.text.format.DateFormat.format(DATE_FORMAT, now);

        tvTitle = findViewById(R.id.tvTitle);
        final TextView tvDate = findViewById(R.id.tvDate);
        tvGamemode = findViewById(R.id.tvGamemode);
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvScore = findViewById(R.id.tvScore);
        btnBack = findViewById(R.id.btnBack);
        btnScreenshot = findViewById(R.id.btnScreenshot);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreActivity.this.onBackPressed();
            }
        });

        btnScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ScoreActivity.this);
                dialog.setContentView(R.layout.screenshot_ask);
                dialog.setTitle(R.string.screenshot_ask_title);

                final EditText etName = dialog.findViewById(R.id.etYourName);
                final Switch switchDate = dialog.findViewById(R.id.switchIncludeDate);

                Button dialogButton = dialog.findViewById(R.id.btnOk);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        btnBack.setVisibility(View.INVISIBLE);
                        btnScreenshot.setVisibility(View.INVISIBLE);
                        if (switchDate.isChecked()){
                            tvDate.setText(date);
                        }
                        String title = tvTitle.getText().toString();
                        String name = etName.getText().toString().trim();
                        tvTitle.setText(name);
                        takeScreenshot();
                        tvTitle.setText(title);
                        tvDate.setText("");
                        btnBack.setVisibility(View.VISIBLE);
                        btnScreenshot.setVisibility(View.VISIBLE);
                    }
                });
                dialog.show();
            }
        });

        try {
            Bundle extras = getIntent().getExtras();
            score = extras.getDouble("score", 0);
            int gamemode = extras.getInt("gamemode");
            int difficulty = extras.getInt("difficulty");

            tvScore.setText(String.format(Locale.getDefault(), "%.3f", score));
            tvGamemode.setText(gamemode);
            tvDifficulty.setText(difficulty);

            if (Session.isLogged()){
                User user = Session.getUser();
                String mode = getString(gamemode);
                String diff = getString(difficulty);
                try{
                    IsHighScore task = new IsHighScore(score, user.email, mode, diff);
                    task.execute();
                    if (task.get(2, TimeUnit.SECONDS)){
                        highScore = true;
                        TextView tvHS = findViewById(R.id.tvHighScore);
                        tvHS.setVisibility(View.VISIBLE);
                        SaveScoreMYSQL taskSave = new SaveScoreMYSQL(score, user.email, mode, diff, date.toString());
                        taskSave.execute();
                    }else{
                        highScore = false;
                    }
                }catch (Exception e){}
            }
        }catch (NullPointerException e){
        }
    }

    private void takeScreenshot() {
        Date now = new Date();
        CharSequence date = android.text.format.DateFormat.format("yyyy-MM-dd hh.mm.ss", now);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                         1);
            }
        }
        /* */
        try {
            //String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + now + ".jpg";
            String mPath = Environment.getExternalStorageDirectory().toString() + "/screenshot (" + date + ").jpg";

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "image title", "image desc");

            openScreenshot(imageFile);
        } catch (Throwable e) {
            e.printStackTrace();
            new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setTitle(R.string.screenshot_ask_error_title)
                    .setMessage(getString(R.string.screenshot_ask_error_msg)+"\n"+e.getMessage())
                    .show();
        }
    }

    private void openScreenshot(File imageFile) {
        /* */
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri photoURI = FileProvider.getUriForFile(this, "com.iesmurgi.cristichi", imageFile);
        intent.setDataAndType(photoURI, "image/*");
        startActivity(intent);
        /* */
    }

    private static class SaveScoreMYSQL extends AsyncTask<Void, Void, Void> {
        private boolean ini;

        private double score;
        private String email;
        private String mode;
        private String diff;
        private String date;

        SaveScoreMYSQL(double score, String email, String mode, String diff, String date){
            ini = true;
            this.score = score;
            this.email = email;
            this.mode = mode;
            this.diff = diff;
            this.date = date;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (!ini){
                return null;
            }

            Connection con = null;
            try {
                Log.d("CRISTICHIEX", "delete from HighScores where UserEmail='"+email+"' and Gamemode='"+mode+"' and Difficulty='"+diff+"'");
                Log.d("CRISTICHIEX", "insert into HighScores values('"+email+"', '"+mode+"', '"+diff+"', '"+score+"', '"+date+"')");
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);

                Statement st = con.createStatement();
                st.execute(
                        "delete from HighScores where UserEmail='"+email+"' and Gamemode='"+mode+"' and Difficulty='"+diff+"'");
                Statement st2 = con.createStatement();
                st2.execute(
                        "insert into HighScores values('"+email+"', '"+mode+"', '"+diff+"', '"+score+"', '"+date+"')");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (con != null){
                try{
                    con.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private static class IsHighScore extends AsyncTask<Void, Void, Boolean> {
        private boolean ini;

        private double score;
        private String email;
        private String mode;
        private String diff;

        IsHighScore(double score, String email, String mode, String diff){
            ini = true;
            this.score = score;
            this.email = email;
            this.mode = mode;
            this.diff = diff;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (!ini){
                return false;
            }

            boolean sol = false;
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(
                        "select max(Score) from HighScores where UserEmail='"+email+"' and Gamemode='"+mode+"' and Difficulty='"+diff+"'");

                if (rs.next()) {
                    try{
                        float f = rs.getFloat(1);
                        Log.d("CRISTICHIEX", ""+f+"<"+score+"=="+(f<score));
                        if (f<score){
                            sol = true;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        sol = true;
                    }
                }else{
                    sol = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (con != null){
                try{
                    con.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            return sol;
        }
    }
}

