package org.iesmurgi.cristichi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.iesmurgi.cristichi.data.CustomGamemode;
import org.iesmurgi.cristichi.data.DeleteCustomGamemode;
import org.iesmurgi.cristichi.ddbb.DDBBConstraints;
import org.iesmurgi.cristichi.ddbb.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MyCustomGamemodesActivity extends ActivityWithMusic {

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_custom_gamemodes);
        if (!Session.isLogged()){
            finish();
            return;
        }

        Button btnAdd = findViewById(R.id.btnAddGamemode);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundSystem.playCartoonSlipUp();
                Intent intent = new Intent(MyCustomGamemodesActivity.this, CustomEditorActivity.class);
                startActivity(intent);
            }
        });

        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadMyCustomGamemodes task = new LoadMyCustomGamemodes(rv, Session.getUser().email);
        task.execute();
    }

    private static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDownloads;
        ImageButton btnRemove;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDownloads = itemView.findViewById(R.id.tvDownloads);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
        List<CustomGamemode> gamemodes;

        RecyclerAdapter(List<CustomGamemode> gamemodes){
            this.gamemodes = gamemodes;
        }

        public void setValues(List<CustomGamemode> gamemodes){
            this.gamemodes = gamemodes;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_custom_gamemode, viewGroup, false);
            return new RecyclerViewHolder(layoutView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
            final CustomGamemode gm = gamemodes.get(position);
            holder.tvName.setText(gm.getName());
            holder.tvDownloads.setText(String.valueOf(gm.getDownloads()));
            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundSystem.playRecordedPop();
                    new AlertDialog.Builder(MyCustomGamemodesActivity.this)
                            .setMessage(String.format(getString(R.string.custom_list_ask_delete_message), gm.getName()))
                            .setPositiveButton(R.string.custom_list_ask_delete_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    DeleteCustomGamemode task = new DeleteCustomGamemode(MyCustomGamemodesActivity.this, gm.getId()){
                                        @Override
                                        protected void onPostExecute(Boolean aBoolean) {
                                            super.onPostExecute(aBoolean);
                                            if (aBoolean){
                                                SoundSystem.playCartoonSlipFall();
                                                RecyclerAdapter.this.gamemodes.remove(gm);
                                                RecyclerAdapter.this.notifyDataSetChanged();
                                            }
                                        }
                                    };
                                    task.execute();
                                }
                            })
                            .setNegativeButton(R.string.custom_list_ask_delete_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return gamemodes.size();
        }
    }

    private class LoadMyCustomGamemodes extends AsyncTask<Void, Void, List<CustomGamemode>> {
        private boolean exception;

        private String userEmail;
        private RecyclerView rv;

        public LoadMyCustomGamemodes(RecyclerView rv, String userEmail){
            this.userEmail = userEmail;
            this.rv = rv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<CustomGamemode> doInBackground(Void... voids) {
            List<CustomGamemode> sol = new ArrayList<>();
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                DriverManager.setLoginTimeout(2);
                Log.d("CRISTICHIEX", DriverManager.getLoginTimeout()+"");
                con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);


                Statement st = con.createStatement();
                st.setQueryTimeout(2);
                ResultSet rs = st.executeQuery("SELECT Id, Name, GameValues, Downloads from CustomGamemodes where UserEmail='"+userEmail+"'");

                if (rs.next()) {
                    do{
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String values = rs.getString(3);
                        int downs = rs.getInt(4);
                        sol.add(new CustomGamemode(id, userEmail, name, values, downs));
                    }while (rs.next());
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

        @Override
        protected void onPostExecute(List<CustomGamemode> customGamemodes) {
            super.onPostExecute(customGamemodes);
            if (!exception)
                rv.setAdapter(new RecyclerAdapter(customGamemodes));
        }
    }
}
