package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    ArrayAdapter<String> adapter;
    ArrayList<String> catNames;
    private Context nContext;
    private Typeface mTypeface;
    String search_word = "search";
    Fragment fragment = null;
    FragmentManager fm = getSupportFragmentManager();
    ArrayList<String> selectedUsers = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                CharSequence text = "";
                for (int i = 0;i<listView.getCount();i++){
                    if(listView.isItemChecked(i)){
                        text += adapter.getItem(i) + " ";
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                // получаем нажатый элемент
                String user = adapter.getItem(position);
                if(listView.isItemChecked(position))
                    selectedUsers.add(user);
                else
                    selectedUsers.remove(user);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void Change(View view){
        switch (view.getId()){
            case R.id.btnEdit:
                ListView listView = (ListView) findViewById(R.id.lvMain);
                if (listView.getCheckedItemCount() == 1){
                    for (int i = 0; i < listView.getCount();i++){
                        if (listView.isItemChecked(i)){
                            fragment = new EditFragment(adapter.getItem(i).toString());
                        }
                    }
                }
                else{
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Выберите элементы списка!", duration);
                    toast.show();
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
                for (String item : catNames){
                    if(item.contains(search_word)){
                       search.add(item);
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
            if(listView.isItemChecked(i)){
                catNames.set(i, data);
            }
        }
        adapter.notifyDataSetChanged();
        fm.beginTransaction().hide(fragment).commit();
    }

    public void dataFromDelFragment(Boolean choose){
        ListView listView = (ListView) findViewById(R.id.lvMain);
        if(choose == Boolean.TRUE){
            for(int i=0; i< selectedUsers.size();i++){
                adapter.remove(selectedUsers.get(i));
            }
            // снимаем все ранее установленные отметки
            listView.clearChoices();
            // очищаем массив выбраных объектов
            selectedUsers.clear();
            adapter.notifyDataSetChanged();
        }
        fm.beginTransaction().hide(fragment).commit();
    }

}