package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class Sector1MapActivity extends RegionMapActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector1_map);
        regionName = "Sector1";
        context = getApplicationContext();
        init();
    }

    @Override
    protected void initGames() {
        super.initGames();
        games.put("Sector1_1","sudoku");
        games.put("Sector1_2","sudoku");
        games.put("Sector1_3","sudoku");
    }

    @Override
    protected void initBackgrounds() {
        super.initBackgrounds();
        backgrounds.put("Sector1_2",R.drawable.sector1_2);
        backgrounds.put("Sector1_3",R.drawable.sector1_3);
        backgrounds.put("Sector1_4",R.drawable.sector1_4);
    }
}
