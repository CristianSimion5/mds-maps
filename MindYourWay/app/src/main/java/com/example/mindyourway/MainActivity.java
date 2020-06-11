package com.example.mindyourway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String mainExtra = "com.example.MindYourWay.EXTRA_TEXT";

    private static final Integer ERROR_DIALOG_REQUEST = 9001;

    private static final int locationPermissionRequestCode = 1234;
    private static boolean mLocationPermissionGranted = false;

    //Model
    public static User user;

    //widgets
    private Button btnMap;
    private TextView textName;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: asd");

        if (isServicesOK()){
            getLocationPermission();
                init();

        }
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: Getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;

            } else {
                ActivityCompat.requestPermissions(this,permissions,locationPermissionRequestCode);
            }
        } else {
            ActivityCompat.requestPermissions(this,permissions,locationPermissionRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case locationPermissionRequestCode: {
                if(grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void init() {
        user = new User(this);
        btnMap = (Button) findViewById(R.id.btnMap);
        textName = (TextView) findViewById(R.id.textName);
        textName.setText(user.getName());

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLocationPermissionGranted) {

                    final String name = textName.getText().toString();
                    user.setName(name);
                    Intent intent = new Intent(MainActivity.this, BucharestMapActivity.class);
                    Log.d(TAG, "onClick: " + user.getName());
                    Log.d(TAG, "onClick: " + user.getLevel());
                    //intent.putExtra(EXTRA_TEXT, coordString);
                    startActivity(intent);
                }
            }
        });

        Button btnGuide = (Button) findViewById(R.id.btnGuide);
        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });

        Button btnCredits = (Button) findViewById(R.id.btnCredits);
        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
                startActivity(intent);
            }
        });

        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.reset();
            }
        });
    }

    public boolean isServicesOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google Play services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOK: An eror occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
