package com.example.laba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    String type_store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();

        Bundle arguments = intent.getExtras();

        String value = arguments.get("data").toString();
        type_store = arguments.get("type_store").toString();

        TextInputEditText text = findViewById(R.id.text);

        System.out.println(value);
        text.setText(value.split(";")[1]);
        ArrayList<String> data = new ArrayList<String>();
        data.add(value);
        // находим список
        ListView list = findViewById(R.id.list);
        PrintData(data, list);
    }

    public void PrintData(ArrayList<String> dataCh, ListView list) {
        // создаем адаптер
        ArrayList<String> buf = new ArrayList<String>();
        for (int i = 0; i < dataCh.size(); i++) {
            System.out.println("PRINT: " + dataCh.get(i));
            buf.add(String.valueOf(Integer.parseInt(dataCh.get(i).split(";")[0]))
                    + "              " + dataCh.get(i).split(";")[1]
                    + "              " + dataCh.get(i).split(";")[2]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list, buf);
        list.setAdapter(adapter);
        //устанавливаем множественный выбор
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}