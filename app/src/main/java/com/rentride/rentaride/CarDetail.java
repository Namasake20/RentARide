package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CarDetail extends AppCompatActivity {
    ImageView carImage,textMsg,makeCall;
    CircleImageView dearProfile;
    TextView carName,carDescription,carPrice,agentName;
    Button bookOnline,bookNow;
    EditText startingDate,returningDate;
    String txt = "Hello";
    TextInputLayout Fname,Mail,PhoneN,startDate,returnDate;
    CountryCodePicker picker;

    DatePickerDialog.OnDateSetListener setListener,listener;


    private String productID = "";
    String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        productID = getIntent().getStringExtra("pid");

        bookOnline = findViewById(R.id.pd_add_to_cart);
        bookNow = findViewById(R.id.bookNow);
        carImage = findViewById(R.id.product_details_image);
        carName = findViewById(R.id.product_details_name);
        carDescription = findViewById(R.id.product_details_description);
        carPrice = findViewById(R.id.product_details_price);

        Fname = findViewById(R.id.driverName);
        Mail = findViewById(R.id.driverMail);
        PhoneN = findViewById(R.id.driverPhone);

        startDate = findViewById(R.id.startDateLt);
        returnDate = findViewById(R.id.dropDateL);
        startingDate = findViewById(R.id.startDateEdt);
        returningDate = findViewById(R.id.returnDateEdt);

        agentName = findViewById(R.id.agentName);
        picker = findViewById(R.id.countryPicker);

        dearProfile = findViewById(R.id.agentProfile);
        textMsg = findViewById(R.id.textMsg);
        makeCall = findViewById(R.id.makeCall);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

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

        startingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CarDetail.this, android.R.style.Theme_Holo,listener,year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });
        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/"+ month+"/"+year;
                startingDate.setText(date);

            }
        };

        returningDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CarDetail.this, android.R.style.Theme_Holo,setListener,year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/"+ month+"/"+year;
                returningDate.setText(date);
            }
        };

    }

    private void bookCarNow() {
        final String saveCurrentTime,saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhone());
        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("car name",carName);
        ordersMap.put("Total Amount",carPrice);
        ordersMap.put("driverName",Fname.getEditText().getText().toString());
        ordersMap.put("driverEmail",Mail.getEditText().getText().toString());
        ordersMap.put("From:",startDate.getEditText().getText().toString());
        ordersMap.put("To:",returnDate.getEditText().getText().toString());
        ordersMap.put("phone_number","+"+picker.getFullNumber()+ PhoneN.getEditText().getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        reference.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(CarDetail.this,Home.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CarDetail.this, "Error occurred", Toast.LENGTH_SHORT).show();

                }
            }


        });
    }
    private Boolean validatePhone(){
        String sim = PhoneN.getEditText().getText().toString();
        if (sim.isEmpty()){
            PhoneN.setError("Enter phone number");
            return false;
        }else {
            PhoneN.setError(null);
            PhoneN.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateName(){
        String val = Fname.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()){
            Fname.setError("Enter username");
            return false;
        }
        else if (val.length() >= 15){
            Fname.setError("Username too long");
            return false;
        }
        else if (!val.matches(noWhiteSpace)){
            Fname.setError("Whitespace not allowed.");
            return false;

        }
        else {
            Fname.setError(null);
            Fname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateEmail(){
        String mail = Mail.getEditText().getText().toString();
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (mail.isEmpty()){
            Mail.setError("Enter email");
            return false;
        }
        else if (!mail.matches(emailPattern)){
            Mail.setError("Invalid email address");
            return false;
        }
        else {
            Mail.setError(null);
            Mail.setErrorEnabled(false);
            return true;
        }
    }
    public void placeOrder(){
        if (!validatePhone() | !validateEmail() | !validateName()){
            return;
        }
        else
        {
            bookCarNow();
        }
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