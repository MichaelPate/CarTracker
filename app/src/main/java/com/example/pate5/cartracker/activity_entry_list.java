package com.example.pate5.cartracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class activity_entry_list extends AppCompatActivity {

    private entry_database db = null;
    ArrayList<String> entries = null;
    ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);

        ListView entryList_ui = findViewById(R.id.mainEntryList);

        // Get each of the entries in the database and render them on create.
        db = new entry_database(this);
        try {
            entries = new ArrayList<>();
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, entries);
            entryList_ui.setAdapter(adapter);


            int entryCount = db.countAllEntries(false);
            if (entryCount > 0) {
                for (int i = 0; i < entryCount; i++) {
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
            CharSequence text = "Could not load entries.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            returnToHome(null);
            Log.e("CarTrackerError", e.getStackTrace().toString());
        }


        // Handle clicking of an entry
        entryList_ui.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedValue = (String) parent.getItemAtPosition(position);

                if (clickedValue.equals("No entries to display!")) {
                    Context context = getApplicationContext();
                    CharSequence text = "Click New Entry button to start.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    Intent readEntry =
                            new Intent(activity_entry_list.this, activity_entry_viewer.class);
                    readEntry.putExtra("eid", clickedValue.split(" ")[0]);
                    activity_entry_list.this.startActivity(readEntry);
                }
            }
        });

    }

    public void returnToHome(View v) {
        Intent openActivity = new Intent(activity_entry_list.this, activity_main.class);
        activity_entry_list.this.startActivity(openActivity);
    }

    public void exportAll(View v) {
        // TODO: Add export feature to entry list viewer
        Context context = getApplicationContext();
        CharSequence text = "Not available yet.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void newEntry(View v) {
        // Get the highest eid, add one, and send that to the entry editor
        int targetEid = db.countAllEntries(false);
        targetEid++;
        Intent openActivity = new Intent(activity_entry_list.this, activity_entry_editor.class);
        openActivity.putExtra("eid", Integer.toString(targetEid));
        activity_entry_list.this.startActivity(openActivity);
    }
}
