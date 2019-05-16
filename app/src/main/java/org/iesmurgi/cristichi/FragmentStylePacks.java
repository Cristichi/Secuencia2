package org.iesmurgi.cristichi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.iesmurgi.cristichi.data.Difficulty;
import org.iesmurgi.cristichi.game.CharacterGameActivity;
import org.iesmurgi.cristichi.game.ImageGameActivity;
import org.iesmurgi.cristichi.game.WordGameActivity;
import org.iesmurgi.cristichi.data.CharacterStylePack;
import org.iesmurgi.cristichi.data.ImageStylePack;
import org.iesmurgi.cristichi.data.StylePack;
import org.iesmurgi.cristichi.data.WordStylePack;

import java.util.ArrayList;
import java.util.List;

public class FragmentStylePacks extends Fragment {

    private ArrayList<StylePack> packs;

    public FragmentStylePacks(){
        packs = new ArrayList<>();
    }

    public void setPacks(StylePack... packs) {
        this.packs = new ArrayList<>(packs.length);
        for (int i=0; i<packs.length; i++)
            this.packs.add(packs[i]);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View sol = inflater.inflate(R.layout.fragment_sp, container, false);
        RecyclerView rv = sol.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        rv.setAdapter(new RecyclerViewAdapter(inflater.getContext(), packs));
        return sol;
    }
}

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<StylePack> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<StylePack> itemList) {
        this.itemList = itemList;
        this.context = context;
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
                final StylePack este = itemList.get(contado);
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
                        if (este instanceof CharacterStylePack){
                            intento = new Intent(context, CharacterGameActivity.class);
                        } else if (este instanceof WordStylePack){
                            intento = new Intent(context, WordGameActivity.class);
                        } else if (este instanceof ImageStylePack){
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
        holder.ivPhoto.setImageResource(itemList.get(position).getIcon());
        holder.ivPhoto.setColorFilter(Color.RED);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}

class RecyclerViewHolders extends RecyclerView.ViewHolder {

    public ImageView ivPhoto;
    public TextView tvName;

    public RecyclerViewHolders(View itemView) {
        super(itemView);

        ivPhoto = itemView.findViewById(R.id.ivSPLogo);
        tvName = itemView.findViewById(R.id.tvSPName);
    }
}
