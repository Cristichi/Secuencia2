package com.cristichi.secuencia2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.cristichi.secuencia2.data.CustomGamemode;
import com.cristichi.secuencia2.data.Difficulty;
import com.cristichi.secuencia2.data.InfoCustomGamemode;
import com.cristichi.secuencia2.data.LoadCustomGamemode;
import com.cristichi.secuencia2.data.LoadInfoCustomGamemodes;
import com.cristichi.secuencia2.game.CustomGameActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentCustomGamemode extends FragmentGamemodes {

    private List<InfoCustomGamemode> infos;

    public FragmentCustomGamemode(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View sol = inflater.inflate(R.layout.fragment_custom_gamemodes, container, false);
        final RecyclerView rv = sol.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        final SearchView searchView = sol.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("CRISTICHIEX", "NEPEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                LoadInfoCustomGamemodes task = new LoadInfoCustomGamemodes((query.trim().equals("")?null:query)){
                    @Override
                    protected void onPostExecute(List<InfoCustomGamemode> infoCustomGamemodes) {
                        super.onPostExecute(infoCustomGamemodes);
                        rv.setAdapter(new RecyclerViewCustomGamemodeAdapter(inflater.getContext(), infoCustomGamemodes));
                    }
                };
                task.execute();
                try {
                    task.get(2, TimeUnit.SECONDS);
                }catch (Exception e){}
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    this.onQueryTextSubmit("");
                }else{
                    newText = newText.trim().toLowerCase();
                    List<InfoCustomGamemode> newInfos = new ArrayList<>(infos.size());
                    for(InfoCustomGamemode info : infos){
                        if (info.getName().toLowerCase().contains(newText) || info.getUserEmail().toLowerCase().contains(newText)){
                            newInfos.add(info);
                        }
                    }
                    rv.setAdapter(new RecyclerViewCustomGamemodeAdapter(inflater.getContext(), newInfos));
                }
                return true;
            }
        });
        searchView.setEnabled(false);
        LoadInfoCustomGamemodes task = new LoadInfoCustomGamemodes(null){
            @Override
            protected void onPostExecute(List<InfoCustomGamemode> infoCustomGamemodes) {
                super.onPostExecute(infoCustomGamemodes);
                infos = infoCustomGamemodes;
                rv.setAdapter(new RecyclerViewCustomGamemodeAdapter(inflater.getContext(), infoCustomGamemodes));
                searchView.setEnabled(true);
            }
        };
        task.execute();
        rv.setAdapter(new RecyclerViewCustomGamemodeAdapter(inflater.getContext(), new ArrayList<InfoCustomGamemode>()));
        return sol;
    }
}

class RecyclerViewCustomGamemodeAdapter extends RecyclerView.Adapter<CustomGamemodeHolders> {
    private List<InfoCustomGamemode> itemList;
    private Context context;

    public RecyclerViewCustomGamemodeAdapter(Context context, List<InfoCustomGamemode> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    private int cont = 0;
    @Override @NonNull
    public CustomGamemodeHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_gamemode_sp, parent, false);
        final CustomGamemodeHolders rcv = new CustomGamemodeHolders(layoutView);
        layoutView.setOnClickListener(new View.OnClickListener() {
            final int contado = cont++;
            @Override
            public void onClick(View v) {
                SoundSystem.playRecordedPop();
                final InfoCustomGamemode este = itemList.get(contado);

                String[] diffs = new String[Difficulty.values().length];
                for (int i=0; i<diffs.length; i++){
                    diffs[i] = context.getString(Difficulty.values()[i].getName());
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.pick_diff)+" "+este.getName());
                builder.setItems(diffs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        LoadCustomGamemode task = new LoadCustomGamemode(este.getId()){
                            AlertDialog alert;
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();

                                alert =  new AlertDialog.Builder(context)
                                        .setCancelable(false)
                                        .setTitle(context.getString(R.string.downloading_custom_gamemode))
                                        .show();
                                alert.setTitle(context.getString(R.string.downloading_custom_gamemode));
                            }

                            @Override
                            protected void onPostExecute(CustomGamemode customGamemode) {
                                super.onPostExecute(customGamemode);

                                alert.dismiss();
                                if (customGamemode==null){
                                    new AlertDialog.Builder(context)
                                            .setTitle(R.string.error_net)
                                            .setMessage("Gamemode null")
                                            .show();
                                } else {
                                    Intent intento = new Intent(context, CustomGameActivity.class);
                                    intento.putExtra("id", customGamemode.getId());
                                    intento.putExtra("email", customGamemode.getUserEmail());
                                    intento.putExtra("name", customGamemode.getName());
                                    intento.putExtra("values", customGamemode.getValuesString());
                                    intento.putExtra("difficulty", which);
                                    context.startActivity(intento);
                                }
                            }
                        };
                        task.execute();
                        try {
                            task.get(2, TimeUnit.SECONDS);
                        }catch (Exception e){}
                    }
                });
                builder.show();

            }
        });
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomGamemodeHolders holder, int position) {
        holder.tvName.setText(itemList.get(position).getName());
        holder.tvEmail.setText(itemList.get(position).getUserEmail());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}

class CustomGamemodeHolders extends RecyclerView.ViewHolder {

    TextView tvName;
    TextView tvEmail;

    CustomGamemodeHolders(View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tvName);
        tvEmail = itemView.findViewById(R.id.tvUser);
    }
}
