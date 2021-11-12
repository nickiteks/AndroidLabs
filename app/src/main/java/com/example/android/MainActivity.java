package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<State> states = new ArrayList();
    ArrayAdapter<String> adapter;
    ArrayList<String> catNames;
    private Context nContext;
    private Typeface mTypeface;
    String search_word = "search";
    Fragment fragment = null;
    FragmentManager fm = getSupportFragmentManager();
    StateAdapter stateAdapter;
    final String LOG_TAG = "myLogs";
    DBHelper dbHelper;
    boolean isDatabase= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        Cursor c = null;
        if(isDatabase) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            c = db.query("mytable", null, null, null, null, null, null);
            while (c.moveToNext()) {
                states.add(new State(c.getString(1), c.getString(2), c.getInt(3), false));
            }
        } else {
            try {
                JSONObject object = new JSONObject(readJSON());
                JSONArray jsonArrayObject = object.getJSONArray("data");

                for (int i = 0; i < jsonArrayObject.length(); i++) {
                    states.add(new State(jsonArrayObject.getJSONObject(i).getString("TextA"),
                            jsonArrayObject.getJSONObject(i).getString("TextB"),
                            jsonArrayObject.getJSONObject(i).getInt("numbers"),
                            jsonArrayObject.getJSONObject(i).getBoolean("check")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // прочтение компонентов
        ListView listView = (ListView) findViewById(R.id.lvMain);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final EditText editText = (EditText) findViewById(R.id.addTextField);
        final EditText searchText = (EditText) findViewById(R.id.searchText);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        Button btnSelectAll = (Button) findViewById(R.id.btnSelectALL);
        Button btnClearSelection = (Button) findViewById(R.id.btnClear);
        Button btnTOST = (Button) findViewById(R.id.btnTOST);
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);


        catNames = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,catNames){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                // Set the typeface/font for the current item
                item.setTypeface(mTypeface);

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#DDADAF"));

                // Set the item text style to bold
                item.setTypeface(item.getTypeface(), Typeface.BOLD);

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);

                // return the view
                return item;
            }
        };

        stateAdapter = new StateAdapter(this, R.layout.list_item, states);

        listView.setAdapter(stateAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                cv.put("first_name",editText.getText().toString());
                cv.put("last_name",editText.getText().toString());
                cv.put("number",1);

                db.insert("mytable", null, cv);

                stateAdapter.add(new State (editText.getText().toString(),editText.getText().toString(), 1,false));
                stateAdapter.notifyDataSetChanged();
            }
        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0;i< listView.getCount();i++){
                    stateAdapter.getItem(i).setCheck(true);
                    stateAdapter.notifyDataSetChanged();
                }
            }
        });

        btnClearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0;i< listView.getCount();i++){
                    stateAdapter.getItem(i).setCheck(false);
                    stateAdapter.notifyDataSetChanged();
                }
            }
        });

        btnTOST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "";
                for (int i = 0;i<listView.getCount();i++){
                    if(stateAdapter.getItem(i).getCheck()){
                        text += stateAdapter.getItem(i).getName() + " ";
                    }
                }
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_word = searchText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (c.getCount() == 0) {
            return;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void Change(View view){
        switch (view.getId()){
            case R.id.btnEdit:
                ListView listView = (ListView) findViewById(R.id.lvMain);
                if (listView.getCheckedItemCount() == 1){
                    for (int i = 0; i < listView.getCount();i++){
                        if (listView.isItemChecked(i)){
                            fragment = new EditFragment(stateAdapter.getItem(i).getName().toString());
                        }
                    }
                }
                else{
                    fragment = new EditFragment(" ");
                }
                break;
            case R.id.btnDel:
                fragment = new DeleteFragment();
                break;
        }

        FragmentTransaction ft = fm.beginTransaction();
        assert fragment != null;
        ft.replace(R.id.fr_place,fragment);
        ft.commit();
    }

    protected void onRestoreInstanceState(Bundle outState) {
        if (outState != null) {
            catNames = (ArrayList<String>) outState.getStringArrayList("myKey");
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,catNames){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    // Cast the list view each item as text view
                    TextView item = (TextView) super.getView(position,convertView,parent);

                    // Set the typeface/font for the current item
                    item.setTypeface(mTypeface);

                    // Set the list view item's text color
                    item.setTextColor(Color.parseColor("#DDADAF"));

                    // Set the item text style to bold
                    item.setTypeface(item.getTypeface(), Typeface.BOLD);

                    // Change the item text size
                    item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);

                    // return the view
                    return item;
                }
            };
            ListView listView = (ListView) findViewById(R.id.lvMain);
            listView.setAdapter(adapter);
        }
        super.onRestoreInstanceState(outState);
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("myKey", catNames);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                Intent intent = new Intent(this, SearchActivity.class);
                //поиск
                ArrayList<String> search = new ArrayList<>();
                for(State state : states){
                    if (state.getName().contains(search_word)){
                        search.add(state.getName());
                    }
                }
                intent.putExtra("MyClass", search);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    public void dataFromFragment(String data) {
        ListView listView = (ListView) findViewById(R.id.lvMain);
        for(int i = 0;i<listView.getCount();i++){
            if(stateAdapter.getItem(i).getCheck()){
                stateAdapter.getItem(i).setName(data);
            }
        }
        stateAdapter.notifyDataSetChanged();
        fm.beginTransaction().hide(fragment).commit();
    }

    public void dataFromDelFragment(Boolean choose){
        ListView listView = (ListView) findViewById(R.id.lvMain);
        if(choose == Boolean.TRUE){

            ArrayList<String> checked = new ArrayList<String>();
            for(int i = 0; i<states.size();i++) {
                if (stateAdapter.getItem(i).getCheck()) {
                    checked.add(stateAdapter.getItem(i).getName());
                }
            }

            for(int i = 0; i < checked.size();i++){
                for(int j = 0; j < states.size();j++){
                    if(stateAdapter.getItem(j).getName().equals(checked.get(i))){
                        stateAdapter.remove(states.get(j));
                    }
                }
            }

            // снимаем все ранее установленные отметки
            for(int i = 0;i< listView.getCount();i++){
                stateAdapter.getItem(i).setCheck(false);
                stateAdapter.notifyDataSetChanged();
            }
            // очищаем массив выбраных объектов
            stateAdapter.notifyDataSetChanged();
        }
        fm.beginTransaction().hide(fragment).commit();
    }

    public String readJSON() {
        String json = null;
        try {
            // Opening data.json file
            InputStream inputStream = getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        return json;
    }

}