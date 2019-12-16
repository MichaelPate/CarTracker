package com.example.pate5.cartracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AppSettings extends AppCompatActivity {

    private EntryDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        getSupportActionBar().setTitle("Settings");

        db = new EntryDatabase(this);

        try {
            String opt = db.getInformation("optionBar1");
            Spinner spin = findViewById(R.id.optionBar1);
            spin.setSelection(((ArrayAdapter) spin.getAdapter()).getPosition(opt));

            opt = db.getInformation("optionBar2");
            spin = findViewById(R.id.optionBar2);
            spin.setSelection(((ArrayAdapter) spin.getAdapter()).getPosition(opt));

            opt = db.getInformation("optionBar3");
            spin = findViewById(R.id.optionBar3);
            spin.setSelection(((ArrayAdapter) spin.getAdapter()).getPosition(opt));

            opt = db.getInformation("optionBar4");
            spin = findViewById(R.id.optionBar4);
            spin.setSelection(((ArrayAdapter) spin.getAdapter()).getPosition(opt));
        } catch (Exception e) {

        }

    }

    public void returnToHome(View v) {
        Intent openActivity = new Intent(AppSettings.this, MainActivity.class);
        AppSettings.this.startActivity(openActivity);
    }

    public void saveAndReturnHome(View v) {
        Intent openActivity = new Intent(AppSettings.this, MainActivity.class);

        // Put all the option bar settings
        try {
            Spinner opt = findViewById(R.id.optionBar1);
            db.addInformation("optionBar1", opt.getSelectedItem().toString());
            opt = findViewById(R.id.optionBar2);
            db.addInformation("optionBar2", opt.getSelectedItem().toString());
            opt = findViewById(R.id.optionBar3);
            db.addInformation("optionBar3", opt.getSelectedItem().toString());
            opt = findViewById(R.id.optionBar4);
            db.addInformation("optionBar4", opt.getSelectedItem().toString());
        } catch (Exception e) {
            Log.e("CarTrackerSQL", "Could not write to database: " + e.toString());
            e.printStackTrace();
        }

        AppSettings.this.startActivity(openActivity);
    }
}
