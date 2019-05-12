package org.iesmurgi.cristichi.ddbb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.iesmurgi.cristichi.LoginActivity;
import org.iesmurgi.cristichi.storage.StorageHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.security.auth.login.LoginException;

public class Session {

    private static boolean logged = false;
    private static User user;

    public static boolean isLogged() {
        return logged;
    }

    public static User getUser() {
        return user;
    }

    public static void login(Context ctxt, User user, String pass){
        logged = true;
        Session.user = user;
        StorageHelper.saveUser(ctxt, Session.getUser(), pass);
    }

    public static Throwable login(Context ctxt, String email, String pass){
        LoginMYSQL login = new LoginMYSQL();
        try{
            login.execute(email, pass);
            ReturnLogin ret = login.get();
            if (ret.e == null){
                logged = true;
                user = ret.user;
                StorageHelper.saveUser(ctxt, Session.getUser(), pass);
                return null;
            }else{
                logged = false;
                return ret.e;
            }
        }catch (Exception e){
            e.printStackTrace();
            return e;
        }
    }

    public static void logout(){
        logged = false;
        user = null;
    }
}

class ReturnLogin{
    Throwable e = null;
    User user = null;
}

class LoginMYSQL extends AsyncTask<String, Void, ReturnLogin> {

    @Override
    protected ReturnLogin doInBackground(String... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Wrong params in LoginMYSQL.execute(), use (String email, String password)");
        }
        ReturnLogin sol = new ReturnLogin();
        String email = params[0];
        String pass = params[1];

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.d("CRISTICHIEX", "Conectando");
            con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);
            Log.d("CRISTICHIEX", "Success");

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT Nickname, Email from Users where Email='" + email + "' and Pass='" + pass + "'");

            if (rs.first()) {
                sol.user = new User(rs.getString(1), rs.getString(2));
            } else {
                sol.e = new LoginException();
            }
        }catch (Exception e) {
            e.printStackTrace();
            sol.e = e;
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
