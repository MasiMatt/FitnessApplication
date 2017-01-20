package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Activity that display all the trophies that the use has aquired
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TrophyActivity extends AppCompatActivity {

    public static TextView[] textViews = new TextView[6]; // array of textviews for trophies

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy);

        for(int i = 0; i < 6; i++)
            textViews[i] = new TextView(this);

        /*****Some custom trophies to unlock*****/
        textViews[0] = (TextView) findViewById(R.id.tenMeters);
        textViews[1] = (TextView) findViewById(R.id.fiftyMeters);
        textViews[2] = (TextView) findViewById(R.id.twoHundredMeters);
        textViews[3] = (TextView) findViewById(R.id.tenCalories);
        textViews[4] = (TextView) findViewById(R.id.fifteenCalories);
        textViews[5] = (TextView) findViewById(R.id.twentyCalories);
    }

}
