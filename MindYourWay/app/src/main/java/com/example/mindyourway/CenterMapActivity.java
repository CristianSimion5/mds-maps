package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CenterMapActivity extends RegionMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_map);
        regionName = "Center";
        context = getApplicationContext();
        init();
    }

    @Override
    protected void initGames() {
        super.initGames();
        games.put("Center_1","sudoku");
        games.put("Center_2","sudoku");
        games.put("Center_3","findWord");
    }

    @Override
    protected void initBackgrounds() {
        super.initBackgrounds();
        backgrounds.put("Center_1",R.drawable.center_1);
        backgrounds.put("Center_2",R.drawable.center_2);
        backgrounds.put("Center_3",R.drawable.center_3);
        backgrounds.put("Center_4",R.drawable.center_4);
    }

}
