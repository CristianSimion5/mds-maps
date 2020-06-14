package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuideActivity extends AppCompatActivity {
    public static final String guideExtra = "com.example.MindYourWay.Guide.EXTRA_TEXT";

    private Button buttonBack;
    private Button buttonSwitch;
    public boolean switchText;
    private TextViewWithImages textGuide;
    private String origin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        Intent intent = getIntent();
        origin = intent.getStringExtra(GuideActivity.guideExtra);
        if(origin.equals("Main"))
            switchText = true;
        else
            switchText = false;


        buttonBack = (Button) findViewById(R.id.backButton);
        buttonSwitch = (Button) findViewById(R.id.buttonSwitch);
        textGuide = findViewById(R.id.textViewGuide);
        if(switchText) {
            textGuide.setText(R.string.tutorial);
            buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_next));
        } else {
            textGuide.setText(R.string.games);
            buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_back));
        }
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (origin.equals("Main")) {
                    intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    String[] strings = origin.split("_");
                    switch (strings[0]) {
                        case "Sudoku":
                            intent = new Intent(GuideActivity.this, SudokuActivity.class);
                            intent.putExtra(CenterMapActivity.regionExtra, strings[1]);
                            startActivity(intent);
                            break;
                        case "FindWord":
                            intent = new Intent(GuideActivity.this, FindWordActivity.class);
                            intent.putExtra(CenterMapActivity.regionExtra, strings[1]);
                            startActivity(intent);
                            break;
                        case "ColorLink":
                            intent = new Intent(GuideActivity.this, ColorLinkActivity.class);
                            intent.putExtra(CenterMapActivity.regionExtra, strings[1]);
                            startActivity(intent);
                            break;
                    }
                }
            }
        });
        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchText = !switchText;
                if(switchText) {
                    textGuide.setText(R.string.tutorial);
                    buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_next));
                } else {
                    textGuide.setText(R.string.games);
                    buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_back));
                }
            }
        });
    }
}