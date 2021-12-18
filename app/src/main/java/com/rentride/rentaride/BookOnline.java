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
    EditText PickUp,DropOff,DropTime,PickTime;
    TextInputLayout pickLoc,DropLoc,Phone;
    TextView car,carP;
    Button placeOrderBtn;
    DatePickerDialog.OnDateSetListener setListener,listener;
    int hr,min,hr1,min1;
    CountryCodePicker picker;
    private String carName = "";
    private String carCharges = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_online);

        carName = getIntent().getStringExtra("car Name");
        carCharges = getIntent().getStringExtra("car Charge");

        PickUp = findViewById(R.id.calD);
        car = findViewById(R.id.bookedcarName);
        carP = findViewById(R.id.rate);
        car.setText(carName);
        carP.setText(carCharges);
        PickTime = findViewById(R.id.Ptime);
        DropOff = findViewById(R.id.calDrop);
        DropTime = findViewById(R.id.TDrop);
        picker = findViewById(R.id.bookCode);

        pickLoc = findViewById(R.id.PickupLocation);
        DropLoc = findViewById(R.id.DropoffLocation);
        Phone = findViewById(R.id.phoneOrder);

        placeOrderBtn = findViewById(R.id.Order);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);



        PickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookOnline.this,setListener,year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = day + "/"+ month+"/"+year;
                PickUp.setText(date);
            }
        };
        DropOff.setOnClickListener(new View.OnClickListener() {
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
                String date = day + "/"+ month+"/"+year;
                DropOff.setText(date);
            }
        };

        PickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize timePicker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookOnline.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //initialize hour and minute
                        hr = hourOfDay;
                        min = minute;
                        //initialize calendar
                        Calendar calendar1 = Calendar.getInstance();
                        //set hour and minute
                        calendar1.set(0,0,0,hr,min);
                        //set selected time
                        PickTime.setText(DateFormat.format("hh:mm aa",calendar1));
                    }
                },12,0,false);
                //display previous selection
                timePickerDialog.updateTime(hr,min);
                //show dialog
                timePickerDialog.show();
            }
        });
        DropTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(BookOnline.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hr1 = hourOfDay;
                    min1 = minute;

                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.set(0,0,0,hr1,min1);
                    DropTime.setText(DateFormat.format("hh:mm aa",calendar2));

                }
            },12,0,false);
            timePickerDialog.updateTime(hr1,min1);
            timePickerDialog.show();
        });

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderNow();
            }
        });

    }

    private void OrderNow() {
        if (!validateDrop() | !validatePhone() | !validatePick()){
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
        ordersMap.put("pdate",PickUp.getText().toString());
        ordersMap.put("pname",carName);
        ordersMap.put("pcharge",carCharges);
        ordersMap.put("ptime",PickTime.getText().toString());
        ordersMap.put("ddate",DropOff.getText().toString());
        ordersMap.put("dtime",DropTime.getText().toString());
        ordersMap.put("plocation",pickLoc.getEditText().getText().toString());
        ordersMap.put("dlocation",DropLoc.getEditText().getText().toString());
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
    private Boolean validateDrop(){
        String sim = DropLoc.getEditText().getText().toString();
        if (sim.isEmpty()){
            DropLoc.setError("Enter drop-off location");
            return false;
        }else {
            DropLoc.setError(null);
            DropLoc.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePick(){
        String sim = pickLoc.getEditText().getText().toString();
        if (sim.isEmpty()){
            pickLoc.setError("Enter pick-up location");
            return false;
        }else {
            pickLoc.setError(null);
            pickLoc.setErrorEnabled(false);
            return true;
        }
    }

}