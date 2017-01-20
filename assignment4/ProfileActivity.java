package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Activity that receives the user's information (name, age, gender, height, weight)
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private EditText NameEdit;
    private EditText AgeEdit;
    private EditText WeightEdit;
    private EditText HeightEdit;
    private Switch genderSwitch;
    private Button saveButton;
    private Boolean flag = false;
    private Boolean backButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        genderSwitch = (Switch) findViewById(R.id.switchGender);
        genderSwitch.setEnabled(false);

        NameEdit = (EditText) findViewById(R.id.editName);
        NameEdit.setEnabled(false);
        AgeEdit = (EditText) findViewById(R.id.editAge);
        AgeEdit.setEnabled(false);
        WeightEdit = (EditText) findViewById(R.id.editWeight);
        WeightEdit.setEnabled(false);
        HeightEdit = (EditText) findViewById(R.id.editHeight);
        HeightEdit.setEnabled(false);

        saveButton = (Button) findViewById(R.id.sButton);

        // when pressing the save button
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // checks for invalid name
                if (NameEdit.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Invalid Name", Toast.LENGTH_LONG).show();
                    return;
                }
                // checks for invalid age
                if ( AgeEdit.getText().toString().trim().length() == 0 || Integer.parseInt(AgeEdit.getText().toString()) > 120){
                    Toast.makeText(getApplicationContext(), "Invalid Age", Toast.LENGTH_LONG).show();
                    return;
                }

                // checks for invalid weight
                if (WeightEdit.getText().toString().trim().length() == 0 || Integer.parseInt(WeightEdit.getText().toString()) > 300){
                    Toast.makeText(getApplicationContext(), "Invalid Weight", Toast.LENGTH_LONG).show();
                    return;
                }

                // checks for invalid height
                if (HeightEdit.getText().toString().trim().length() == 0 || Integer.parseInt(HeightEdit.getText().toString()) > 300){
                    Toast.makeText(getApplicationContext(), "Invalid Height", Toast.LENGTH_LONG).show();
                    return;
                }

                // saves all the information
                SharedPreferences sharedPreferences = getSharedPreferences("ProfilePreference", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("profileName", NameEdit.getText().toString());
                editor.putString("profileAge", AgeEdit.getText().toString());
                editor.putString("profileWeight", WeightEdit.getText().toString());
                editor.putString("profileHeight", HeightEdit.getText().toString());
                editor.putBoolean("profileGender", flag);
                editor.commit();

                saveButton.setVisibility(View.INVISIBLE);

                NameEdit.setEnabled(false);
                AgeEdit.setEnabled(false);
                WeightEdit.setEnabled(false);
                HeightEdit.setEnabled(false);
                genderSwitch.setEnabled(false);
                backButton = true;
            }
        });

        // checks value of gender switch
        genderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    flag = true;
                }else{
                    flag = false;
                }
            }
        });
    }

    // loads saved information when starting the activity
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("ProfilePreference", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("profileName", null);
        String age = sharedPreferences.getString("profileAge", null);
        String weight = sharedPreferences.getString("profileWeight", null);
        String height = sharedPreferences.getString("profileHeight", null);
        Boolean gender = sharedPreferences.getBoolean("profileGender", false);

        NameEdit.setText(name);
        AgeEdit.setText(age);
        WeightEdit.setText(weight);
        HeightEdit.setText(height);
        genderSwitch.setChecked(gender);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_profile, menu);
        return true;
    }

    // toolbar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_edit: // edit profile button
                backButton = false;

                saveButton.setVisibility(View.VISIBLE);

                NameEdit.setEnabled(true);
                AgeEdit.setEnabled(true);
                WeightEdit.setEnabled(true);
                HeightEdit.setEnabled(true);
                genderSwitch.setEnabled(true);
                return true;

            case android.R.id.home:
                if(backButton)
                    onBackPressed();
                else
                    Toast.makeText(getApplicationContext(), "Save before returning", Toast.LENGTH_LONG).show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
