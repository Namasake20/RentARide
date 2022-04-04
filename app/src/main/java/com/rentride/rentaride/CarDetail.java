package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.rentride.rentaride.MyModel.Cars;
import com.squareup.picasso.Picasso;

public class CarDetail extends AppCompatActivity {
    ImageView carImage,textMsg,makeCall;
    CircleImageView dearProfile;
    TextView carName,carDescription,carPrice,agentName;
    Button bookOnline,bookNow;
    EditText startingDate,returningDate;
    String txt = "Hello";
    TextInputLayout  PhoneN,startDate,returnDate;
    CountryCodePicker picker;

    DatePickerDialog.OnDateSetListener setListener,listener;


    private String productID = "";
    String number = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        productID = getIntent().getStringExtra("pid");

        bookOnline = findViewById(R.id.btnBookNow);
        carImage = findViewById(R.id.product_details_image);
        carName = findViewById(R.id.product_details_name);
        carDescription = findViewById(R.id.product_details_description);
        carPrice = findViewById(R.id.product_details_price);

        returningDate = findViewById(R.id.returnDateEdt);

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
                DatabaseReference bookingRefs = FirebaseDatabase.getInstance().getReference().child("Products");

                bookingRefs.child(productID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Cars products = snapshot.getValue(Cars.class);

                            assert products != null;
                            Intent intent = new Intent(CarDetail.this,BookOnline.class);
                            intent.putExtra("carName",products.getName());
                            intent.putExtra("car Charge",products.getPrice());
                            intent.putExtra("agent",products.getAgentUsername());
                            intent.putExtra("name",products.getName());
                            intent.putExtra("image",products.getImg());
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
                    carPrice.setText( "KES "+products.getPrice()+"/Day");
                    agentName.setText(products.getAgentUsername());
                    Picasso.get().load(products.getImg()).into(carImage);
                    number = products.getAgentPhone();
//                    makeCall.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent callIntent = new Intent(Intent.ACTION_CALL);
//                            callIntent.setData(Uri.parse("tel:"+number));
//                            startActivity(callIntent);
//                        }
//                    });

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
                                Toast.makeText(CarDetail.this, "Install WhatsApp", Toast.LENGTH_SHORT).show();
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