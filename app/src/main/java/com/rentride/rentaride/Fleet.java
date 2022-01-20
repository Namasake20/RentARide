package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Fleet extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet);

        recyclerView = findViewById(R.id.fleet_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference bookList = FirebaseDatabase.getInstance().getReference().child("Book List");

        FirebaseRecyclerOptions<FleetModel> options = new FirebaseRecyclerOptions.Builder<FleetModel>()
                .setQuery(bookList.child("User View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products"), FleetModel.class).build();

        FirebaseRecyclerAdapter<FleetModel, FleetAdapter> adapter = new FirebaseRecyclerAdapter<FleetModel, FleetAdapter>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FleetAdapter fleetAdapter, int i, @NonNull FleetModel fleetModel) {
                fleetAdapter.txtAgentName.setText("Dealer Agent: "+fleetModel.getAgentUsername());
                fleetAdapter.txtCarPrice.setText(fleetModel.getPrice());
                fleetAdapter.txtCarName.setText(fleetModel.getPname());

                fleetAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence charSequence[] = new CharSequence[]{
                                "Confirm Order",
                                "Delete"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(Fleet.this);
                        builder.setTitle("Options:");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0){
                                    //TODO add bookList.child() interface to confirm a single order of car 
                                    Intent intent = new Intent(Fleet.this,BookOnline.class);
                                    intent.putExtra("car Name",fleetModel.getPname());
                                    intent.putExtra("car Charge",fleetModel.getPrice());
                                    startActivity(intent);
                                }
                                else if (i == 1){
                                    bookList.child("User View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products")
                                            .child(fleetModel.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                      Intent intent = new Intent(Fleet.this,Home.class);
                                                      startActivity(intent);
                                                    }
                                                }
                                            });
                                }

                            }
                        });
                        builder.show();
                    }
                });
            }
            @NonNull
            @Override
            public FleetAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fleet_layout,parent,false);
                FleetAdapter holder = new FleetAdapter(view);
                return holder;
            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

}
