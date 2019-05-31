package org.iesmurgi.cristichi;

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
import android.widget.TextView;

import org.iesmurgi.cristichi.data.CustomGamemode;
import org.iesmurgi.cristichi.ddbb.DDBBConstraints;
import org.iesmurgi.cristichi.ddbb.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MyCustomGamemodesActivity extends AppCompatActivity {

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
            }
        });

        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        LoadMyCustomGamemode task = new LoadMyCustomGamemode(rv, Session.getUser().email);
        task.execute();
    }

    private static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDownloads;

        RecyclerViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDownloads = itemView.findViewById(R.id.tvDownloads);
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
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            CustomGamemode gm = gamemodes.get(position);
            holder.tvName.setText(gm.getName());
            holder.tvDownloads.setText(gm.getDownloads()+"");
        }

        @Override
        public int getItemCount() {
            return gamemodes.size();
        }

        public void clear(){
            gamemodes.clear();
            notifyDataSetChanged();
        }
    }

    private class LoadMyCustomGamemode extends AsyncTask<Void, Void, List<CustomGamemode>> {
        private boolean exception;

        private String userEmail;
        private RecyclerView rv;

        public LoadMyCustomGamemode(RecyclerView rv, String userEmail){
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
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    String values = rs.getString(3);
                    int downs = rs.getInt(4);
                    sol.add(new CustomGamemode(id, userEmail, name, values, downs));
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
            rv.setAdapter(new RecyclerAdapter(customGamemodes));
        }
    }
}
