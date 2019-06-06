package org.iesmurgi.cristichi.ddbb;

import android.content.Context;

import org.iesmurgi.cristichi.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.security.auth.login.LoginException;

/**
 * Clase que representa la sesión de un Usuario e informa de su estado.
 */
public class Session {

    private static boolean logged = false;
    private static User user;

    /**
     *
     * @return True, si un Usuario tiene la sesión iniciada; false en otro caso.
     */
    public static boolean isLogged() {
        return logged;
    }

    /**
     *
     * @return El Usuario que ha iniciado sesión o null.
     */
    public static User getUser() {
        return user;
    }

    /**
     * Intenta iniciar sesión. Se debe usar en una tarea asíncrona.
     * @param ctxt Contexto de la actividad que ejecuta el método.
     * @param email Email del Usuario.
     * @param pass Contraseña del Usuario, sin encriptar.
     * @return Un objeto ReturnLogin con un error o un usuario.
     */
    public static ReturnLogin login(Context ctxt, String email, String pass){
        ReturnLogin sol = new ReturnLogin();

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.setLoginTimeout(2);
            con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT Nickname, Email from Users where Email='" + email + "' and Pass='" + Session.encrypt(pass) + "'");

            if (rs.first()) {
                String nickread = rs.getString(1);
                String emailread = rs.getString(2);
                sol.user = new User(nickread, emailread);
                LocalStorage.trySaveUser(ctxt, sol.user, pass);
                logged = true;
                Session.user = sol.user;
            }else{
                sol.e = new LoginException(ctxt.getString(R.string.error_login));
            }
        }catch (Exception e) {
            e.printStackTrace();
            sol.e = new ServerException(ctxt.getString(R.string.error_net));
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

    /**
     * Cierra la sesión actual, si la hay.
     */
    public static void logout(){
        logged = false;
        user = null;
    }

    /**
     * Encripta una cadena de texto.
     * @param s Cadena de texto
     * @return La misma cadena de texto, encriptada
     */
    public static String encrypt(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Excepción que representa un fallo por parte del servidor.
     */
    public static class ServerException extends RuntimeException{
        public ServerException(String msg) {
            super(msg);
        }
    }
}

