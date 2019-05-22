package org.iesmurgi.cristichi;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.iesmurgi.cristichi.data.Difficulty;
import org.iesmurgi.cristichi.data.StylePackFinder;
import org.iesmurgi.cristichi.ddbb.DDBBConstraints;
import org.iesmurgi.cristichi.ddbb.Session;
import org.iesmurgi.cristichi.ddbb.User;
import org.iesmurgi.cristichi.ddbb.LocalStorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountActivity extends ActivityWithMusic {

    private TextView tvNick;
    private TextView tvEmail;

    private Button btnLogout;
    private ImageButton btnEdit;

    private RecyclerView rvHighScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (!Session.isLogged()){
            finish();
            return;
        }

        tvNick = findViewById(R.id.tvNickname);
        tvEmail = findViewById(R.id.tvEmail);

        btnLogout = findViewById(R.id.btnLogout);
        btnEdit = findViewById(R.id.btnEdit);

        rvHighScores = findViewById(R.id.rvAccountHighScores);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundSystem.playCartoonPunch();
                if (LocalStorage.logout(AccountActivity.this))
                    finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundSystem.playCartoonSlipUp();
                Intent intent = new Intent(AccountActivity.this, EditAccountActivity.class);
                startActivity(intent);
            }
        });

        LoadHighScoresTask task = new LoadHighScoresTask();
        task.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();

        User user = Session.getUser();
        tvNick.setText(user.nick);
        tvEmail.setText(user.email);
    }

    private class HighScore{
        int gamemode;
        int diff;
        String score;
        String date;

        private HighScore(int gamemode, int diff, int score, Date date){
            this.gamemode = gamemode;
            this.diff = diff;
            this.score = score+"";
            this.date = android.text.format.DateFormat.format("dd/MM/yyyy", date).toString();
        }
    }

    private static class RecyclerViewHolders extends RecyclerView.ViewHolder {
        TextView tvGamemode;
        TextView tvDifficulty;
        TextView tvScore;
        TextView tvDate;

        RecyclerViewHolders(View itemView, int textColor) {
            super(itemView);

            tvGamemode = itemView.findViewById(R.id.tvGamemode);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvDate = itemView.findViewById(R.id.tvDate);

            tvGamemode.setTextColor(textColor);
            tvDifficulty.setTextColor(textColor);
            tvScore.setTextColor(textColor);
            tvDate.setTextColor(textColor);
        }
    }

    private class RVHighScoresAdapter extends RecyclerView.Adapter<RecyclerViewHolders>{

        List<HighScore> highScores;
        int textColor;

        RVHighScoresAdapter(List<HighScore> highScores, int textColor){
            this.highScores = highScores;
            this.textColor = textColor;
        }

        @NonNull
        @Override
        public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_account_highscore, viewGroup, false);
            return new RecyclerViewHolders(layoutView, textColor);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolders holder, int position) {
            HighScore hs = highScores.get(position);
            holder.tvGamemode.setText(hs.gamemode);
            holder.tvDifficulty.setText(hs.diff);
            holder.tvScore.setText(hs.score);
            holder.tvDate.setText(hs.date);
        }

        @Override
        public int getItemCount() {
            return highScores.size();
        }
    }

    private class LoadHighScoresTask extends AsyncTask<Void, Void, List<HighScore>>{
        private TextView tvOutput;
        private boolean exception;
        int textColor = ResourcesCompat.getColor(getResources(), R.color.secondaryTextColor, getTheme());

        private LoadHighScoresTask(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvOutput = findViewById(R.id.tvOutput);
            tvOutput.setText(R.string.account_highscores_loading);
            tvOutput.setTextColor(textColor);
        }

        @Override
        protected List<HighScore> doInBackground(Void... voids) {
            List<HighScore> sol = new ArrayList<>();
            User user = Session.getUser();
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                DriverManager.setLoginTimeout(2);
                con = DriverManager.getConnection(DDBBConstraints.URL_DDBB, DDBBConstraints.USER, DDBBConstraints.PASSWORD);

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT Gamemode, Difficulty, Score, ScoreDate from HighScores where " +
                        "UserEmail='"+user.email+"' order by Difficulty, Gamemode");

                while (rs.next()) {
                    String gamemode = rs.getString(1);
                    int difficulty = rs.getInt(2);
                    int score = rs.getInt(3);
                    Date scoredate = rs.getDate(4);

                    int gm;
                    try{
                        gm = StylePackFinder.byCode(gamemode).getName();
                    }catch (IllegalArgumentException e){
                        gm = R.string.highscores_unknown_gamemode;
                    }

                    int df;
                    try{
                        df = Difficulty.getById(difficulty).getName();
                    }catch (IllegalArgumentException e){
                        df = R.string.highscores_unknown_difficulty;
                    }
                    sol.add(new HighScore(gm, df, score, scoredate));
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
        protected void onPostExecute(List<HighScore> list) {
            super.onPostExecute(list);
            if (exception){
                tvOutput.setText(R.string.error_net);
            } else if (list.isEmpty()){
                tvOutput.setText(R.string.account_highscores_error_empty);
            } else {
                tvOutput.setText("");
                rvHighScores.setAdapter(new RVHighScoresAdapter(list, textColor));
                rvHighScores.setLayoutManager(new LinearLayoutManager(AccountActivity.this));
            }
        }
    }

}

