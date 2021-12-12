package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class Profile extends AppCompatActivity {
    CircleImageView profileImageView;
    EditText fullNameEdt,UserPhoneEdt,addressEdt;
    TextView profileChangeTxt,closeTxt,saveTxt;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference profilePicturestorageRefence;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePicturestorageRefence = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView = findViewById(R.id.settings_profile_image);
        fullNameEdt = findViewById(R.id.nameEDT);
        UserPhoneEdt = findViewById(R.id.PhoneEdt);
        profileChangeTxt = findViewById(R.id.profile_image_change);
        closeTxt = findViewById(R.id.close_settings_btn);
        saveTxt = findViewById(R.id.update_account_settings);

        closeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    userInfoSave();

                }
                else {
                    updateOnlyUserInfo();

                }
            }
        });
        profileChangeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                CropImage.activity(imageUri).setAspectRatio(1,1).start(Profile.this);

            }
        });

        userInfoDisplay(profileImageView,fullNameEdt,UserPhoneEdt,addressEdt);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        }else {
            Toast.makeText(this, "Try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Profile.this,Profile.class));
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEdt, final EditText userPhoneEdt, EditText addressEdt) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("image").exists()){
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEdt.setText(name);
                        userPhoneEdt.setText(phone);
                        addressEdt.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference refs = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("name",fullNameEdt.getText().toString());
        userMap.put("address",addressEdt.getText().toString());
        userMap.put("phoneOrder",UserPhoneEdt.getText().toString());
        refs.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);


        startActivity(new Intent(Profile.this,Home.class));
        Toast.makeText(Profile.this, "Profile Info Updated Successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoSave() {
        String phone = UserPhoneEdt.getText().toString();
        String uname = fullNameEdt.getText().toString();
        String addrs = addressEdt.getText().toString();
        ;
        if (phone.isEmpty()){
            Toast.makeText(this, "Enter phone", Toast.LENGTH_SHORT).show();

        }
        else if (uname.isEmpty()){
            Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
        }
        else if (addrs.isEmpty()){
            Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked")){
            uploadImage();
        }

    }

    private void uploadImage() {
        if (imageUri != null){
            final StorageReference fileRefs = profilePicturestorageRefence.child(Prevalent.CurrentOnlineUser.getPhone() + "jpg");

            uploadTask = fileRefs.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRefs.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference refs = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("name",fullNameEdt.getText().toString());
                        userMap.put("address",addressEdt.getText().toString());
                        userMap.put("phoneOrder",UserPhoneEdt.getText().toString());
                        userMap.put("image",myUrl);
                        refs.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);


                        startActivity(new Intent(Profile.this,Home.class));
                        Toast.makeText(Profile.this, "Profile Info Updated Successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(Profile.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Image not selected.", Toast.LENGTH_SHORT).show();
        }
    }

}