package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgotPassword extends AppCompatActivity {
    TextInputLayout phoneNumber;
    MaterialButton Next;
    CountryCodePicker codePicker;

    String full_number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        phoneNumber = findViewById(R.id.PhoneNumberFg);
        Next = findViewById(R.id.forgotNext);
        codePicker = findViewById(R.id.countryCodePn);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneN = phoneNumber.getEditText().getText().toString();
                full_number = "+"+codePicker + phoneN;
                VerifyPhoneData();
            }
        });
    }

    private void VerifyPhoneData() {
        String phone = "+"+codePicker.getFullNumber()+phoneNumber.getEditText().getText().toString();
        if (!validatePhone()){
            return;
        }else {
            final DatabaseReference reference;
             reference = FirebaseDatabase.getInstance().getReference();
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("Users").child(phone).exists()){
                        Intent intent = new Intent(ForgotPassword.this,ResetPassword.class);
                        intent.putExtra("phone_number",phone);
                        startActivity(intent);
                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);
                        finish();
                    }
                    else{
                        phoneNumber.setError("User does not exist");
                        phoneNumber.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private Boolean validatePhone(){
        String sim = phoneNumber.getEditText().getText().toString();
        if (sim.isEmpty()){
            phoneNumber.setError("Enter phone number");
            return false;
        }else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }
}