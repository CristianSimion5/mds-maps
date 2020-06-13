package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Sector4MapActivity extends RegionMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector4_map);
        regionName = "Sector4";
        context = getApplicationContext();
        init();
    }

    @Override
    protected void initGames() {
        super.initGames();
        games.put("Sector4_1","findWord");
        games.put("Sector4_2","sudoku");
        games.put("Sector4_3","colorlink");
    }

    @Override
    protected void initBackgrounds() {
        super.initBackgrounds();
        backgrounds.put("Sector4_1",R.drawable.sector4_1);
        backgrounds.put("Sector4_2",R.drawable.sector4_2);
        backgrounds.put("Sector4_3",R.drawable.sector4_3);
        backgrounds.put("Sector4_4",R.drawable.sector4_4);
    }
}
