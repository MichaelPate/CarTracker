package com.example.pate5.cartracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class activity_entry_viewer extends AppCompatActivity {

    entry_database db = new entry_database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_viewer);

        // Get the eid from the intent and get its information
        try {
            ArrayList<String> entryAttribs = db.getEntryByEid(getIntent().getStringExtra("eid"));

            // TODO: Get the attributes and render them on the activity.
            TextView idView = findViewById(R.id.entryIdBox);
            idView.setText(entryAttribs.get(1));

            TextView dateView = findViewById(R.id.entryDateBox);
            dateView.setText(entryAttribs.get(1));

            //TODO: Add code to populate the rest of the fields.

        } catch (Exception e) {
            Context context = getApplicationContext();
            CharSequence text = "Could not load entry.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            returnToHome(null);
            Log.e("CarTrackerError", e.getStackTrace().toString());
        }

    }

    public void returnToHome(View v) {
        Intent openActivity = new Intent(activity_entry_viewer.this, activity_main.class);
        activity_entry_viewer.this.startActivity(openActivity);
    }
}
