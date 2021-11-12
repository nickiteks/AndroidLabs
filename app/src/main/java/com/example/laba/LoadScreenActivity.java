package com.example.laba;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class LoadScreenActivity extends AppCompatActivity {

    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);
        Button btnToMain = findViewById(R.id.btnToMain);
        btnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if (count == 5) {
                    Intent intent = new Intent(LoadScreenActivity.this, MainActivity.class);                //поиск
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    startActivity(intent);
                    //finish();}
                }
            }
        });
    }
}