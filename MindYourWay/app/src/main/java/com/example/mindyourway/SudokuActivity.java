package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SudokuActivity extends AppCompatActivity {

    Button buttonComplete;
    String checkpointString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        Intent intent = getIntent();
        checkpointString = intent.getStringExtra(CenterMapActivity.centerExtra);
        init();
    }

    private void init() {
        buttonComplete = (Button) findViewById(R.id.buttonComplete);
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.user.incrementStatus(checkpointString);
                if(checkpointString.contains("Center")) {
                    Intent intent = new Intent(SudokuActivity.this, CenterMapActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
