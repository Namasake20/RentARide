package com.rentride.rentaride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassword extends AppCompatActivity {
    TextInputLayout pass1,pass2;
    MaterialButton updatebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        pass1 = findViewById(R.id.ChangePassEdt1);
        pass2 = findViewById(R.id.ChangePassEdt2);
        updatebtn = findViewById(R.id.updatePass);

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

    }

    private void updatePassword() {
        String password = pass1.getEditText().getText().toString();
        String confirmPassword = pass2.getEditText().getText().toString();
        if (!validatePassword() | !validateConfirmPass()){
            return;

        }
        else {
            SubmitData(password,confirmPassword);
        }
    }

    private void SubmitData(String password, String confirmPassword) {
        String phoneNumber = getIntent().getStringExtra("phone_number");
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference("Users");
        RootRef.child(phoneNumber).child("password").setValue(password);
        startActivity(new Intent(ResetPassword.this,Login.class));
        finish();
    }

    private Boolean validatePassword(){
        String pass = pass1.getEditText().getText().toString();
        String valPass = "^" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";
        if (pass.isEmpty()){
            pass1.setError("Enter password");
            return false;
        }
        else if (!pass.matches(valPass)){
            pass1.setError("Invalid password");
            return false;

        }
        else {
            pass1.setError(null);
            pass1.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateConfirmPass(){
        String confirm = pass2.getEditText().getText().toString();
        if (confirm.isEmpty()){
            pass2.setError("Confirm password");
            return false;
        }
        else if (!confirm.matches(pass1.getEditText().getText().toString())){
            pass2.setError("password doesn't match.");
            return false;

        }
        else {
            pass2.setError(null);
            pass2.setErrorEnabled(false);
            return true;

        }
    }
}