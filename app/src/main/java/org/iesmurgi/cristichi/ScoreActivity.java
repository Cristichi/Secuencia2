package org.iesmurgi.cristichi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Locale;

public class ScoreActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvGamemode;
    private TextView tvDifficulty;
    private TextView tvScore;

    private Button btnBack;
    private Button btnScreenshot;

    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Date now = new Date();
        final CharSequence date = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", now);

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
            double score = extras.getDouble("score");
            int gamemode = extras.getInt("gamemode");
            int difficulty = extras.getInt("difficulty");

            tvScore.setText(String.format(Locale.getDefault(), "%.3f", score));
            tvGamemode.setText(gamemode);
            tvDifficulty.setText(difficulty);
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
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri photoURI = FileProvider.getUriForFile(this, "com.iesmurgi.cristichi", imageFile);
        intent.setDataAndType(photoURI, "image/*");
        startActivity(intent);
    }
}