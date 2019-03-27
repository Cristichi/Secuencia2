package org.iesmurgi.cristichi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.iesmurgi.cristichi.stylePacks.StylePack;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<StylePack> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<StylePack> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override @NonNull
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp, parent, false);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders holder, int position) {
        holder.tvName.setText(itemList.get(position).getName());
        holder.ivPhoto.setImageResource(itemList.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
