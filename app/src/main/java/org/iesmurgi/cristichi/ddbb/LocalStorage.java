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

public class LocalStorage {

    private static final String FILE = "loggeduser.bin";

    public static boolean saveUser(Context context, User user, String pass){
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
            return true;
        }catch (IOException e){
            return false;
        }
    }

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
