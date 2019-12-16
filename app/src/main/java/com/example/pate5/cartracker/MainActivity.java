package com.example.pate5.cartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EntryDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            db = new EntryDatabase(this);

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
    }

    public void aboutThisApp(View v) {
        Intent openActivity = new Intent(MainActivity.this, AboutThisApp.class);
        openActivity.putExtra("Version", "1.0");
        MainActivity.this.startActivity(openActivity);
    }

    public void openSettings(View v) {
        Intent openActivity = new Intent(MainActivity.this, AppSettings.class);

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

        MainActivity.this.startActivity(openActivity);
    }
}
