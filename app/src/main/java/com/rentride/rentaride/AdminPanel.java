package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminPanel extends AppCompatActivity {
    TextInputLayout CarName,carDescription,carCharge,AgentPhone,agentUsername;
    Button AddCar;
    ImageView carImage,profilePic;
    Uri imageUri,profileUri;
    String nameCar,DescCar,chargeCar,saveDate,saveTime,PhoneAgent,UserName;
    private static final int galleryPick = 1;
    private static final int profilePick = 1;
    private String productRandomKey,downloadImageUrl;
    private StorageReference productImageRef;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        productImageRef = FirebaseStorage.getInstance().getReference().child("Product_Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        CarName = findViewById(R.id.CarName);
        carDescription = findViewById(R.id.Description);
        carCharge = findViewById(R.id.CarFee);
        AddCar = findViewById(R.id.addCar);
        carImage = findViewById(R.id.carImage);
        profilePic = findViewById(R.id.agentImage);
        AgentPhone = findViewById(R.id.AgentPhone);
        agentUsername = findViewById(R.id.agentUsername);

        carImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileImage();
            }
        });
        AddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void openProfileImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,galleryPick);
    }

    private void validateData() {
        if (!validateName() | !validateDescription() | !validatePrice() | !validateImage() | !validateAgentPhone() | !validateAgentUsername()){
            return;

        }else {
            submitData();

        }
    }

    private void submitData() {
        nameCar = CarName.getEditText().getText().toString();
        DescCar = carDescription.getEditText().getText().toString();
        chargeCar = carCharge.getEditText().getText().toString();
        PhoneAgent = AgentPhone.getEditText().getText().toString();
        UserName = agentUsername.getEditText().getText().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");
        saveDate = dateFormat.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveTime = currentTime.format(calendar.getTime());
        productRandomKey = saveDate + saveTime;

        StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpeg");

        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminPanel.this, "Error: "+message, Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminPanel.this, "Product Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();

                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminPanel.this, "got Product image Url successfully.", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
//        StorageReference profilePath = productImageRef.child(profileUri.getLastPathSegment() + productRandomKey + ".jpeg");
//        final UploadTask uploadTask1 = filePath.putFile(profileUri);
//        uploadTask1.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                String message = e.toString();
//                Toast.makeText(AdminPanel.this, message, Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AdminPanel.this, "Agent Image Uploaded successfully.", Toast.LENGTH_SHORT).show();
//                Task<Uri> uriTask = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                        if (!task.isSuccessful()){
//                            throw task.getException();
//                        }
//                        downloadImageUrl = profilePath.getDownloadUrl().toString();
//                        return profilePath.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()){
//                            downloadImageUrl = task.getResult().toString();
//                            Toast.makeText(AdminPanel.this, "Profile Url obtained.", Toast.LENGTH_SHORT).show();
//                            saveProductInfoToDatabase();
//                        }
//                    }
//                });
//
//            }
//        });

    }

    private void saveProductInfoToDatabase() {
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveDate);
        productMap.put("time",saveTime);
        productMap.put("description",DescCar);
        productMap.put("img",downloadImageUrl);
        productMap.put("price",chargeCar);
        productMap.put("name",nameCar);
        productMap.put("agentPhone",PhoneAgent);
        productMap.put("agentUsername",UserName);

        productsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Intent intent = new Intent(AdminPanel.this,Login.class);
                    startActivity(intent);
                    Toast.makeText(AdminPanel.this, "Product added successfully.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String message = task.getException().toString();
                    Toast.makeText(AdminPanel.this, "Error: "+message, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            carImage.setImageURI(imageUri);
        }
//        else if (requestCode == profilePick && requestCode == RESULT_OK && data != null){
//            profileUri = data.getData();
//            profilePic.setImageURI(profileUri);
//        }
    }
    private Boolean validateImage(){
        if (imageUri == null){
            Toast.makeText(this, "Image is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }
    private Boolean validateName(){
        String pass = CarName.getEditText().getText().toString();
        if (pass.isEmpty()){
            CarName.setError("Enter car name");
            return false;
        }

        else {
            CarName.setError(null);
            CarName.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateDescription(){
        String desc = carDescription.getEditText().getText().toString();
        if (desc.isEmpty()){
            carDescription.setError("Enter car description");
            return false;
        }

        else {
            carDescription.setError(null);
            carDescription.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePrice(){
        String price = carCharge.getEditText().getText().toString();
        if (price.isEmpty()){
            carCharge.setError("Enter charges per hour");
            return false;
        }

        else {
            carCharge.setError(null);
            carCharge.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean ProfileImage(){
        if (profileUri == null){
            Toast.makeText(this, "Profile Image is mandatory", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }

    }
    private Boolean validateAgentPhone(){
        String phone = AgentPhone.getEditText().getText().toString();
        if (phone.isEmpty()){
            AgentPhone.setError("Enter agent phone number");
            return false;
        }

        else {
            AgentPhone.setError(null);
            AgentPhone.setErrorEnabled(false);
            return true;
        }

    }
    private Boolean validateAgentUsername(){
        String username = agentUsername.getEditText().getText().toString();
        if (username.isEmpty()){
            agentUsername.setError("Enter agent username");
            return false;
        }

        else {
            agentUsername.setError(null);
            agentUsername.setErrorEnabled(false);
            return true;
        }

    }

}