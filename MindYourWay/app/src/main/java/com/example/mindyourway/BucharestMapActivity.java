package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BucharestMapActivity extends AppCompatActivity {

    private static final String TAG = "BucharestMapActivity";

    //vars
    private HashMap<String,Pair<Integer,Integer>> levelRangerForRegions;
    private LatLng cornerLeftUp = new LatLng(44.501426, 25.947406);
    private LatLng cornerRightUp = new LatLng(44.501426, 26.239759);
    private LatLng cornerRightDown = new LatLng(44.360718, 26.239759);
    private LatLng cornerLeftDown = new LatLng(44.360718, 25.947406);
    private LatLng myLocation = new LatLng(44.435597, 26.099499);

    //widgets
    private Button buttonGoToRegion;
    private ArrayList<View> buttonList;
    private TextView textLevelEnd;
    private TextView textLevelStart;
    private TextView textRegionName;
    private TextView textStatus;
    private Button buttonMyLocation;
    private View bucharestMap;
    private ImageView myLocationIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucharest_map);
        init();
    }

    private void generateLevels(){
        levelRangerForRegions = new HashMap<>();
        levelRangerForRegions.put("Center", new Pair<Integer, Integer>(1,5));
        levelRangerForRegions.put("Sector1", new Pair<Integer, Integer>(5,10));
        levelRangerForRegions.put("Sector3", new Pair<Integer, Integer>(5,15));
        //Log.d(TAG, "generateLevels: "+levelRangerForRegions.get("Center").first.toString());
    }

    private void pointMyLocationOnMap(LatLng myLocation){
        int[] location = new int[2];
        bucharestMap.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        x-=30;
        y-=130;
        int dx = bucharestMap.getWidth();
        int dy = bucharestMap.getHeight();
        //Log.d(TAG, "pointMyLocationOnMap: "+ x +" "+ y+" "+dx+" "+dy);
        double rx = (myLocation.longitude - cornerLeftUp.longitude) / (cornerRightDown.longitude - cornerLeftUp.longitude);
        double ry = (myLocation.latitude - cornerLeftUp.latitude) / (cornerRightDown.latitude - cornerLeftUp.latitude);
        int myX = (int)(x + dx * rx);
        int myY = (int)(y + dy * ry);
        Log.d(TAG, "pointMyLocationOnMap: "+ myX+ " "+ myY);
        myLocationIcon.setX(myX);
        myLocationIcon.setY(myY);
        myLocationIcon.setVisibility(View.VISIBLE);
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
        buttonMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: asd");
                pointMyLocationOnMap(myLocation);

            }
        });

        buttonList = bucharestMap.getTouchables();
        instantiateRegionDescription("Center");
        for (View view : buttonList) {
            final Button button = (Button) view;
            Log.d(TAG, "init: "+button.getTag().toString());
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String regionName = button.getTag().toString();
                    instantiateRegionDescription(regionName);
                }
            });
        }
    }

    private void instantiateRegionDescription(final String regionName){
        Log.d(TAG, "instantiateRegionDescription: "+regionName);
        int levelStart = levelRangerForRegions.get(regionName).first;
        int levelEnd = levelRangerForRegions.get(regionName).second;
        String status = "Completed";

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
                if(regionName.equals("Sector1")) {
                    Intent intent = new Intent(BucharestMapActivity.this, Sector1MapActivity.class);
                    startActivity(intent);
                }
                if(regionName.equals("Sector3")) {
                    Intent intent = new Intent(BucharestMapActivity.this, Sector3MapActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
