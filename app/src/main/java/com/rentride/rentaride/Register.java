package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    TextInputLayout emailEdt,Username,passwordEdt,confirm_password,PhoneNumber;
//    EditText Username,passwordEdt,confirm_password,PhoneNumber;
    TextView SignUpLogin;
    CountryCodePicker countryCodePicker;
    Button register;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SignUpLogin = findViewById(R.id.SignUpLogin);
        register = findViewById(R.id.register);
        Username = findViewById(R.id.username);
        emailEdt = findViewById(R.id.email);
        countryCodePicker = findViewById(R.id.RegCountryCode);
        passwordEdt = findViewById(R.id.password);
        PhoneNumber = findViewById(R.id.phonenumber);
        confirm_password = findViewById(R.id.confirmpassword);

        SignUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }
    private Boolean validateUsername(){
        String val = Username.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()){
            Username.setError("Enter username");
            return false;
        }
        else if (val.length() >= 15){
            Username.setError("Username too long");
            return false;
        }
        else if (!val.matches(noWhiteSpace)){
            Username.setError("Whitespace not allowed.");
            return false;

        }
        else {
            Username.setError(null);
            Username.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateEmail(){
        String mail = emailEdt.getEditText().getText().toString();
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (mail.isEmpty()){
            emailEdt.setError("Enter email");
            return false;
        }
        else if (!mail.matches(emailPattern)){
            emailEdt.setError("Invalid email address");
            return false;
        }
        else {
            emailEdt.setError(null);
            emailEdt.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePhone(){
        String sim = PhoneNumber.getEditText().getText().toString();
        if (sim.isEmpty()){
            PhoneNumber.setError("Enter phone number");
            return false;
        }else {
            PhoneNumber.setError(null);
            PhoneNumber.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword(){
        String pass = passwordEdt.getEditText().getText().toString();
        String valPass = "^" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";
        if (pass.isEmpty()){
            passwordEdt.setError("Enter password");
            return false;
        }
        else if (!pass.matches(valPass)){
            passwordEdt.setError("Invalid password");
            return false;

        }
        else {
            passwordEdt.setError(null);
            passwordEdt.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateConfirmPass(){
        String confirm = confirm_password.getEditText().getText().toString();
        if (confirm.isEmpty()){
            confirm_password.setError("Confirm password");
            return false;
        }
        else if (!confirm.matches(passwordEdt.getEditText().getText().toString())){
            confirm_password.setError("password doesn't match.");
            return false;

        }
        else {
            confirm_password.setError(null);
            confirm_password.setErrorEnabled(false);
            return true;

        }
    }

    private void CreateAccount() {
        String username = Username.getEditText().getText().toString();
        String email = emailEdt.getEditText().getText().toString();
        String Phone = "+"+countryCodePicker.getFullNumber()+PhoneNumber.getEditText().getText().toString();
        String password = passwordEdt.getEditText().getText().toString();

        if (!validateUsername() | !validateEmail()| !validatePhone() | !validatePassword() | !validateConfirmPass()){
            return;

        }
        else {
            ValidateData(username,email,password,Phone);
        }
    }

    private void ValidateData(String username, String email, String password,  String phone) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone",phone);
                    userDataMap.put("email",email);
                    userDataMap.put("password",password);
                    userDataMap.put("username",username);

                    RootRef.child("Users").child(phone).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
//                                Toast.makeText(Register.this, "Account Created Successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this,Login.class);
                                //TODO submit username,email,password and phone to VerificationOTP for verification before submission to db
//                                intent.putExtra("phone_number",phone);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(Register.this, "Something wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else {
                    PhoneNumber.setError(phone+" already exists.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
