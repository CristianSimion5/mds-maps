package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Sector3MapActivity extends RegionMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector3_map);
        regionName = "Sector3";
        context = getApplicationContext();
        init();
    }

    @Override
    protected void initGames() {
        super.initGames();
        games.put("Sector3_1","colorlink");
        games.put("Sector3_2","findWord");
        games.put("Sector3_3","sudoku");
    }

    @Override
    protected void initBackgrounds() {
        super.initBackgrounds();
        backgrounds.put("Sector3_1",R.drawable.sector3_1);
        backgrounds.put("Sector3_2",R.drawable.sector3_2);
        backgrounds.put("Sector3_3",R.drawable.sector3_3);
        backgrounds.put("Sector3_4",R.drawable.sector3_4);
    }
}
