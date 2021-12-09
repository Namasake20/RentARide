package com.rentride.rentaride;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN = 5000;
    ImageView mImageView,copyR;
    TextView Rent;
    Animation tAnimation,bAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        //Animation
        tAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        mImageView = findViewById(R.id.splash_logo);
        copyR = findViewById(R.id.copr);
        Rent = findViewById(R.id.rent);

        mImageView.setAnimation(tAnimation);
        mImageView.startAnimation(tAnimation);
        Rent.setAnimation(bAnimation);
        copyR.setAnimation(bAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);


    }
}