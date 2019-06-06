package org.iesmurgi.cristichi.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.mysql.jdbc.NotImplemented;

import org.iesmurgi.cristichi.R;
import org.iesmurgi.cristichi.ddbb.DDBBConstraints;
import org.iesmurgi.cristichi.ddbb.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Tarea asíncrona para publicar un Modo de Juego personalizado
 */
public class UploadCustomGamemode extends AsyncTask<Void, Void, Boolean> {
    private Activity ctxt;
    private AlertDialog dialog;
    private String name;
    private String values;

    public UploadCustomGamemode(Activity ctxt, String name, String values){
        this.ctxt = ctxt;
        this.name = name;
        this.values = values;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new AlertDialog.Builder(ctxt)
                .setCancelable(false)
                .setTitle(R.string.custom_editor_uploading_title)
                .setMessage(R.string.custom_editor_uploading_message)
                .show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean sol = false;
        Connection con = null;
        try {
            if (Session.isLogged()){
                Class.forName("com.mysql.jdbc.Driver");
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                DriverManager.setLoginTimeout(2);
                Log.d("CRISTICHIEX", DriverManager.getLoginTimeout()+"");
                con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);


                Statement st = con.createStatement();
                st.setQueryTimeout(2);
                st.execute("insert into CustomGamemodes(UserEmail, Name, GameValues, Downloads) " +
                        "values(\""+Session.getUser().email+"\", \""+name+"\", \""+values+"\", 0)");
                sol = true;
            }
        }catch (Exception e) {
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

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean){
            dialog.setMessage(ctxt.getString(R.string.custom_editor_uploading_good));
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ctxt.finish();
                }
            });
        } else {
            dialog.setMessage(ctxt.getString(R.string.custom_editor_uploading_bad));
        }
        dialog.setCancelable(true);
    }
}