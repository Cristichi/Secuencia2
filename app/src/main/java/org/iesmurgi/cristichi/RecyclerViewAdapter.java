package org.iesmurgi.cristichi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.iesmurgi.cristichi.game.CharacterGameActivity;
import org.iesmurgi.cristichi.game.ImageGameActivity;
import org.iesmurgi.cristichi.game.SpecialGameActivity;
import org.iesmurgi.cristichi.game.WordGameActivity;
import org.iesmurgi.cristichi.stylePacks.CharacterStylePack;
import org.iesmurgi.cristichi.stylePacks.ImageStylePack;
import org.iesmurgi.cristichi.stylePacks.SpecialStylePack;
import org.iesmurgi.cristichi.stylePacks.StylePack;
import org.iesmurgi.cristichi.stylePacks.WordStylePack;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
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
                        } else if (este instanceof SpecialStylePack){
                            intento = new Intent(context, SpecialGameActivity.class);
                        }
                        intento.putExtra("stylePack", este);
                        intento.putExtra("difficulty", which);
                        context.startActivity(intento);
                        if (context instanceof Activity){
                            ((Activity)context).finish();
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
