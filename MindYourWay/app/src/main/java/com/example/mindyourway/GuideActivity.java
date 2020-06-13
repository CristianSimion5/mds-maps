package com.example.mindyourway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuideActivity extends AppCompatActivity {
    private Button buttonBack;
    private Button buttonSwitch;
    public boolean switchText = true;
    private TextViewWithImages textGuide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

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
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
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