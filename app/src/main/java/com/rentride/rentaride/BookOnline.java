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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class BookOnline extends AppCompatActivity {
    TextInputLayout Phone,DriverName,DriverMail,fromDate,toDate;
    EditText pickDate,dropDate;
    TextView car,carP;
    Button placeOrderBtn;
    ImageView carImage;
    DatePickerDialog.OnDateSetListener setListener,listener;
    CountryCodePicker picker;
    private String carName = "";
    private String carCharges = "";
    private String agentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_online);

        carName = getIntent().getStringExtra("car Name");
        carCharges = getIntent().getStringExtra("car Charge");
        agentName = getIntent().getStringExtra("agent");

        car = findViewById(R.id.bookedcarName);
        carP = findViewById(R.id.rate);
        car.setText(carName);
        carP.setText(carCharges);
         DriverName = findViewById(R.id.driverNameb);
        DriverMail = findViewById(R.id.driverMailb);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookOnline.this,listener,year,month,day);

                datePickerDialog.getWindow();
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
        if (!validateName() | !validatePhone() | !validateMail()){
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

        final DatabaseReference OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhone());
        HashMap<String,Object> ordersMap = new HashMap<>();
//        ordersMap.put("pdate",PickUp.getText().toString());
        ordersMap.put("car name",carName);
        ordersMap.put("Total Amount",carCharges);
        ordersMap.put("driverName",DriverName.getEditText().getText().toString());
        ordersMap.put("driverEmail",DriverMail.getEditText().getText().toString());
        ordersMap.put("From:",fromDate.getEditText().getText().toString());
        ordersMap.put("To:",toDate.getEditText().getText().toString());
        ordersMap.put("phone_number","+"+picker.getFullNumber()+Phone.getEditText().getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        OrdersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Book List")
                            .child("User View")
                            .child(Prevalent.CurrentOnlineUser.getPhone())
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
    private Boolean validateName(){
        String sim = DriverName.getEditText().getText().toString();
        if (sim.isEmpty()){
            DriverName.setError("Enter full name");
            return false;
        }else {
            DriverName.setError(null);
            DriverName.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateMail(){
        String sim = DriverMail.getEditText().getText().toString();
        if (sim.isEmpty()){
            DriverMail.setError("Enter email address");
            return false;
        }else {
            DriverMail.setError(null);
            DriverMail.setErrorEnabled(false);
            return true;
        }
    }

}