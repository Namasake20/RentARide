package com.rentride.rentaride;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ItemclickListener itemclickListener;
    public TextView txtCarNameO,txtCarPriceO,txtPickDate,txtDropDate,txtPickPlace;
    public OrderAdapter(@NonNull View itemView) {
        super(itemView);

        txtCarNameO = itemView.findViewById(R.id.carNameO);
        txtCarPriceO = itemView.findViewById(R.id.TotalText);
        txtDropDate = itemView.findViewById(R.id.dateDr);
        txtPickDate = itemView.findViewById(R.id.dateO);
        txtPickPlace = itemView.findViewById(R.id.PickReturn);

    }

    @Override
    public void onClick(View v) {
        itemclickListener.onClick(v,getAdapterPosition(),false);

    }
    public void setItemClickListener(ItemclickListener itemClickListener) {
        this.itemclickListener = itemClickListener;
    }
}
