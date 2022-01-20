package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okio.Timeout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VerificationOTP extends AppCompatActivity {

    PinView pinView;
    String generatedCode;
    MaterialButton btnCode;
    String action,phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_o_t_p);

        pinView = findViewById(R.id.pinViewV);
        btnCode = findViewById(R.id.VerifyID);

        String phone = getIntent().getStringExtra("phone_number");

        action = getIntent().getStringExtra("Action");
        phoneNumber = getIntent().getStringExtra("phone_number");


        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pinView.getText().toString();
                if (!code.isEmpty()){
                    verifyCode(code);
                }
            }
        });

        submitCode(phone);
    }

    private void submitCode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone,60, TimeUnit.SECONDS, this,mCallBacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            generatedCode = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                pinView.setText(code);
                verifyCode(code);
            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerificationOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
//            System.out.println(e.getMessage());
//            Log.d(e.getMessage(),"Error");

        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(generatedCode,code);
        SignUsingCode(phoneAuthCredential);
    }

    private void SignUsingCode(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //forgot password
                    if (action.equals("updatePass")){
                        updatePassword();
                    }
                    //register new user
                    else if (action.equals("register")){
                    storeNewUserData();
                    }

                }
                else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(VerificationOTP.this, "Verification failed.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void updatePassword() {
        Intent intent = new Intent(VerificationOTP.this,ResetPassword.class);
        intent.putExtra("phone_number",phoneNumber);
        startActivity(intent);
        finish();
    }
    public void storeNewUserData(){
        String phoneNo = getIntent().getStringExtra("phone_number");
        String mail = getIntent().getStringExtra("mail");
        String name = getIntent().getStringExtra("name");
        String pass = getIntent().getStringExtra("password");
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,Object> userDataMap = new HashMap<>();
                userDataMap.put("phone",phoneNo);
                userDataMap.put("email",mail);
                userDataMap.put("password",pass);
                userDataMap.put("username",name);

                RootRef.child("Users").child(phoneNo).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(VerificationOTP.this,Login.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(VerificationOTP.this, "Something wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}