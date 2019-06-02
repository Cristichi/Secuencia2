package org.iesmurgi.cristichi.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.iesmurgi.cristichi.R;
import org.iesmurgi.cristichi.ddbb.DDBBConstraints;
import org.iesmurgi.cristichi.ddbb.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteCustomGamemode extends AsyncTask<Void, Void, Boolean> {
    private Activity ctxt;
    private AlertDialog dialog;
    private int id;

    public DeleteCustomGamemode(Activity ctxt, int id){
        this.ctxt = ctxt;
        this.id = id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new AlertDialog.Builder(ctxt)
                .setCancelable(false)
                .setTitle(R.string.custom_list_delete_title)
                .setMessage(R.string.custom_list_delete_message)
                .show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean sol = false;
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            DriverManager.setLoginTimeout(2);
            Log.d("CRISTICHIEX", DriverManager.getLoginTimeout()+"");
            con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);

            Statement st = con.createStatement();
            st.setQueryTimeout(2);
            st.execute("delete from CustomGamemodes where Id=\""+id+"\"");
            sol = true;
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
            dialog.setMessage(ctxt.getString(R.string.custom_list_delete_good));
        } else {
            dialog.setMessage(ctxt.getString(R.string.custom_list_delete_bad));
        }
        dialog.setCancelable(true);
    }
}