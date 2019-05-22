package org.iesmurgi.cristichi;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.iesmurgi.cristichi.ddbb.DDBBConstraints;
import org.iesmurgi.cristichi.ddbb.ReturnLogin;
import org.iesmurgi.cristichi.ddbb.Session;
import org.iesmurgi.cristichi.ddbb.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditAccountActivity extends ActivityWithMusic {

    private User user;

    private EditText etEmail, etNick, etOldPass1, etOldPass2, etNewPass1, etNewPass2;
    private Button btnBack, btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        if (Session.isLogged()){
            user = Session.getUser();

            etEmail = findViewById(R.id.etEmail);
            etNick = findViewById(R.id.etNick);
            etOldPass1 = findViewById(R.id.etOldPass1);
            etOldPass2 = findViewById(R.id.etOldPass2);
            etNewPass1 = findViewById(R.id.etNewPass1);
            etNewPass2 = findViewById(R.id.etNewPass2);

            btnBack = findViewById(R.id.btnBack);
            btnUpdate = findViewById(R.id.btnUpdate);

            etEmail.setHint(user.email);
            etNick.setHint(user.nick);

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newEmail, newNick,
                            oldPass1, oldPass2,
                            newPass1, newPass2;
                    newEmail = etEmail.getText().toString().trim();
                    newNick = etNick.getText().toString().trim();
                    oldPass1 = etOldPass1.getText().toString();
                    oldPass2 = etOldPass2.getText().toString();
                    newPass1 = etNewPass1.getText().toString();
                    newPass2 = etNewPass2.getText().toString();

                    if (oldPass1.isEmpty()){
                        new AlertDialog.Builder(EditAccountActivity.this)
                                .setMessage(R.string.account_update_error_no_pass)
                                .show();

                    } else if (!oldPass1.equals(oldPass2)){
                        new AlertDialog.Builder(EditAccountActivity.this)
                                .setMessage(R.string.account_update_error_old_pass_not_match)
                                .show();

                    } else if (newEmail.isEmpty() && newNick.isEmpty() && newPass1.isEmpty()){
                        new AlertDialog.Builder(EditAccountActivity.this)
                                .setMessage(R.string.account_update_error_no_changes)
                                .show();

                    } else if (!newPass1.isEmpty() && !newPass1.equals(newPass2)){
                        new AlertDialog.Builder(EditAccountActivity.this)
                                .setMessage(R.string.account_update_error_new_pass_not_match)
                                .show();

                    } else {
                        if (newEmail.isEmpty()){
                            newEmail = user.email;
                        }
                        if (newNick.isEmpty()){
                            newNick = user.nick;
                        }
                        if (newPass1.isEmpty()){
                            newPass1 = oldPass1;
                        }
                        EditAccountTask task = new EditAccountTask(EditAccountActivity.this, user.email, user.nick, oldPass1, newEmail, newNick, newPass1);
                        task.execute();
                    }
                }
            });
        }else{
            finish();
        }
    }

    class EditAccountTask extends AsyncTask<Void, Void, ReturnLogin> {

        private AlertDialog dial;
        private Context ctxt;
        private String oldEmail, oldNick, oldPass, newEmail, newNick, newPass;

        public EditAccountTask(Context ctxt, String oldEmail, String oldNick, String oldPass, String newEmail, String newNick, String newPass){
            this.ctxt = ctxt;
            this.oldEmail = oldEmail;
            this.oldNick = oldNick;
            this.oldPass = oldPass;
            this.newEmail = newEmail;
            this.newNick = newNick;
            this.newPass = newPass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dial = new AlertDialog.Builder(ctxt)
                    .setTitle(R.string.account_update_loging_dial_title)
                    .setMessage(R.string.account_update_loging_dial_msg)
                    .setCancelable(false)
                    .show();
        }

        @Override
        protected ReturnLogin doInBackground(Void... params) {
            ReturnLogin sol = new ReturnLogin();
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DDBBConstraints.URL_DDBB,
                        DDBBConstraints.USER, DDBBConstraints.PASSWORD);

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(
                        "select Email from Users where Email='"+newEmail+"'");

                if (rs.next() && !oldEmail.equals(newEmail)) {
                    sol.e = new Session.ServerException(ctxt.getString(R.string.account_update_error_email_exists));
                }else{
                    Statement st2 = con.createStatement();
                    ResultSet rs2 = st2.executeQuery(
                            "select Email from Users where Nickname='"+newNick+"'");
                    if (rs2.next() && !oldNick.equals(newNick)) {
                        sol.e = new Session.ServerException(ctxt.getString(R.string.account_update_error_nick_exists));
                    }else{
                        Statement st3 = con.createStatement();
                        int updates = st3.executeUpdate("update Users set Email='"+newEmail+"', Nickname = '"+newNick+"', Pass='"+Session.encrypt(newPass)+"' where Email='"+oldEmail+"' and Pass='"+Session.encrypt(oldPass)+"'");
                        if (updates>0){
                            sol.user = new User(newNick, newEmail);
                            Session.login(ctxt, newEmail, newPass);
                        }else{
                            sol.e = new Session.ServerException(ctxt.getString(R.string.account_update_error_wrong_pass));
                        }
                    }
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
            return sol;
        }

        @Override
        protected void onPostExecute(ReturnLogin sol) {
            super.onPostExecute(sol);
            dial.dismiss();
            if (sol.e!=null){
                new AlertDialog.Builder(EditAccountActivity.this)
                        .setMessage(sol.e.getMessage())
                        .show();
            }else
                EditAccountActivity.this.finish();
        }
    }
}
