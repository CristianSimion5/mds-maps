package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BucharestMapActivity extends AppCompatActivity {

    private static final String TAG = "BucharestMapActivity";

    //vars
    private HashMap<String,Pair<Integer,Integer>> levelRangerForRegions;
    private LatLng cornerLeftUp = new LatLng(44.501426, 25.947406);
    private LatLng cornerRightUp = new LatLng(44.501426, 26.239759);
    private LatLng cornerRightDown = new LatLng(44.360718, 26.239759);
    private LatLng cornerLeftDown = new LatLng(44.360718, 25.947406);
    private LatLng myLocation= new LatLng(44.435597, 26.099499);
    private Location currentLocation;

    //widgets
    private Button buttonGoToRegion;
    private Button buttonBack;
    private ArrayList<View> buttonList;
    private TextView textLevelEnd;
    private TextView textLevelStart;
    private TextView textRegionName;
    private TextView textStatus;
    private Button buttonMyLocation;
    private View bucharestMap;
    private ImageView myLocationIcon;
    private TextView textName;
    private TextView textLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucharest_map);
        init();
    }

    private void generateLevels(){
        levelRangerForRegions = new HashMap<>();
        levelRangerForRegions.put("Center", new Pair<Integer, Integer>(0,5));
        levelRangerForRegions.put("Sector1", new Pair<Integer, Integer>(5,15));
        levelRangerForRegions.put("Sector3", new Pair<Integer, Integer>(5,15));
        levelRangerForRegions.put("Sector2", new Pair<Integer, Integer>(15,25));
        levelRangerForRegions.put("Sector4", new Pair<Integer, Integer>(15,25));
        levelRangerForRegions.put("Sector5", new Pair<Integer, Integer>(25,35));
        levelRangerForRegions.put("Sector6", new Pair<Integer, Integer>(25,35));
        //Log.d(TAG, "generateLevels: "+levelRangerForRegions.get("Center").first.toString());
    }

    private void pointMyLocationOnMap(LatLng myLocation){
        int[] location = new int[2];
        bucharestMap.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int dx = bucharestMap.getWidth();
        int dy = bucharestMap.getHeight();
        Log.d(TAG, "pointMyLocationOnMap: "+ x +" "+ y+" "+dx+" "+dy);
        double rx = (myLocation.longitude - cornerLeftUp.longitude) / (cornerRightDown.longitude - cornerLeftUp.longitude);
        double ry = (myLocation.latitude - cornerLeftUp.latitude) / (cornerRightDown.latitude - cornerLeftUp.latitude);
        int myX = (int)(dx * rx-40);
        int myY = (int)(dy * ry-40);
        Log.d(TAG, "pointMyLocationOnMap: "+ rx + " " + ry + " " + myX + " "+ myY);
        myLocationIcon.setX(myX);
        myLocationIcon.setY(myY);
        myLocationIcon.setVisibility(View.VISIBLE);
    }

    private boolean checkIfInCity(LatLng myLocation){
        if (myLocation.latitude > cornerLeftUp.latitude) return false;
        if (myLocation.latitude < cornerRightDown.latitude) return false;
        if (myLocation.longitude < cornerLeftUp.longitude) return false;
        if (myLocation.longitude > cornerRightDown.longitude) return false;
        return true;
    }

    private void init(){
        generateLevels();
        myLocationIcon = (ImageView) findViewById(R.id.locationIcon);
        bucharestMap = findViewById(R.id.BucharestMap);
        textLevelEnd = (TextView) findViewById(R.id.textLevelEnd);
        textLevelStart = (TextView) findViewById(R.id.textLevelStart);
        textRegionName = (TextView) findViewById(R.id.textRegionName);
        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonGoToRegion = (Button) findViewById(R.id.buttonGoToRegion);
        buttonMyLocation = (Button) findViewById(R.id.buttonMyLocation);
        textName = (TextView) findViewById(R.id.textName);
        textLevel = (TextView) findViewById(R.id.textLevel);

        buttonBack = (Button) findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BucharestMapActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        textName.setText(MainActivity.user.getName());
        textLevel.setText(String.valueOf(MainActivity.user.getLevel()));
        buttonMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDeviceLocation();
                //myLocation = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                //pointMyLocationOnMap(myLocation);
                Log.d(TAG, "onClick: "+ myLocation.latitude + myLocation.longitude);

            }
        });

        buttonList = bucharestMap.getTouchables();
        initRegionDescription("Center");
        for (View view : buttonList) {
            final Button button = (Button) view;
            Log.d(TAG, "init: "+button.getTag().toString());
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String regionName = button.getTag().toString();
                    initRegionDescription(regionName);
                }
            });
        }
    }

    private void initRegionDescription(final String regionName){
        Log.d(TAG, "instantiateRegionDescription: "+regionName);
        int levelStart = levelRangerForRegions.get(regionName).first;
        int levelEnd = levelRangerForRegions.get(regionName).second;
        String status = "Coming soon";
        if(!regionName.equals("Sector5") && !regionName.equals("Sector6")) {
            if(MainActivity.user.getStatus(regionName+"_"+String.valueOf(1)) == 1) {
                if(regionName.equals("Sector1") || regionName.equals("Sector3")) {
                    if(MainActivity.user.getStatus("Center_3") != 4) {
                        status = "Region unavailable yet";
                    } else {
                        MainActivity.user.incrementStatus(regionName+"_"+String.valueOf(1));
                        status = "0/3 checkpoints completed";
                    }
                }
                if(regionName.equals("Sector2")) {
                    if(MainActivity.user.getStatus("Sector1_3") != 4) {
                        status = "Region unavailable yet";
                    } else {
                        MainActivity.user.incrementStatus(regionName+"_"+String.valueOf(1));
                        status = "0/3 checkpoints completed";
                    }
                }
                if(regionName.equals("Sector4")) {
                    if(MainActivity.user.getStatus("Sector3_3") != 4) {
                        status = "Region unavailable yet";
                    } else {
                        status = "0/3 checkpoints completed";
                        MainActivity.user.incrementStatus(regionName+"_"+String.valueOf(1));
                    }
                }
            } else {
                int progress = 0;

                while (progress < 3 && MainActivity.user.getStatus(regionName + "_" + (progress + 1)) == 4) {
                    progress++;
                }
                if (progress == 3) {
                    status = "Completed";
                } else {
                    status = String.valueOf(progress) + "/3 checkpoints completed";
                }
            }
        }

        textRegionName.setText(regionName);
        textLevelStart.setText(String.valueOf(levelStart));
        textLevelEnd.setText(String.valueOf(levelEnd));
        textStatus.setText(status);
        buttonGoToRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(regionName.equals("Center")) {
                    Intent intent = new Intent(BucharestMapActivity.this, CenterMapActivity.class);
                    startActivity(intent);
                }
                else if(regionName.equals("Sector1")) {
                    Intent intent = new Intent(BucharestMapActivity.this, Sector1MapActivity.class);
                    startActivity(intent);
                }
                else if(regionName.equals("Sector3")) {
                    Intent intent = new Intent(BucharestMapActivity.this, Sector3MapActivity.class);
                    startActivity(intent);
                }
                else if(regionName.equals("Sector2")) {
                    Intent intent = new Intent(BucharestMapActivity.this, Sector2MapActivity.class);
                    startActivity(intent);
                }
                else if(regionName.equals("Sector4")) {
                    Intent intent = new Intent(BucharestMapActivity.this, Sector4MapActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        client.getLastLocation().addOnSuccessListener(BucharestMapActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    Log.d(TAG, "onSuccess: "+location.getLatitude()+ " "+ location.getLongitude());
                    myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    if(checkIfInCity(myLocation)) {
                        pointMyLocationOnMap(myLocation);
                    } else {
                        Toast.makeText(BucharestMapActivity.this, "Your are not in Bucharest", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
