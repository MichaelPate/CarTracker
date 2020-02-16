package com.example.pate5.cartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class activity_entry_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);
    }

    public void returnToHome(View v) {
        Intent openActivity = new Intent(activity_entry_list.this, activity_main.class);
        activity_entry_list.this.startActivity(openActivity);
    }
}
