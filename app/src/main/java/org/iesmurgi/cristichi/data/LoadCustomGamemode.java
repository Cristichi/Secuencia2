package org.iesmurgi.cristichi.data;

import android.os.AsyncTask;
import android.util.Log;

import org.iesmurgi.cristichi.ddbb.DDBBConstraints;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoadCustomGamemode extends AsyncTask<Void, Void, CustomGamemode> {
    private boolean exception;

    private int id;

    public LoadCustomGamemode(int id){
        this.id = id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected CustomGamemode doInBackground(Void... voids) {
        CustomGamemode sol = null;
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            DriverManager.setLoginTimeout(2);
            Log.d("CRISTICHIEX", DriverManager.getLoginTimeout()+"");
            con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);


            Statement st = con.createStatement();
            st.setQueryTimeout(2);
            ResultSet rs = st.executeQuery("SELECT UserEmail, Name, GameValues from CustomGamemodes where Id='"+id+"'");

            if (rs.next()) {
                String userEmail = rs.getString(1);
                String name = rs.getString(2);
                String values = rs.getString(3);
                sol = new CustomGamemode(id, userEmail, name, values);
            }else{
                exception = true;
            }
        }catch (Exception e) {
            e.printStackTrace();
            exception = true;
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