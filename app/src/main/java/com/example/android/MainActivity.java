package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
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
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private ArrayAdapter mAdapter;
    ArrayList<String> catNames;
    private Context mContext;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            Locale locale = new Locale("en");
            conf.setLocale(locale);
            res.updateConfiguration(conf, dm);

        } else {

        }
        setContentView(R.layout.activity_main);

        // прочтение компонентов
        ListView listView = (ListView) findViewById(R.id.lvMain);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final EditText editText = (EditText) findViewById(R.id.addTextField);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        Button btnSelectAll = (Button) findViewById(R.id.btnSelectALL);
        Button btnClearSelection = (Button) findViewById(R.id.btnClear);
        Button btnTOST = (Button) findViewById(R.id.btnTOST);


        catNames = new ArrayList<>();

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, catNames) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position, convertView, parent);

                // Set the typeface/font for the current item
                item.setTypeface(mTypeface);

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#FF3E80F1"));

                // Set the item text style to bold
                item.setTypeface(item.getTypeface(), Typeface.BOLD);

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                // return the view
                return item;
            }
        };

        // Data bind the list view with array adapter items
        listView.setAdapter(mAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                catNames.add(editText.getText().toString());
                mAdapter.notifyDataSetChanged();
            }
        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < listView.getCount(); i++) {
                    listView.setItemChecked(i, true);
                }
            }
        });

        btnClearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < listView.getCount(); i++) {
                    listView.setItemChecked(i, false);
                }
            }
        });

        btnTOST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "";
                for (int i = 0;i<listView.getCount();i++){
                    if(listView.isItemChecked(i)){
                        text += mAdapter.getItem(i) + " ";
                    }
                }
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                if (text == ""){
                    text = "Nothing found";
                }
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    protected void onRestoreInstanceState(Bundle outState) {
        if (outState != null) {
            catNames = (ArrayList<String>) outState.getStringArrayList("myKey");
            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, catNames) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Cast the list view each item as text view
                    TextView item = (TextView) super.getView(position, convertView, parent);

                    // Set the typeface/font for the current item
                    item.setTypeface(mTypeface);

                    // Set the list view item's text color
                    item.setTextColor(Color.parseColor("#FF3E80F1"));

                    // Set the item text style to bold
                    item.setTypeface(item.getTypeface(), Typeface.BOLD);

                    // Change the item text size
                    item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    // return the view
                    return item;
                }
            };
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.setLocale(new Locale("en"));
                res.updateConfiguration(conf, dm);

            } else {

            }
            ListView listView = (ListView) findViewById(R.id.lvMain);
            listView.setAdapter(mAdapter);
        }
        super.onRestoreInstanceState(outState);
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("myKey", catNames);
        super.onSaveInstanceState(outState);
    }

    public static <T> Collection<T> getCheckedItems(ListView listView) {
        Collection<T> ret = new ArrayList();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            if (checkedItemPositions.valueAt(i)) {
                T item = (T) listView.getAdapter().getItem(checkedItemPositions.keyAt(i));
                ret.add(item);
            }
        }
        return ret;
    }

}