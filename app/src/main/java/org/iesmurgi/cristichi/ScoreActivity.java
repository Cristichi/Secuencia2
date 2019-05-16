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

    private TextView tvGamemode;
    private TextView tvDifficulty;
    private TextView tvScore;

    private Button btnBack;

    private int score;
    private CharSequence date;
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Date now = new Date();
        date = android.text.format.DateFormat.format(DATE_FORMAT, now);

        tvGamemode = findViewById(R.id.tvGamemode);
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvScore = findViewById(R.id.tvScore);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreActivity.this.onBackPressed();
            }
        });

        try {
            Bundle extras = getIntent().getExtras();
            score = extras.getInt("score", 0);
            int gamemode = extras.getInt("gamemode");
            int difficulty = extras.getInt("difficulty");

            //tvScore.setText(String.format(Locale.getDefault(), "%.3f", score));
            tvScore.setText(score+"");
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
                        TextView tvHS = findViewById(R.id.tvHighScore);
                        tvHS.setVisibility(View.VISIBLE);
                        SaveScoreMYSQL taskSave = new SaveScoreMYSQL(score, user.email, mode, diff, date.toString());
                        taskSave.execute();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
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

