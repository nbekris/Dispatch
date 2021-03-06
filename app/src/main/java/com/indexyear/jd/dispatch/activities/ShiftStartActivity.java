package com.indexyear.jd.dispatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.indexyear.jd.dispatch.R;
import com.indexyear.jd.dispatch.data.ManageUsers;
import com.indexyear.jd.dispatch.models.Employee;

public class ShiftStartActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener{

    private static final String TAG = "ShiftStartActivity: ";

    private FirebaseAuth mAuth;
    private FirebaseAnalytics mAnalyticsInstance;
    private DatabaseReference mDB;
    private ManageUsers mUser;
    private Employee mEmployee;

    Spinner role_spinner;
    Spinner team_spinner;
    Spinner status_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.shift_start_button).setOnClickListener(this);

        role_spinner = (Spinner) findViewById(R.id.role_spinner);
        role_spinner.setOnItemSelectedListener(this);
        // jdp not sure how to set default values, may need to define arrayadapter programmatically
        // instead of in string values resource

        team_spinner = (Spinner) findViewById(R.id.team_spinner);
        status_spinner = (Spinner) findViewById(R.id.status_spinner);

        mAuth = FirebaseAuth.getInstance();
        mUser = new ManageUsers();
    }

    //when the button is clicked, take values from spinners
    @Override
    public void onClick(View v){
        int i = v.getId();

        Log.d(TAG, " onclick");

        final Intent ShiftStartHandoff = new Intent(this, MainActivity.class);

        if (i== R.id.shift_start_button){ //do I need a test if there's only one button?
            //update employee status
            Log.d(TAG, "shift start button");
            String role = role_spinner.getSelectedItem().toString();
            String team = team_spinner.getSelectedItem().toString();
            String status = status_spinner.getSelectedItem().toString();

            Log.d(TAG, "role is " + role +", team is " + team + ", and status is " + status);

            if (role.equals("MCT")) { //&& !team.isEmpty() && !status.isEmpty()
                updateEmployeeAsMCT(role, team, status);
            } else if (role.equals("Dispatcher")){
                updateEmployeeAsDispatch(role);
            } else {
                //do something to raise error
            }

            startActivity(ShiftStartHandoff);
        }

    }

    // jdp this is where I had intended to control which spinners are available, but it isn't
    // functioning as expected. it won't update a second time
    public void onItemSelected(AdapterView adapterView,View view, int pos, long id){
        if (role_spinner.getSelectedItem().toString().equals("MCT")){
            // make other spinners visible
            team_spinner.setVisibility(View.VISIBLE);
            status_spinner.setVisibility(View.VISIBLE);
        } else if (role_spinner.getSelectedItem().toString().equals("Dispatch")) {
            team_spinner.setVisibility(View.INVISIBLE);
            status_spinner.setVisibility(View.INVISIBLE);
        }
    }

    public void onNothingSelected(AdapterView adapterView){
        // do nothing?
    }

    //update the employee status with relevant values
    private void updateEmployeeAsMCT(String role, String team, String status){
        Log.d(TAG, "updateEmployeeAsMCT");
        String uid = mAuth.getCurrentUser().getUid();

        mUser.setUserRole(uid, role);
        mUser.setUserTeam(uid, team);
        mUser.setUserStatus(uid, status);
    }

    private void updateEmployeeAsDispatch(String role){
        Log.d(TAG, "updateEmployeeAsDispatch");
        String uid = mAuth.getCurrentUser().getUid();

        mUser.setUserRole(uid, role);
    }

}
