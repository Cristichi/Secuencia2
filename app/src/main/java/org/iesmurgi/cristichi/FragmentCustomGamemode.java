package org.iesmurgi.cristichi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.iesmurgi.cristichi.data.CustomGamemode;
import org.iesmurgi.cristichi.data.Difficulty;
import org.iesmurgi.cristichi.data.InfoCustomGamemode;
import org.iesmurgi.cristichi.data.LoadCustomGamemode;
import org.iesmurgi.cristichi.data.LoadInfoCustomGamemodes;
import org.iesmurgi.cristichi.game.CustomGameActivity;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentCustomGamemode extends FragmentGamemodes {


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

        final Button btnReload = sol.findViewById(R.id.btnDownload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                LoadInfoCustomGamemodes task = new LoadInfoCustomGamemodes(){
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
                v.setEnabled(true);
            }
        });
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
                                        .setTitle(context.getString(R.string.pick_diff)+" "+este.getName())
                                        .show();
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
