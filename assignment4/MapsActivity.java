package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Activity that displays the map with the path taken by the user and  all the stats (distance,
 * duration, average speed, and calories burned).
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import static android.view.View.VISIBLE;
import static com.example.assignment4.R.id.map;
import static com.example.assignment4.TrophyActivity.textViews;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static TextView distanceValue, timerValue, speedValue, calsValue;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 0;
    private GoogleMap mMap;
    private LatLng latLng, prev, last;
    private Button startButton, pauseButton;
    private Marker currLocationMarker;
    private Timer timer;
    private TrophiesUnlocked trophiesUnlocked;
    private DistanceCalculator distanceCalculator;
    private Boolean backButton = true;
    private double distance, distanceDiff = 0.0;
    private double speed, avgSpeed, currSpeed, prevSpeed = 0.0;
    private double calsBurned = 0.0;
    private int time1, time2 = 0;
    static int j = 1;
    private int speedCtr = 0;
    private int locFlag = 0;
    private int prevInt = 0;
    private SharedPreferences mPrefs;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        // checks manifest permission access coarse location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        db = new DBHandler(this);
        startButton = (Button) findViewById(R.id.button2); // start Button
        pauseButton = (Button) findViewById(R.id.button3); // Pause button
        pauseButton.setVisibility(View.INVISIBLE);
        distanceCalculator = new DistanceCalculator(); // CLass to calculate distance
        trophiesUnlocked = new TrophiesUnlocked(); // Class that unlocks trophies

        distanceValue = (TextView) findViewById(R.id.distanceValue);
        speedValue = (TextView) findViewById(R.id.speedValue);
        calsValue = (TextView) findViewById(R.id.calsValue);

        timer = new Timer(); // Class that calculates time
        timerValue = (TextView) findViewById(R.id.timerValue);

        // index of last entry of database
        mPrefs = getSharedPreferences("label", 0);
        j = mPrefs.getInt("ctr", 1);

        // Pressing the start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MapsActivity.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION); // start foreground
                startService(startIntent);
                backButton = false;
                locFlag = 1;
                startButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(VISIBLE);
                timer.startTimer(); // starts timer
                turnGPSOn(); // turns on GPS
            }
        });

        // pressing the pause button
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locFlag = 0;
                pauseButton.setVisibility(View.INVISIBLE);
                startButton.setVisibility(VISIBLE);
                timer.stopTimer(); // stops timer
                turnGPSOff(); // turns off gps
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("ProfilePreference", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("profileName", null);

        //forces user to create profile before doing anything else
        if(name == null)
            goToProfileActivity();
    }

    // Goes to the maps activity
    void goToProfileActivity(){
        Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    // checks if user granted permission to use GPS
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recreate();
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // when creating the map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    // building Google API Client
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // when first connecting to google API client
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // checks permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        // creating marker for current location
        if (mLastLocation != null) {
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            last = latLng;
            prev = latLng;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            currLocationMarker = mMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // 5 seconds
        mLocationRequest.setFastestInterval(5000); // 5 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5); //5 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    // when location changes
    @Override
    public void onLocationChanged(Location location) {
        last = prev;
        prev = latLng; // keeps track of previous coordinates
        latLng = new LatLng(location.getLatitude(), location.getLongitude()); // get new coordinates
        distanceDiff = distanceCalculator.CalculationByDistance(prev, latLng); // calculates distance between old and new coordinates
        time1 = time2; // previous time
        time2 = timer.getTime(); // current time
        prevSpeed = currSpeed; // previous speed
        distanceDiff = distanceDiff*2;

        // disregard inaccurate results
        if(((distanceDiff >= (prevSpeed*(time2 - time1))*2) && (prevSpeed != 0.0)) || (prevSpeed == 0.0 && distanceDiff >= 20)) {
            prev = last;
            return;
        }

        // if result is accurate enough
        else {
            distance += (distanceDiff*0.46); // adds to total distance

            if(currSpeed != 0) {
                currSpeed = (distanceDiff / ((time2 - time1) + 1));
                speed += currSpeed;
            }

            else {
                currSpeed = 2;
                speed += 0.5;
            }

            //avgSpeed = (speed / speedCtr);
            avgSpeed = distance/timer.getTime();
            if (speedCtr == 0)
                avgSpeed = 0;
        }
        // if not first marker
        if(locFlag == 1) {

            if (currLocationMarker != null) {
                currLocationMarker.remove();
            }

            // creates line from gps position
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(prev, latLng)
                    .width(17)
                    .color(Color.BLUE));

            Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();

            distanceValue.setText(new DecimalFormat("#.##").format(distance) + "m"); // display distance
            speedValue.setText(new DecimalFormat("#.##").format(avgSpeed) + "m/s"); // display speed

            // getting weight from profile
            SharedPreferences sharedPreferences = getSharedPreferences("ProfilePreference", Context.MODE_PRIVATE);
            double weight = Double.parseDouble(sharedPreferences.getString("profileWeight", null));

            calsBurned = (0.75 * (weight*2.20462) * (distance / 1609.34)); // calculate calories burned
            calsValue.setText(new DecimalFormat("#.##").format(calsBurned)); // display calories burned

            // checks if any trophies have been unlocked
            int unlockNum = 0;
            while(unlockNum != -1) {
                unlockNum = trophiesUnlocked.checkCompleted(distance, 0, 0, calsBurned);

                // if a trophy has been unlocked, shows notification and unlocks it on trophy activity
                if(unlockNum != -1) {
                    showNotification(trophiesUnlocked.getTrophy(unlockNum));
                    //textViews[unlockNum].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.trophy_gold);
                }
            }
            speedCtr++;
        }
    }

    // turns off GPS
    public void turnGPSOff() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);
    }

    // turns on GPS
    public void turnGPSOn() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_maps, menu);
        return true;
    }

    // toolbar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_profile: // profile page
                Intent i2 = new Intent(MapsActivity.this, ProfileActivity.class);
                startActivity(i2);
                return true;

            case R.id.action_report: // report page
                Intent i3 = new Intent(MapsActivity.this, ReportActivity.class);
                startActivity(i3);
                return true;

            case R.id.action_trophies: // trophies page
                Intent i4 = new Intent(MapsActivity.this, TrophyActivity.class);
                startActivity(i4);
                return true;

            case R.id.action_save: // saves run
                Intent stopIntent = new Intent(MapsActivity.this, ForegroundService.class);
                stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION); // stop foreground service
                startService(stopIntent);
                backButton = true;
                startButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.INVISIBLE);
                locFlag = 0;

                // asks if user is sure they want to save
                new AlertDialog.Builder(MapsActivity.this)
                        .setMessage("Are you sure you want to end run?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                timer.stopTimer(); // stop timer
                                turnGPSOff(); // turn off GPS
                                Toast.makeText(getApplicationContext(), "Run Saved", Toast.LENGTH_LONG).show();

                                // insert data into database
                                if (db.getRunStatsCount() >= 5) {
                                    Log.d("Insert: ", "Updating..");
                                    db.updateRunStats(new RunStats(j, distance, timerValue.getText().toString(), avgSpeed, calsBurned));
                                    prevInt = j;
                                    j++;
                                    if (j == 6)
                                        j = 1;
                                }
                                // update old values of database
                                else {
                                    Log.d("Insert: ", "Inserting ..");
                                    db.addRunStats(new RunStats(distance, timerValue.getText().toString(), avgSpeed, calsBurned));
                                    prevInt = j;
                                    j++;
                                    if (j == 6)
                                        j = 1;
                                }

                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                mEditor.putInt("ctr", j).commit();
                                // brings user to report activity
                                Intent intent = new Intent(MapsActivity.this, ReportActivity.class);
                                intent.putExtra("Spinner Select", prevInt);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startButton.setVisibility(VISIBLE);
                            }
                        })
                        .show();
                return true;

            case R.id.action_newRun: // create new run
                if(locFlag == 0)
                    recreate();

                else
                    Toast.makeText(this, "Save run first!", Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // creates new map
    @Override
    public void recreate()
    {
        if (android.os.Build.VERSION.SDK_INT >= 11)
        {
            super.recreate();
        }
        else
        {
            startActivity(getIntent());
            finish();
        }
    }

    // displays notification for acquired trophy
    public void showNotification(Trophy t) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MapsActivity.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(t.getName())
                .setSmallIcon(R.drawable.trophy)
                .setContentTitle(t.getName())
                .setContentText(t.getDescription())
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(t.getId(), notification);
    }
}