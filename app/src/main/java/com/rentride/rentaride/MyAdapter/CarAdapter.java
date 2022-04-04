package com.rentride.rentaride.MyAdapter;

import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.rentride.rentaride.ItemclickListener;
import com.rentride.rentaride.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarAdapter extends RecyclerView.ViewHolder implements View.OnClickListener, Filterable {
    public ImageView carImg;
    public TextView txtName,txtPass,txtBag,txtDoor,txtTrans,txtPrice;
    public ItemclickListener listener;

    public CarAdapter(@NonNull View itemView) {
        super(itemView);

        carImg = itemView.findViewById(R.id.carImg);
        txtName = itemView.findViewById(R.id.carNm);
        txtPass = itemView.findViewById(R.id.txtPass);
        txtBag = itemView.findViewById(R.id.txtBag);
        txtDoor = itemView.findViewById(R.id.txtDoor);
        txtTrans = itemView.findViewById(R.id.txtAuto);
        txtPrice = itemView.findViewById(R.id.carCharges);
    }
    public void setItemClickListener(ItemclickListener itemClickListener){
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
