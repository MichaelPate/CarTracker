package com.example.pate5.cartracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Objects;

public class activity_app_settings extends AppCompatActivity {

    // All settings and options here
    //NOTE: All settings need a value here to capture the value upon creation
    String opt1, opt2, opt3, opt4, initMiles;

    private entry_database db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");

        db = new entry_database(this);

        try {
            opt1 = db.getInformation("optionBar1");
            Spinner spin = findViewById(R.id.optionBar1);
            spin.setSelection(((ArrayAdapter) spin.getAdapter()).getPosition(opt1));

            opt2 = db.getInformation("optionBar2");
            spin = findViewById(R.id.optionBar2);
            spin.setSelection(((ArrayAdapter) spin.getAdapter()).getPosition(opt2));

            opt3 = db.getInformation("optionBar3");
            spin = findViewById(R.id.optionBar3);
            spin.setSelection(((ArrayAdapter) spin.getAdapter()).getPosition(opt3));

            opt4 = db.getInformation("optionBar4");
            spin = findViewById(R.id.optionBar4);
            spin.setSelection(((ArrayAdapter) spin.getAdapter()).getPosition(opt4));

            initMiles = db.getInformation("initialMiles");
            EditText et = findViewById(R.id.initialOdoBox);
            et.setText(initMiles);
        } catch (Exception e) {
            Log.e("CarTrackerSQL", "Could not get information. :" + e.toString());
        }

    }

    public void returnToHome(View v) {

        // If app is new, then dont let them go home without saving
        String isNew = db.getInformation("isInstallNew");
        if(isNew == null) {
            // Error message and cancel
            Context context = getApplicationContext();
            CharSequence text = "Must enter settings.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            // Check if anything has changed, compare the initial variables to what they are now
            boolean isChanged = false;
            
            Spinner opt = findViewById(R.id.optionBar1);
            if (!opt.getSelectedItem().toString().equals(opt1)) isChanged = true;
            opt = findViewById(R.id.optionBar2);
            if (!opt.getSelectedItem().toString().equals(opt2)) isChanged = true;
            opt = findViewById(R.id.optionBar3);
            if (!opt.getSelectedItem().toString().equals(opt3)) isChanged = true;
            opt = findViewById(R.id.optionBar4);
            if (!opt.getSelectedItem().toString().equals(opt4)) isChanged = true;
            EditText initialMilesBox = findViewById(R.id.initialOdoBox);
            if (!initialMilesBox.getText().toString().equals(initMiles)) isChanged = true;

            // Display the warning if anything changed
            if (isChanged) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Unsaved Changes");
                builder.setMessage("You have changed some settings, do you still want to return home?");
                builder.setPositiveButton("Home", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent openActivity = new Intent(activity_app_settings.this, activity_main.class);
                        activity_app_settings.this.startActivity(openActivity);
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
            } else {
                Intent openActivity = new Intent(activity_app_settings.this, activity_main.class);
                activity_app_settings.this.startActivity(openActivity);
            }
        }
    }

    public void saveAndReturnHome(View v) {
        Intent openActivity = new Intent(activity_app_settings.this, activity_main.class);

        // Put all the option bar settings
        try {
            // These do not need validation as they are drop down boxes
            Spinner opt = findViewById(R.id.optionBar1);
            db.addInformation("optionBar1", opt.getSelectedItem().toString());
            opt = findViewById(R.id.optionBar2);
            db.addInformation("optionBar2", opt.getSelectedItem().toString());
            opt = findViewById(R.id.optionBar3);
            db.addInformation("optionBar3", opt.getSelectedItem().toString());
            opt = findViewById(R.id.optionBar4);
            db.addInformation("optionBar4", opt.getSelectedItem().toString());

            // Input sanitation, dont save if the value is invalid, give an error message instead.
            try {
                // Test the format of the initial miles setting
                EditText initialMilesBox = findViewById(R.id.initialOdoBox);
                String initialMilesText = initialMilesBox.getText().toString();
                Double.parseDouble(initialMilesText); // If the user entered bad text, this will fail
                db.addInformation("initialMiles", initialMilesText);

                // ****
                // Other settings that need validation go here
                // ****

                // Only go back if all validations pass
                Context context = getApplicationContext();
                CharSequence text = "Settings saved.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                db.addInformation("isInstallNew", "false");
                activity_app_settings.this.startActivity(openActivity);
            } catch (NumberFormatException ex) {
                Context context = getApplicationContext();
                CharSequence text = "Some settings are invalid.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        } catch (Exception e) {
            Log.e("CarTrackerSQL", "Could not write to database: " + e.toString());
            e.printStackTrace();
        }
    }
}
