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

public class CenterMapActivity extends AppCompatActivity {

    public static final String centerExtra = "com.example.MindYourWay.Center.EXTRA_TEXT";
    private static final String TAG = "CenterMapActivity";
    //widgets
    private LinearLayout linearLayoutTitle;
    private Button buttonCheckpoint1;
    private Button buttonCheckpoint2;
    private Button buttonCheckpoint3;
    private ArrayList<Button> buttonCheckpointList;
    private ProgressBar progressBar;
    private TextView textCheckpointNumber;
    private TextView textCheckpointStatus;
    private TextView textCheckpointGame;
    private Button buttonReachLocation;
    private Button buttonPlayGame;
    private TextView textName;
    private TextView textLevel;
    private View centerMap;

    //vars
    private HashMap<String, String> games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_map);
        init();
    }

    void init() {
        initGames();
        buttonCheckpointList = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonCheckpoint1 = (Button) findViewById(R.id.buttonCheckpoint1);
        buttonCheckpoint2 = (Button) findViewById(R.id.buttonCheckpoint2);
        buttonCheckpoint3 = (Button) findViewById(R.id.buttonCheckpoint3);
//        linearLayoutTitle = (LinearLayout) findViewById(R.id.linearLayoutTitle);
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
        centerMap = findViewById(R.id.CenterMap);

        textName.setText(MainActivity.user.getName());
        textLevel.setText(String.valueOf(MainActivity.user.getLevel()));

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

    private void initProgress(){
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
        if(MainActivity.user.getStatus(tag) == 3) {
            button.setBackground(getResources().getDrawable(R.drawable.btncirclez));
            if(progress == 1) {
                centerMap.setBackground(getResources().getDrawable(R.drawable.center2));
            } else if (progress == 2) {
                centerMap.setBackground(getResources().getDrawable(R.drawable.center3));
            }
        }

        initCheckpoint(buttonCheckpointList.get(progress));
    }

    private void initGames(){
        games = new HashMap<>();
        games.put("Center_1","sudoku");
        games.put("Center_2","game2");
        games.put("Center_3","game3");
    }

    private void initCheckpoint(final Button button){
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
                    Intent intent = new Intent(CenterMapActivity.this, MapsActivity.class);
                    intent.putExtra(centerExtra, tag);
                    startActivity(intent);
                } else {
                    Toast.makeText(CenterMapActivity.this, "You can't go to this checkpoint yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.user.getStatus(tag)>=3) {
                    if (game.equals("sudoku")) {
                        Intent intent = new Intent(CenterMapActivity.this, SudokuActivity.class);
                        intent.putExtra(centerExtra, tag);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(CenterMapActivity.this, "You can't play the game yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getCheckpointStatus(String tag){
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
