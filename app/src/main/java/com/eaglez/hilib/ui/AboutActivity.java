package com.eaglez.hilib.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.eaglez.hilib.R;

public class AboutActivity extends AppCompatActivity {
    private ImageButton btnEmail, btnWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnEmail = findViewById(R.id.about_btn_email);
        btnWebsite = findViewById(R.id.about_btn_website);

        btnEmail.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, "hilib.hq@gmail.com");
            email.putExtra(Intent.EXTRA_SUBJECT, "General Inquiry");
            email.putExtra(Intent.EXTRA_TEXT, "Hello.");

            //need this to prompts email client only
            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        });

        btnWebsite.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://hi-lib.weebly.com/"))));
    }
}