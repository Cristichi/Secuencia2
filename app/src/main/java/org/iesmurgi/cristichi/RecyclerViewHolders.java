package org.iesmurgi.cristichi;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewHolders extends RecyclerView.ViewHolder {

    public ImageView ivPhoto;
    public TextView tvName;

    public RecyclerViewHolders(View itemView) {
        super(itemView);

        ivPhoto = itemView.findViewById(R.id.ivSPLogo);
        tvName = itemView.findViewById(R.id.tvSPName);
    }
}
