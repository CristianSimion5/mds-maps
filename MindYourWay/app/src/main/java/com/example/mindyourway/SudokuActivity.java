package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SudokuActivity extends AppCompatActivity {

    private static final String TAG = "SudokuActivity";

    private Button buttonComplete;
    private String checkpointString;
    private Button[][] buttonsSudoku = new Button[9][9];
    private Button[] buttonsDigit = new Button[9];;
    private Button buttonEmpty;
    private Button buttonBack;

    private int xCurrent = 1;
    private int yCurrent = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        Intent intent = getIntent();
        checkpointString = intent.getStringExtra(CenterMapActivity.regionExtra);
        Log.d(TAG, "onCreate: "+checkpointString);
        init();
    }

    private void init() {
        buttonComplete = (Button) findViewById(R.id.buttonComplete);
        buttonEmpty = (Button) findViewById(R.id.buttonEmpty) ;
        buttonBack = (Button) findViewById(R.id.buttonBack);
        final Sudoku sudoku;

        if(MainActivity.user.checkSudokuGame(checkpointString))
            sudoku = new Sudoku(MainActivity.user.getSudokuGame(checkpointString));
        else {
            int difficulty = 0;
            if (checkpointString.contains("Center")) {
                difficulty = 35;
            } else if (checkpointString.contains("Sector1") || checkpointString.contains("Sector3")) {
                difficulty = 45;
            } else if (checkpointString.contains("Sector2") || checkpointString.contains("Sector4")) {
                difficulty = 55;
            }

            int i = Math.abs(ThreadLocalRandom.current().nextInt()) % 9;
            int j =  Math.abs(ThreadLocalRandom.current().nextInt()) % 9;
            int val =  Math.abs(ThreadLocalRandom.current().nextInt()) % 9;
            sudoku = new Sudoku(i, j, val, difficulty);
        }

        final int[][] table = sudoku.getTable();

        for (int i = 0;i<9;i++){
            for(int j=0;j<9;j++){
                //Log.d(TAG, "init: "+String.valueOf(table[i][j]));
            }
        }

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                String stringID = "buttonSudoku" + String.valueOf(i+1) + String.valueOf(j+1);
                int ID = SudokuActivity.this.getResources().getIdentifier(stringID, "id", SudokuActivity.this.getPackageName());
                buttonsSudoku[i][j] = (Button) findViewById(ID);
                if(table[i][j]!=0) {
                    String string = String.valueOf(table[i][j]);
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new StyleSpan(Typeface.BOLD), 0, string.length(), 0);
                    Log.d(TAG, "init: "+builder);
                    buttonsSudoku[i][j].setText(string);
                   // buttonsSudoku[i][j].setText("");

                } else {
                    buttonsSudoku[i][j].setText("");
                }
                final int finalI = i;
                final int finalJ = j;
                buttonsSudoku[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xCurrent= finalI;
                        yCurrent= finalJ;
                    }
                });
            }
        }

        for(int i = 0; i < 9; i++) {
            String string = "buttonDigit" + String.valueOf(i+1);
            int ID = SudokuActivity.this.getResources().getIdentifier(string, "id", SudokuActivity.this.getPackageName());
            buttonsDigit[i] = (Button) findViewById(ID);
            final int finalI = i;
            buttonsDigit[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int digit = Integer.parseInt(buttonsDigit[finalI].getText().toString());
                    buttonsSudoku[xCurrent][yCurrent].setText(String.valueOf(digit));
                    if(sudoku.checkElement(xCurrent, yCurrent, digit)) {
                        Log.d(TAG, "onClick: 1");
                        MainActivity.user.setSudokuGame(checkpointString, sudoku.getCurrentState());
                        table[xCurrent][yCurrent]=digit;
                        sudoku.setElement(xCurrent, yCurrent,digit);
                        buttonsSudoku[xCurrent][yCurrent].setTextColor(getApplication().getResources().getColor(R.color.black));
                        if(sudoku.checkIfSolved()){
                            buttonComplete.performClick();
                        }
                    } else {
                        buttonsSudoku[xCurrent][yCurrent].setTextColor(getApplication().getResources().getColor(R.color.red));
                    }
                }
            });
        }

        buttonEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsSudoku[xCurrent][yCurrent].setText("");
                MainActivity.user.setSudokuGame(checkpointString, sudoku.getCurrentState());
                table[xCurrent][yCurrent]=0;
                sudoku.setElement(xCurrent, yCurrent,0);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpointString.contains("Center")) {
                    Intent intent = new Intent(SudokuActivity.this, CenterMapActivity.class);
                    startActivity(intent);
                }
                else if(checkpointString.contains("Sector1")) {
                    Intent intent = new Intent(SudokuActivity.this, Sector1MapActivity.class);
                    startActivity(intent);
                }
                else if(checkpointString.contains("Sector2")) {
                    Intent intent = new Intent(SudokuActivity.this, Sector2MapActivity.class);
                    startActivity(intent);
                }
                else if(checkpointString.contains("Sector3")) {
                    Intent intent = new Intent(SudokuActivity.this, Sector3MapActivity.class);
                    startActivity(intent);
                }
                else if(checkpointString.contains("Sector4")) {
                    Intent intent = new Intent(SudokuActivity.this, Sector4MapActivity.class);
                    startActivity(intent);
                }
            }
        });

        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sudoku.checkIfSolved() || MainActivity.user.isAdmin()){
                    if(MainActivity.user.getStatus(checkpointString)==3)
                        MainActivity.user.incrementStatus(checkpointString);
                    buttonBack.performClick();

                } else {
                    Toast.makeText(SudokuActivity.this, "Incorrect solution", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
