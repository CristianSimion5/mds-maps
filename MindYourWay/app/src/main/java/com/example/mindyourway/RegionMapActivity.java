package com.example.mindyourway;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class RegionMapActivity extends AppCompatActivity {
    public static final String centerExtra = "com.example.MindYourWay.Center.EXTRA_TEXT";
    protected static final String TAG = "RegionMapActivity";
    protected static String regionName = "Region";
    protected Context context;

    //widgets
    protected LinearLayout linearLayoutTitle;
    protected Button buttonCheckpoint1;
    protected Button buttonCheckpoint2;
    protected Button buttonCheckpoint3;
    protected Button buttonBack;
    protected ArrayList<Button> buttonCheckpointList;
    protected ProgressBar progressBar;
    protected TextView textCheckpointNumber;
    protected TextView textCheckpointStatus;
    protected TextView textCheckpointGame;
    protected Button buttonReachLocation;
    protected Button buttonPlayGame;
    protected TextView textName;
    protected TextView textLevel;
    protected View regionMap;

    //vars
    protected HashMap<String, String> games;
    protected HashMap<String, Integer> backgrounds;

    protected void initGames(){
        games = new HashMap<>();
    }

    protected void initBackgrounds() {
        backgrounds = new HashMap<>();
    }

    protected void init() {
        initGames();
        initBackgrounds();
        buttonCheckpointList = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonCheckpoint1 = (Button) findViewById(R.id.buttonCheckpoint1);
        buttonCheckpoint2 = (Button) findViewById(R.id.buttonCheckpoint2);
        buttonCheckpoint3 = (Button) findViewById(R.id.buttonCheckpoint3);
        //linearLayoutTitle = (LinearLayout) findViewById(R.id.linearLayoutTitle);
        buttonCheckpointList.add(buttonCheckpoint1);
        buttonCheckpointList.add(buttonCheckpoint2);
        buttonCheckpointList.add(buttonCheckpoint3);
        buttonReachLocation = (Button) findViewById(R.id.buttonReachLocation);
        buttonPlayGame = (Button) findViewById(R.id.buttonPlayGame);
        textCheckpointNumber = (TextView) findViewById(R.id.textCheckpointNumber);
        textCheckpointStatus = (TextView) findViewById(R.id.textCheckpointStatus);
        textCheckpointGame = (TextView) findViewById(R.id.textCheckpointGame);
        textName = (TextView) findViewById(R.id.textName);
        textLevel = (TextView) findViewById(R.id.textLevel);
        regionMap = findViewById(R.id.RegionMap);

        textName.setText(MainActivity.user.getName());
        textLevel.setText(String.valueOf(MainActivity.user.getLevel()));

        buttonBack = (Button) findViewById(R.id.BackButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BucharestMapActivity.class);
                startActivity(intent);
            }
        });

        for (final Button button : buttonCheckpointList) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initCheckpoint(button);
                }
            });
        }
        initProgress();

    }

    protected void initProgress(){
        int progress = 0;
        for (Button button : buttonCheckpointList) {
            String tag = buttonCheckpointList.get(progress).getTag().toString();
            if(MainActivity.user.getStatus(tag) < 4)
                break;
            button.setBackground(getResources().getDrawable(R.drawable.btncircle));
            progress++;
        }

        progressBar.setProgress(progress);
        Button button = buttonCheckpointList.get(progress);
        String tag = button.getTag().toString();
        if(MainActivity.user.getStatus(tag) == 1) {
            MainActivity.user.incrementStatus(tag);
        }
        String string = new String(regionName);
        string+="_";
        if(MainActivity.user.getStatus(tag) == 3) {
            button.setBackground(getResources().getDrawable(R.drawable.btncirclez));
            if(progress == 0) {
                string+=String.valueOf(2);
                regionMap.setBackground(getResources().getDrawable(backgrounds.get(string)));
            }
            else if(progress == 1) {
                string+=String.valueOf(3);
                regionMap.setBackground(getResources().getDrawable(backgrounds.get(string)));
            } else if (progress == 2) {
                string+=String.valueOf(4);
                regionMap.setBackground(getResources().getDrawable(backgrounds.get(string)));
            }
        }

        initCheckpoint(buttonCheckpointList.get(progress));
    }

    protected void initCheckpoint(final Button button){
        final String tag = button.getTag().toString();
        Log.d(TAG, "initCheckpoint: "+tag);
        final int number = Character.getNumericValue(tag.charAt(tag.length()-1));
        final String status = getCheckpointStatus(tag);
        final String game = games.get(tag);

        textCheckpointNumber.setText(String.valueOf(number));
        textCheckpointStatus.setText(status);
        textCheckpointGame.setText(game);

        buttonReachLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.user.getStatus(tag)>=2) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra(centerExtra, tag);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "You can't go to this checkpoint yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.user.getStatus(tag)>=3) {
                    if (game.equals("sudoku")) {
                        Intent intent = new Intent(context, SudokuActivity.class);
                        intent.putExtra(centerExtra, tag);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(context, "You can't play the game yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    protected String getCheckpointStatus(String tag){
        int statusIndex = MainActivity.user.getStatus(tag);
        String status;
        switch (statusIndex){
            case 1:
                status =  "Checkpoint unavailable";
                break;
            case 2:
                status =  "Checkpoint not reached";
                break;
            case 3:
                status = "Checkpoint reached, game available";
                break;
            default:
                status = "Checkpoint completed";
        }
        return status;
    }
}
