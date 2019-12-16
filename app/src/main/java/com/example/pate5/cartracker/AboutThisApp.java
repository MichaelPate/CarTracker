package com.example.pate5.cartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutThisApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_this_app);
        getSupportActionBar().setTitle("About This App");

        // Version information was added to the intent using intent.putExtra()
        TextView versionBar = findViewById(R.id.versionView);
        versionBar.setText(getIntent().getStringExtra("Version"));
    }

    public void returnToHome(View v) {
        Intent openActivity = new Intent(AboutThisApp.this, MainActivity.class);
        AboutThisApp.this.startActivity(openActivity);
    }
}