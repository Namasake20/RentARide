package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CarDetail extends AppCompatActivity {
    ImageView carImage,textMsg,makeCall;
    CircleImageView dearProfile;
    TextView carName,carDescription,carPrice,agentName;
    Button bookOnline;
    String txt = "Hello";

    private String productID = "";
    String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        productID = getIntent().getStringExtra("pid");

        bookOnline = findViewById(R.id.pd_add_to_cart);
        carImage = findViewById(R.id.product_details_image);
        carName = findViewById(R.id.product_details_name);
        carDescription = findViewById(R.id.product_details_description);
        carPrice = findViewById(R.id.product_details_price);

        agentName = findViewById(R.id.agentName);

        dearProfile = findViewById(R.id.agentProfile);
        textMsg = findViewById(R.id.textMsg);
        makeCall = findViewById(R.id.makeCall);

        getCarDetails(productID);

        //adding zoom capability on the car image
        Zoomy.Builder builder = new Zoomy.Builder(this).target(carImage).animateZooming(false).enableImmersiveMode(false);
        builder.register();

        bookOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFleet();
            }
        });

    }

    private void addToFleet() {
        String saveCurrentTime,saveCurrentDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference bookList = FirebaseDatabase.getInstance().getReference().child("Book List");
        final HashMap<String,Object> CartMap = new HashMap<>();
        CartMap.put("pid",productID);
        CartMap.put("pname",carName.getText().toString());
        CartMap.put("price",carPrice.getText().toString());
        CartMap.put("agentUsername",agentName.getText().toString());
        CartMap.put("date",saveCurrentDate);
        CartMap.put("time",saveCurrentTime);

        bookList.child("User View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products").child(productID)
                .updateChildren(CartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            bookList.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products").child(productID)
                                    .updateChildren(CartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
//                                                Toast.makeText(CarDetail.this, "Added to Fleet successfully.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CarDetail.this,Home.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                        }

                    }
                });
    }

    private void getCarDetails(String productID) {
        DatabaseReference productRefs = FirebaseDatabase.getInstance().getReference().child("Products");

        productRefs.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Cars products = snapshot.getValue(Cars.class);

                    assert products != null;
                    carName.setText(products.getName());
                    carDescription.setText(products.getTransmission());
                    carPrice.setText( "$"+products.getPrice()+"/Day");
                    agentName.setText(products.getAgentUsername());
                    Picasso.get().load(products.getImg()).into(carImage);
                    number = products.getAgentPhone();
                    makeCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+number));
                            startActivity(callIntent);
                        }
                    });

                    textMsg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean installed = isAppInstalled("com.whatsapp");
                            if (installed){
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+products.getAgentPhone()+"&text="+txt));
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(CarDetail.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isAppInstalled(String s) {
        PackageManager packageManager = getPackageManager();
        boolean is_installed;
        try {
            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            is_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed = false;
            e.printStackTrace();
        }
        return is_installed;
    }

}