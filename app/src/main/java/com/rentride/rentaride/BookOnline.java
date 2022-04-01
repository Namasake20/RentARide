package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class BookOnline extends AppCompatActivity {
    TextInputLayout Phone,fromDate,toDate;
    EditText pickDate,dropDate;
    TextView car,carP;
    Button placeOrderBtn;
    ImageView carImage;
    DatePickerDialog.OnDateSetListener setListener,listener;
    CountryCodePicker picker;
    private String carName = "";
    private String carCharges = "";
    private String agentName = "";
    private String jina = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_online);

        carName = getIntent().getStringExtra("car Name");
        carCharges = getIntent().getStringExtra("car Charge");
        agentName = getIntent().getStringExtra("agent");
        jina = getIntent().getStringExtra("name");

        car = findViewById(R.id.bookedcarName);
        carP = findViewById(R.id.rate);
        car.setText(carName);
        carP.setText(carCharges);
        picker = findViewById(R.id.countryPickerb);
        pickDate = findViewById(R.id.DateEdt);
        dropDate = findViewById(R.id.returnDateEdt);

        fromDate = findViewById(R.id.startDateL);
        toDate = findViewById(R.id.returnDateL);
        Phone = findViewById(R.id.phoneOrderEdt);
        carImage =findViewById(R.id.carProductImage);

        placeOrderBtn = findViewById(R.id.bookNowBtn);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookOnline.this, android.R.style.Theme,setListener,year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/"+ month+"/"+year;
                pickDate.setText(date);
            }
        };
        dropDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookOnline.this,android.R.style.Theme,listener,year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/"+ month+"/"+year;
                dropDate.setText(date);
            }
        };

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderNow();
            }
        });


    }

    private void OrderNow() {
        if ( !validatePhone()){
            return;
        }
        else{
            confirmOrder();
        }

    }

    private void confirmOrder() {
        final String saveCurrentTime,saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(BookOnline.this);

        final DatabaseReference OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(String.valueOf(signInAccount.getEmail()).replace(".","*")).child("Booked").child(saveCurrentDate);
        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("car_name",jina);
        ordersMap.put("total_mount",carCharges);
        ordersMap.put("pickUpDate",fromDate.getEditText().getText().toString());
        ordersMap.put("returnDate",toDate.getEditText().getText().toString());
        ordersMap.put("phone_number","+"+picker.getFullNumber()+Phone.getEditText().getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        OrdersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Book List")
                            .child("Saved")
                            .child(String.valueOf(signInAccount.getEmail()).replace(".","*"))
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(BookOnline.this,Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });

    }

    private Boolean validatePhone(){
        String sim = Phone.getEditText().getText().toString();
        if (sim.isEmpty()){
            Phone.setError("Enter phone number");
            return false;
        }else {
            Phone.setError(null);
            Phone.setErrorEnabled(false);
            return true;
        }
    }

}