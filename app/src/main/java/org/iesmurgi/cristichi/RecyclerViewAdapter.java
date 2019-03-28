package org.iesmurgi.cristichi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.iesmurgi.cristichi.stylePacks.StylePack;

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
                StylePack este = itemList.get(contado);
                Toast.makeText(context, "HOLA "+context.getString(este.getName()), Toast.LENGTH_SHORT).show();
                Intent intento = new Intent(context, GameActivity.class);
                intento.putExtra("stylePack", este);
                context.startActivity(intento);
                if (context instanceof Activity){
                    ((Activity)context).finish();

                }
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
