package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Console;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final ArrayList<String> names = new ArrayList<>();

    Button btnADD;
    ListView lvMain;
    EditText textField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // прочтение компонентов
        ListView listView = (ListView) findViewById(R.id.lvMain);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final EditText editText = (EditText) findViewById(R.id.addTextField);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        Button btnSelectAll = (Button) findViewById(R.id.btnSelectALL);
        Button btnClearSelection = (Button) findViewById(R.id.btnClear);
        Button btnTOST = (Button) findViewById(R.id.btnTOST);

        // список
        final ArrayList<String> catNames = new ArrayList<>();


        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, catNames);

        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                catNames.add(editText.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0;i< listView.getCount();i++){
                    listView.setItemChecked(i,true);
                }
            }
        });

        btnClearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0;i< listView.getCount();i++){
                    listView.setItemChecked(i,false);
                }
            }
        });

        btnTOST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}