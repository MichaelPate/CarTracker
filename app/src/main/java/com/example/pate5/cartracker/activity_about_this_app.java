package com.example.pate5.cartracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class activity_about_this_app extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_this_app);
        Objects.requireNonNull(getSupportActionBar()).setTitle("About This App");

        // Version information was added to the intent using intent.putExtra()
        TextView versionBar = findViewById(R.id.versionView);
        versionBar.setText(getIntent().getStringExtra("Version"));
    }

    public void returnToHome(View v) {
        Intent openActivity = new Intent(activity_about_this_app.this, activity_main.class);
        activity_about_this_app.this.startActivity(openActivity);
    }
}
