package com.cristichi.secuencia2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cristichi.secuencia2.data.Difficulty;
import com.cristichi.secuencia2.game.CharacterGameActivity;
import com.cristichi.secuencia2.game.ImageGameActivity;
import com.cristichi.secuencia2.game.WordGameActivity;
import com.cristichi.secuencia2.data.CharacterGamemode;
import com.cristichi.secuencia2.data.ImageGamemode;
import com.cristichi.secuencia2.data.Gamemode;
import com.cristichi.secuencia2.data.WordGamemode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentGamemodes extends Fragment {

    RecyclerView rv;
    RecyclerViewAdapter adapter;

    private ArrayList<Gamemode> packs;

    public FragmentGamemodes(){
        packs = new ArrayList<>();
    }

    public void setPacks(Gamemode... packs) {
        this.packs = new ArrayList<>(packs.length);
        this.packs.addAll(Arrays.asList(packs));
        if (adapter!=null)
            adapter.setItemList(this.packs);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View sol = inflater.inflate(R.layout.fragment_sp, container, false);
        rv = sol.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(getContext(), packs);
        rv.setAdapter(adapter);
        return sol;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("packs", packs);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            packs = (ArrayList<Gamemode>) savedInstanceState.getSerializable("packs");
            adapter.setItemList(packs);
        }catch (Exception e){
        }
    }
}

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<Gamemode> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<Gamemode> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    public void setItemList(List<Gamemode> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    private int cont = 0;
    @Override @NonNull
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp, parent, false);
        final RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        layoutView.setOnClickListener(new View.OnClickListener() {
            final int contado = cont++;
            @Override
            public void onClick(View v) {
                SoundSystem.playRecordedPop();
                final Gamemode este = itemList.get(contado);
                String[] diffs = new String[Difficulty.values().length];
                for (int i=0; i<diffs.length; i++){
                    diffs[i] = context.getString(Difficulty.values()[i].getName());
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.pick_diff)+" "+context.getString(este.getName()));
                builder.setItems(diffs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intento = null;
                        if (este instanceof CharacterGamemode){
                            intento = new Intent(context, CharacterGameActivity.class);
                        } else if (este instanceof WordGamemode){
                            intento = new Intent(context, WordGameActivity.class);
                        } else if (este instanceof ImageGamemode){
                            intento = new Intent(context, ImageGameActivity.class);
                        }
                        intento.putExtra("stylePack", contado);
                        intento.putExtra("difficulty", which);
                        context.startActivity(intento);
                        if (context instanceof Activity){
                            //((Activity)context).finish();
                        }
                    }
                });
                builder.show();

            }
        });
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders holder, int position) {
        holder.tvName.setText(itemList.get(position).getName());
        Log.d("CRISTICHIEX", itemList.get(position).getCode()+": "+itemList.get(position).getIcon());
        holder.ivPhoto.setImageResource(itemList.get(position).getIcon());
        holder.ivPhoto.setColorFilter(holder.ivPhoto.getContext().getResources().getColor(R.color.secondaryDarkColor));
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}

class RecyclerViewHolders extends RecyclerView.ViewHolder {

    ImageView ivPhoto;
    TextView tvName;

    RecyclerViewHolders(View itemView) {
        super(itemView);

        ivPhoto = itemView.findViewById(R.id.ivSPLogo);
        tvName = itemView.findViewById(R.id.tvName);
    }
}
