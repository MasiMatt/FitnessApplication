package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Activity that displays all the stats of a run
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private Spinner spin;
    private TextView dist;
    private TextView dur;
    private TextView speed;
    private TextView cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dist = (TextView) findViewById(R.id.textView5);
        dur = (TextView) findViewById(R.id.textView6);
        speed = (TextView) findViewById(R.id.textView7);
        cal = (TextView) findViewById(R.id.textView8);

        // creating database object
        final DBHandler db = new DBHandler(this);

        // a spinner that displays 5 sets of run stats
        addItemsOnSpinner();
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();

                // displays first set of stats on first entry of spinner
                if(selectedItem.equals("Item 1")) {
                    if(db.getRunStatsCount() >= 1) {
                        dist.setText(Double.toString(db.getRunStats(1).getDistance()));
                        dur.setText(db.getRunStats(1).getDuration());
                        speed.setText(Double.toString(db.getRunStats(1).getAverageSpeed()));
                        cal.setText(Double.toString(db.getRunStats(1).getCalorieBurn()));
                    }
                    else{
                        dist.setText("0");
                        dur.setText("0");
                        speed.setText("0");
                        cal.setText("0");
                    }
                }
                // displays second set of stats on second entry of spinner
                if(selectedItem.equals("Item 2")) {
                    if(db.getRunStatsCount() >= 2) {
                        dist.setText(Double.toString(db.getRunStats(2).getDistance()));
                        dur.setText(db.getRunStats(2).getDuration());
                        speed.setText(Double.toString(db.getRunStats(2).getAverageSpeed()));
                        cal.setText(Double.toString(db.getRunStats(2).getCalorieBurn()));
                    }
                    else{
                        dist.setText("0");
                        dur.setText("0");
                        speed.setText("0");
                        cal.setText("0");
                    }
                }
                // displays third set of stats on third entry of spinner
                if(selectedItem.equals("Item 3")) {
                    if(db.getRunStatsCount() >= 3) {
                        dist.setText(Double.toString(db.getRunStats(3).getDistance()));
                        dur.setText(db.getRunStats(3).getDuration());
                        speed.setText(Double.toString(db.getRunStats(3).getAverageSpeed()));
                        cal.setText(Double.toString(db.getRunStats(3).getCalorieBurn()));
                    }
                    else{
                        dist.setText("0");
                        dur.setText("0");
                        speed.setText("0");
                        cal.setText("0");
                    }
                }
                // displays fourth set of stats on fourth entry of spinner
                if(selectedItem.equals("Item 4")) {
                    if(db.getRunStatsCount() >= 4) {
                        dist.setText(Double.toString(db.getRunStats(4).getDistance()));
                        dur.setText(db.getRunStats(4).getDuration());
                        speed.setText(Double.toString(db.getRunStats(4).getAverageSpeed()));
                        cal.setText(Double.toString(db.getRunStats(4).getCalorieBurn()));
                    }
                    else{
                        dist.setText("0");
                        dur.setText("0");
                        speed.setText("0");
                        cal.setText("0");
                    }
                }
                // displays fifth set of stats on fifth entry of spinner
                if(selectedItem.equals("Item 5")) {
                    if(db.getRunStatsCount() >= 5) {
                        dist.setText(Double.toString(db.getRunStats(5).getDistance()));
                        dur.setText(db.getRunStats(5).getDuration());
                        speed.setText(Double.toString(db.getRunStats(5).getAverageSpeed()));
                        cal.setText(Double.toString(db.getRunStats(5).getCalorieBurn()));
                    }
                    else{
                        dist.setText("0");
                        dur.setText("0");
                        speed.setText("0");
                        cal.setText("0");
                    }
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        Intent mIntent = getIntent();
        int j = mIntent.getIntExtra("Spinner Select", 0);
        spin.setSelection(j-1);
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        spin = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("Item 1");
        list.add("Item 2");
        list.add("Item 3");
        list.add("Item 4");
        list.add("Item 5");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
