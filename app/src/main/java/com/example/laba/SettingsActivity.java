package com.example.laba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button bF = findViewById(R.id.buttonFile);
        Button bD = findViewById(R.id.buttonDB);

        bF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("result", "file");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        bD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("result", "db");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}