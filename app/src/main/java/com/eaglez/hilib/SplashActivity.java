package com.eaglez.hilib;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.ui.account.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_DURATION = 1000;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Core.initialize(getApplicationContext());
        builder = new AlertDialog.Builder(this);

        new Handler().postDelayed(() -> {
            try {
                Intent intent;

                if (Core.LoggedIn())
                    intent = new Intent(SplashActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                else
                    intent = new Intent(SplashActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.v("Splash", e.getMessage());
                builder.setTitle("Error")
                        .setCancelable(true)
                        .setMessage("It seems like the application can't be loaded. Please try again or contact administrator.")
                        .setPositiveButton("Continue", (dialog, which) -> finishAndRemoveTask()).show();
            }
        }, SPLASH_DISPLAY_DURATION);
    }
}