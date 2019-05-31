package org.iesmurgi.cristichi.data;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import org.iesmurgi.cristichi.ddbb.DDBBConstraints;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoadInfoCustomGamemodes extends AsyncTask<Void, Void, List<InfoCustomGamemode>> {
    private boolean exception;

    private String search;

    public LoadInfoCustomGamemodes(@Nullable String search){
        this.search = search;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<InfoCustomGamemode> doInBackground(Void... voids) {
        List<InfoCustomGamemode> sol = new ArrayList<>();
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.setLoginTimeout(2);
            con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT Id, UserEmail, Name from CustomGamemodes "+(search==null?"":"where Name like \"%"+search+"%\" or UserEmail like \"%"+search+"%\" ")+"order by Downloads"+(search==null?" limit 50":""));
            Log.d("CRISTICHIEX", "SELECT Id, UserEmail, Name from CustomGamemodes "+(search==null?"":"where Name like \"%"+search+"%\" or UserEmail like \"%"+search+"%\" ")+"order by Downloads"+(search==null?" limit 50":""));

            while (rs.next()) {
                int id = rs.getInt(1);
                String email = rs.getString(2);
                String name = rs.getString(3);
                sol.add(new InfoCustomGamemode(id, email, name));
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