package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Sector2MapActivity extends RegionMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector2_map);
        regionName = "Sector2";
        context = getApplicationContext();
        init();
    }

    @Override
    protected void initGames() {
        super.initGames();
        games.put("Sector2_1","sudoku");
        games.put("Sector2_2","findWord");
        games.put("Sector2_3","colorlink");
    }

    @Override
    protected void initBackgrounds() {
        super.initBackgrounds();
        backgrounds.put("Sector2_1",R.drawable.sector2_1);
        backgrounds.put("Sector2_2",R.drawable.sector2_2);
        backgrounds.put("Sector2_3",R.drawable.sector2_3);
        backgrounds.put("Sector2_4",R.drawable.sector2_4);
    }
}
