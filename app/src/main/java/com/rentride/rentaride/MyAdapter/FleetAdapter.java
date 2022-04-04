package com.rentride.rentaride.MyAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rentride.rentaride.ItemclickListener;
import com.rentride.rentaride.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FleetAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ItemclickListener itemclickListener;
    public TextView txtCarName,txtCarPrice,txtAgentName;

    public FleetAdapter(@NonNull View itemView) {
        super(itemView);
        txtCarName = itemView.findViewById(R.id.carNameF);
        txtCarPrice = itemView.findViewById(R.id.carPriceF);
        txtAgentName = itemView.findViewById(R.id.AgentNameF);
    }

    @Override
    public void onClick(View v) {
        itemclickListener.onClick(v,getAdapterPosition(),false);

    }
    public void setItemClickListener(ItemclickListener itemClickListener) {
        this.itemclickListener = itemClickListener;
    }
}
