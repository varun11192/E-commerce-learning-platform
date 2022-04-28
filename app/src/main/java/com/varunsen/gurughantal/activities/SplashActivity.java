package com.varunsen.gurughantal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.varunsen.gurughantal.MainActivity;
import com.varunsen.gurughantal.R;

import org.w3c.dom.Text;

public class SplashActivity extends AppCompatActivity {

    private Animation topAnim, bottomAnim;
    private ImageView logoIv;
    private TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.slash_top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.slash_bottom_animation);

        logoIv = findViewById(R.id.logoIv);
        titleTv = findViewById(R.id.logoTv);

        logoIv.setAnimation(topAnim);
        titleTv.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //todo: check if user is logged in or not

                FirebaseAuth auth = FirebaseAuth.getInstance();

                if (auth.getCurrentUser() == null){

                    Intent mainIntent = new Intent(SplashActivity.this, AuthenticationActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                }
            }
        }, 2000);

    }
}