package com.example.pate5.cartracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

            TextView idView = findViewById(R.id.entryIdBox);
            idView.setText(entryAttribs.get(1));

            TextView dateView = findViewById(R.id.entryDateBox);
            dateView.setText(entryAttribs.get(2));

            TextView typeView = findViewById(R.id.entryTypeBox);
            String type = entryAttribs.get(3);
            typeView.setText(type);

            TextView commentView = findViewById(R.id.entryCommentsBox);
            commentView.setText(entryAttribs.get(4));

            // custom information
            TextView bar1 = findViewById(R.id.entryBar1Box);
            TextView bar2 = findViewById(R.id.entryBar2Box);
            TextView bar3 = findViewById(R.id.entryBar3Box);
            TextView bar1Label = findViewById(R.id.view1Label);
            TextView bar2Label = findViewById(R.id.view2Label);
            TextView bar3Label = findViewById(R.id.view3Label);
            switch (type) {
                case "Gas":
                    bar3.setVisibility(View.VISIBLE);
                    bar3Label.setVisibility(View.VISIBLE);
                    bar3Label.setText("Total Price");
                    bar3.setText(entryAttribs.get(7));
                    bar2Label.setText("Gallons");
                    bar2.setText(entryAttribs.get(6));
                    bar1Label.setText("Price");
                    bar1.setText(entryAttribs.get(5));
                    break;
                case "Repair":
                    bar3.setVisibility(View.INVISIBLE);
                    bar3Label.setVisibility(View.INVISIBLE);
                    bar2Label.setText("Name");
                    bar2.setText(entryAttribs.get(5));
                    bar1Label.setText("Price");
                    bar1.setText(entryAttribs.get(6));
                    break;
                case "Maintenance":
                    bar3.setVisibility(View.INVISIBLE);
                    bar3Label.setVisibility(View.INVISIBLE);
                    bar2Label.setText("Name");
                    bar2.setText(entryAttribs.get(5));
                    bar1Label.setText("Price");
                    bar1.setText(entryAttribs.get(6));
                    break;
                default:

            }

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

    public void returnToEntryList(View v) {
        Intent openActivity = new Intent(activity_entry_viewer.this, activity_entry_list.class);
        activity_entry_viewer.this.startActivity(openActivity);
    }

    public void share(View v) {
        // TODO: Add share feature to entry viewer
        Context context = getApplicationContext();
        CharSequence text = "Not available yet.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void delete(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete?");
        builder.setMessage("Do you want to delete this entry? Cannot be undone.");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Delete the entry and return to the list, with a toast
                db.deleteEntryByEid(getIntent().getStringExtra("eid"));
                Context context = getApplicationContext();
                CharSequence text = "Entry deleted.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                returnToEntryList(null);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void edit(View v) {
        // Send the eid in the intent to the entry editor so that it can populate boxes
        Intent openActivity = new Intent(activity_entry_viewer.this, activity_entry_editor.class);
        openActivity.putExtra("eid", getIntent().getStringExtra("eid"));
        activity_entry_viewer.this.startActivity(openActivity);
    }
}
