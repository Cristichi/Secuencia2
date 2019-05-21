package org.iesmurgi.cristichi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.iesmurgi.cristichi.data.Difficulty;
import org.iesmurgi.cristichi.data.StylePackFinder;
import org.iesmurgi.cristichi.ddbb.DDBBConstraints;
import org.iesmurgi.cristichi.ddbb.Session;
import org.iesmurgi.cristichi.ddbb.User;
import org.iesmurgi.cristichi.data.CharacterStylePack;
import org.iesmurgi.cristichi.data.ImageStylePack;
import org.iesmurgi.cristichi.data.StylePack;
import org.iesmurgi.cristichi.data.WordStylePack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HighscoresActivity extends AppCompatActivity {

    private Spinner spnGamemode;
    private Spinner spnDifficulty;
    private Button btnFilter;
    private RecyclerView rvHighscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        spnGamemode = findViewById(R.id.spnGamemode);
        spnDifficulty = findViewById(R.id.spnDifficulty);
        btnFilter = findViewById(R.id.btnFilter);
        rvHighscores = findViewById(R.id.rvHighScores);

        LoadHighScoresTask task = new LoadHighScoresTask(null, Difficulty.MEDIUM.getId());
        task.execute();

        ArrayList<SpinnerGamemodeItem> stylePacksNames = new ArrayList<>(15);
        stylePacksNames.add(new SpinnerGamemodeItem(null, getString(R.string.highscores_filter_all)));
        StylePack[] sps = CharacterStylePack.values();
        for (StylePack sp : sps){
            stylePacksNames.add(new SpinnerGamemodeItem(sp, getString(sp.getName())));
        }
        sps = ImageStylePack.values();
        for (StylePack isp : sps){
            stylePacksNames.add(new SpinnerGamemodeItem(isp, getString(isp.getName())));
        }
        sps = WordStylePack.values();
        for (StylePack wsp : sps){
            stylePacksNames.add(new SpinnerGamemodeItem(wsp, getString(wsp.getName())));
        }
        spnGamemode.setAdapter(new SpinnerGamemodeAdapter(this, stylePacksNames));

        ArrayList<SpinnerDifficultyItem> diffNames = new ArrayList<>();
        diffNames.add(new SpinnerDifficultyItem(null, getString(R.string.highscores_filter_all)));
        Difficulty[] diffs = Difficulty.values();
        for (Difficulty diff : diffs){
            diffNames.add(new SpinnerDifficultyItem(diff, getString(diff.getName())));
        }
        spnDifficulty.setAdapter(new SpinnerDifficultyAdapter(this, diffNames));
        spnDifficulty.setSelection(Difficulty.MEDIUM.ordinal()+1);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerGamemodeItem sgi = ((SpinnerGamemodeItem)spnGamemode.getSelectedItem());
                SpinnerDifficultyItem sdi = ((SpinnerDifficultyItem)spnDifficulty.getSelectedItem());
                StylePack gm = sgi.gamemode;
                Difficulty dif = sdi.difficulty;
                LoadHighScoresTask task = new LoadHighScoresTask(
                        (gm==null?null:gm.getCode()), (dif==null?null:dif.getId())
                );
                task.execute();
            }
        });
    }

    private class HighScore{
        String user;
        int gamemode;
        int diff;
        String score;
        String date;

        private HighScore(String user, int gamemode, int diff, int score, Date date){
            this.user = user;
            this.gamemode = gamemode;
            this.diff = diff;
            //this.score = String.format(Locale.getDefault(), "%.3f", score);
            this.score = score+"";
            this.date = android.text.format.DateFormat.format("dd/MM/yyyy", date).toString();
        }
    }

    private static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser;
        TextView tvGamemode;
        TextView tvDifficulty;
        TextView tvScore;
        TextView tvDate;

        RecyclerViewHolder(View itemView, int textColor) {
            super(itemView);

            tvUser = itemView.findViewById(R.id.tvEmail);
            tvGamemode = itemView.findViewById(R.id.tvGamemode);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvDate = itemView.findViewById(R.id.tvDate);

            tvUser.setTextColor(textColor);
            tvGamemode.setTextColor(textColor);
            tvDifficulty.setTextColor(textColor);
            tvScore.setTextColor(textColor);
            tvDate.setTextColor(textColor);
        }
    }

    private class RVHighScoresAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

        List<HighScore> highScores;
        int textColor;

        RVHighScoresAdapter(List<HighScore> highScores, int textColor){
            this.highScores = highScores;
            this.textColor = textColor;
        }

        public void setValues(List<HighScore> highScores, int textColor){
            this.highScores = highScores;
            this.textColor = textColor;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_highscore, viewGroup, false);
            return new RecyclerViewHolder(layoutView, textColor);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            HighScore hs = highScores.get(position);
            holder.tvUser.setText(hs.user);
            holder.tvGamemode.setText(hs.gamemode);
            holder.tvDifficulty.setText(hs.diff);
            holder.tvScore.setText(hs.score);
            holder.tvDate.setText(hs.date);
        }

        @Override
        public int getItemCount() {
            return highScores.size();
        }

        public void clear(){
            highScores.clear();
            notifyDataSetChanged();
        }
    }

    RVHighScoresAdapter rvAdapter;
    private class LoadHighScoresTask extends AsyncTask<Void, Void, List<HighScore>> {
        private TextView tvOutput;
        private boolean exception;
        private int textColor = ResourcesCompat.getColor(getResources(), R.color.secondaryTextColor, getTheme());

        private String gamemode;
        private Integer difficulty;

        private LoadHighScoresTask(@Nullable String gamemode, @Nullable Integer difficulty){
            this.gamemode = gamemode;
            this.difficulty = difficulty;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnFilter.setEnabled(false);
            tvOutput = findViewById(R.id.tvOutput);
            tvOutput.setText(R.string.highscores_loading);
            tvOutput.setTextColor(textColor);
            if (rvAdapter!=null){
                rvAdapter.clear();
            }
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
                ResultSet rs = st.executeQuery(
                        "SELECT Nickname, Gamemode, Difficulty, Score, ScoreDate from HighScores, Users " +
                                "where UserEmail='"+user.email+"' and Users.Email=HighScores.UserEmail " +
                                (gamemode!=null?"and Gamemode='"+gamemode+"' ":"") +
                                (difficulty!=null?"and Difficulty='"+difficulty+"' ":"") +
                                "order by Score desc limit 20");
                Log.d("CRISTICHIEX", "SELECT Nickname, Gamemode, Difficulty, Score, ScoreDate from HighScores, Users " +
                                "where UserEmail='"+user.email+"' and Users.Email=HighScores.UserEmail " +
                                (gamemode!=null?"and Gamemode='"+gamemode+"' ":"") +
                                (difficulty!=null?"and Difficulty='"+difficulty+"' ":"") +
                                "order by Score desc limit 20");

                while (rs.next()) {
                    String nickname = rs.getString(1);
                    String gamemode = rs.getString(2);
                    int difficulty = rs.getInt(3);
                    int score = rs.getInt(4);
                    Date scoredate = rs.getDate(5);

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
                    sol.add(new HighScore(nickname, gm, df, score, scoredate));
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
            btnFilter.setEnabled(true);
            if (exception){
                tvOutput.setText(R.string.error_net);
            } else if (list.isEmpty()){
                tvOutput.setText(R.string.highscores_error_empty);
            } else {
                tvOutput.setText("");
                if (rvAdapter==null)
                    rvAdapter = new RVHighScoresAdapter(list, textColor);
                else {
                    rvAdapter.setValues(list, textColor);
                }
                rvHighscores.setAdapter(rvAdapter);
                rvHighscores.setLayoutManager(new LinearLayoutManager(HighscoresActivity.this));
            }
        }
    }
}

class SpinnerGamemodeItem {
    @Nullable
    public StylePack gamemode;
    public String name;

    SpinnerGamemodeItem(@Nullable StylePack gamemode, String name){
        this.gamemode = gamemode;
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

class SpinnerDifficultyItem {
    @Nullable
    public Difficulty difficulty;
    public String name;

    SpinnerDifficultyItem(@Nullable Difficulty difficulty, String name){
        this.difficulty = difficulty;
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

class SpinnerGamemodeAdapter extends ArrayAdapter<SpinnerGamemodeItem> {

    public SpinnerGamemodeAdapter(Activity context, List<SpinnerGamemodeItem> list){
        super(context, android.R.layout.simple_spinner_item, list);
    }

    /* *
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }
    /* */

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(position, convertView, parent);
    }

    private View rowview(int position, View convertView, ViewGroup parent){

        String rowItem = getItem(position).name;

        viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {

            holder = new viewHolder();
            LayoutInflater flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.item_text_spinner, parent, false);

            holder.tvText = rowview.findViewById(R.id.tvText);
            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }
        holder.tvText.setText(rowItem);

        return rowview;
    }

    private class viewHolder{
        TextView tvText;
    }
}

class SpinnerDifficultyAdapter extends ArrayAdapter<SpinnerDifficultyItem> {

    public SpinnerDifficultyAdapter(Activity context, List<SpinnerDifficultyItem> list){
        super(context, android.R.layout.simple_spinner_item, list);
    }

    /* *
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }
    /* */

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(position, convertView, parent);
    }

    private View rowview(int position, View convertView, ViewGroup parent){

        String rowItem = getItem(position).name;

        viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {

            holder = new viewHolder();
            LayoutInflater flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.item_text_spinner, parent, false);

            holder.tvText = rowview.findViewById(R.id.tvText);
            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }
        holder.tvText.setText(rowItem);

        return rowview;
    }

    private class viewHolder{
        TextView tvText;
    }
}