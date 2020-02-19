package com.example.pate5.cartracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class activity_main extends AppCompatActivity {

    entry_database db = new entry_database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
// check flag for new install, if new install, open app to settings page. Make sure user enters initial miles, and when they press save and home, clear the flag and go to home like normal.


            String isNew = db.getInformation("isInstallNew");
            if(isNew == null) {
                // Open settings intent
                // Set isInstallNew to "False"
                openSettings(null);
            }

            String opt = db.getInformation("optionBar1");
            TextView lab = findViewById(R.id.option1Label);
            TextView val = findViewById(R.id.optionValue1);
            if (opt.equals("None")) {
                lab.setVisibility(View.INVISIBLE);
                val.setVisibility(View.INVISIBLE);
            } else {
                lab.setVisibility(View.VISIBLE);
                val.setVisibility(View.VISIBLE);
            }
            val.setText(db.getInformation(String.valueOf(lab.getText())));
            lab.setText(opt);

            opt = db.getInformation("optionBar2");
            lab = findViewById(R.id.option2Label);
            val = findViewById(R.id.optionValue2);
            if (opt.equals("None")) {
                lab.setVisibility(View.INVISIBLE);
                val.setVisibility(View.INVISIBLE);
            } else {
                lab.setVisibility(View.VISIBLE);
                val.setVisibility(View.VISIBLE);
            }
            lab.setText(opt);
            val.setText(db.getInformation(String.valueOf(lab.getText())));

            opt = db.getInformation("optionBar3");
            lab = findViewById(R.id.option3Label);
            val = findViewById(R.id.optionValue3);
            if (opt.equals("None")) {
                lab.setVisibility(View.INVISIBLE);
                val.setVisibility(View.INVISIBLE);
            } else {
                lab.setVisibility(View.VISIBLE);
                val.setVisibility(View.VISIBLE);
            }
            lab.setText(opt);
            val.setText(db.getInformation(String.valueOf(lab.getText())));

            opt = db.getInformation("optionBar4");
            lab = findViewById(R.id.option4Label);
            val = findViewById(R.id.optionValue4);
            if (opt.equals("None")) {
                lab.setVisibility(View.INVISIBLE);
                val.setVisibility(View.INVISIBLE);
            } else {
                lab.setVisibility(View.VISIBLE);
                val.setVisibility(View.VISIBLE);
            }
            lab.setText(opt);
            val.setText(db.getInformation(String.valueOf(lab.getText())));

        } catch (Exception e) {
            Log.e("CarTrackerSQL", "Exception: " + e.toString());
        }

        // Update the recent entries list
        ArrayList<String> entries = null;
        ArrayAdapter<String> adapter = null;
        ListView entryList_ui = findViewById(R.id.recentEntryList);
        try {
            entries = new ArrayList<>();
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, entries);
            entryList_ui.setAdapter(adapter);


            int entryCount = db.countAllEntries(false);

            int target = 0;
            if (entryCount >= 6) target = 6;
            else target = entryCount;

            if (entryCount > 0) {
                for (int i = 0; i < target; i++) {
                    ArrayList<String> curEntry = db.getEntryByEid(Integer.toString(i));
                    String id = curEntry.get(0).split(":")[1];      // ID
                    String date = curEntry.get(1).split(":")[1];    // Date
                    String type = curEntry.get(3).split(":")[1];    // Type

                    String price = "";
                    if (type.equals("Gas")) {
                        price = curEntry.get(5).split(":")[1];
                    } else if (type.equals("Repair")) {
                        price = curEntry.get(6).split(":")[1];
                    } else if (type.equals("Maintenance")) {
                        price = curEntry.get(6).split(":")[1];
                    }

                    String out = id + " - " + date + " - " + type + " - " + price;
                    entries.add(out);
                    adapter.notifyDataSetChanged();
                }
            } else {
                entries.add("No entries to display!");
                adapter.notifyDataSetChanged();
            }
        } catch(Exception e) {
            // Show toast and go home
            Context context = getApplicationContext();
            CharSequence text = "Could not load recent entries.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Log.e("CarTrackerError", e.toString());
        }

        // Handle clicking of an entry
        entryList_ui.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedValue = (String) parent.getItemAtPosition(position);

                if (clickedValue.equals("No entries to display!")) {
                    Context context = getApplicationContext();
                    CharSequence text = "Click View Entries button to start.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    Intent readEntry =
                            new Intent(activity_main.this, activity_entry_viewer.class);
                    readEntry.putExtra("eid", clickedValue.split(" ")[0]);
                    activity_main.this.startActivity(readEntry);
                }
            }
        });

    }

    public void aboutThisApp(View v) {
        Intent openActivity = new Intent(activity_main.this, activity_about_this_app.class);
        openActivity.putExtra("Version", "1.0");
        activity_main.this.startActivity(openActivity);
    }

    public void goToEntryList(View v) {
        Intent openActivity = new Intent(activity_main.this, activity_entry_list.class);
        activity_main.this.startActivity(openActivity);
    }

    public void openSettings(View v) {
        Intent openActivity = new Intent(activity_main.this, activity_app_settings.class);

        TextView tv = findViewById(R.id.option1Label);
        String opt = tv.getText().toString();
        openActivity.putExtra("Option1", opt);

        tv = findViewById(R.id.option2Label);
        opt = tv.getText().toString();
        openActivity.putExtra("Option2", opt);

        tv = findViewById(R.id.option3Label);
        opt = tv.getText().toString();
        openActivity.putExtra("Option3", opt);

        tv = findViewById(R.id.option4Label);
        opt = tv.getText().toString();
        openActivity.putExtra("Option4", opt);

        activity_main.this.startActivity(openActivity);
    }
}
