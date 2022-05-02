package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okio.Timeout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
    TextView phoneDigit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_o_t_p);

        pinView = findViewById(R.id.pinViewV);
        btnCode = findViewById(R.id.VerifyID);
        phoneDigit = findViewById(R.id.simCode);

        String phone = getIntent().getStringExtra("phone_number");
        String num = getIntent().getStringExtra("232");

        action = getIntent().getStringExtra("Action");
        phoneDigit.setText("  ..."+num);



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
                    storeBookingDetails();

                }
                else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(VerificationOTP.this, "Verification failed.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void storeBookingDetails(){
        String phone_number = getIntent().getStringExtra("phone_number");
        String car_name = getIntent().getStringExtra("name");
        String total_amount = getIntent().getStringExtra("total amount");
        String pickUpDate = getIntent().getStringExtra("pickUpDate");
        String returnDate = getIntent().getStringExtra("returnDate");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(VerificationOTP.this);

        final DatabaseReference OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(String.valueOf(signInAccount.getEmail()).replace(".","*")).child("Booked").child(date);
        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("car_name",car_name);
        ordersMap.put("total_amount",total_amount);
        ordersMap.put("pickUpDate",pickUpDate);
        ordersMap.put("returnDate",returnDate);
        ordersMap.put("phone_number",phone_number);
        ordersMap.put("date",date);
        ordersMap.put("time",time);
        OrdersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(VerificationOTP.this,Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();;
                }

            }
        });
    }
}