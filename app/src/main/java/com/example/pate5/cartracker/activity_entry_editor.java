package com.example.pate5.cartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class activity_entry_editor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_editor);
    }

    public void returnToHome(View v) {
        //TODO: Look for changes and ask before leaving, just like in the settings activity
        Intent openActivity = new Intent(activity_entry_editor.this, activity_main.class);
        activity_entry_editor.this.startActivity(openActivity);
    }

    public void saveAndDone(View v) {
        // TODO: Save the fields to the database

        Intent openActivity = new Intent(activity_entry_editor.this, activity_entry_list.class);
        activity_entry_editor.this.startActivity(openActivity);
    }
}
