package org.iesmurgi.cristichi.ddbb;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Session {

    public static boolean logged = false;
    public static User user;

    public static boolean Login(String nick, String pass){
        LoginMYSQL login = new LoginMYSQL();
        try{
            login.execute(nick, pass);
            logged = true;
            user = login.get();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}

class LoginMYSQL extends AsyncTask<String, Void, User> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected User doInBackground(String... params) {
        if (params.length!=2){
            throw new IllegalArgumentException("Wrong params in LoginMYSQL.execute(), use (String username, String password)");
        }
        User user = new User();
        String nick = params[0];
        String pass = params[1];

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.d("CRISTICHIEX", "Conectando");
            con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);
            Log.d("CRISTICHIEX", "Success");

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT Nickname, Email from Users where Nickname='"+nick+"' and Pass='"+pass+"'");

            while (rs.next()) {
                user.nick = rs.getString(0);
                user.email = rs.getString(1);
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
        return user;
    }
}
