package com.rentride.rentaride;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView carImg;
    public TextView txtName,txtDescription,txtPrice;
    public ItemclickListener listener;

    public CarAdapter(@NonNull View itemView) {
        super(itemView);

        carImg = itemView.findViewById(R.id.carImg);
        txtName = itemView.findViewById(R.id.carNm);
        txtDescription = itemView.findViewById(R.id.carDescrp);
        txtPrice = itemView.findViewById(R.id.carCharges);
    }
    public void setItemClickListener(ItemclickListener itemClickListener){
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
}
