package org.iesmurgi.cristichi.ddbb;

import android.content.Context;

import org.iesmurgi.cristichi.LoginActivity;
import org.iesmurgi.cristichi.ddbb.ReturnLogin;
import org.iesmurgi.cristichi.ddbb.Session;
import org.iesmurgi.cristichi.ddbb.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Clase que engloba diferentes métodos que acceden a un archivo local donde se guardan
 * las credenciales de 1 Usuario.
 */
public class LocalStorage {

    private static final String FILE = "loggeduser.bin";

    /**
     * Intenta guardar las credenciales de un Usuario en un archivo local.
     * @param context Contexto de la actividad que ejecuta este método.
     * @param user Usuario a guardar.
     * @param pass Contraseña del usuario.
     */
    static void trySaveUser(Context context, User user, String pass){
        File dir = context.getFilesDir();
        File file = new File(dir, FILE);

        try{
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(user.email);
            bw.newLine();
            bw.write(pass);
            bw.newLine();
            bw.write(user.nick);
            bw.close();
        }catch (IOException e){
        }
    }

    /**
     * Intenta iniciar sesión con los datos del archivo local, si lo hay.
     * @param context Contexto de la actividad que ejecuta este método.
     */
    public static void tryLoginFromFile(Context context){
        File dir = context.getFilesDir();
        File file = new File(dir, FILE);

        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String email = br.readLine();
            String pass = br.readLine();
            LoginActivity.LoginTask loginTask = new LoginActivity.LoginTask(context, email, pass);
            loginTask.execute();
            loginTask.get(2, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Cierra sesión y borra las credenciales del Usuario en el archivo local.
     * @param context
     * @return True, si se ha cerrado sesión; false en otro caso.
     */
    public static boolean logout(Context context){
        File dir = context.getFilesDir();
        File file = new File(dir, FILE);

        try{
            if (!file.exists()){
                return true;
            }
            if (file.delete()){
                Session.logout();
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
