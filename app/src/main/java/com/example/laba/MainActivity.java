package com.example.laba;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ActivityInterractor {
    FragmentChange fragmentChange;
    MainFragment mainFragment;
    FragmentDelete delFragment;
    MyWidget1 widget;
    String store;
    public static FragmentManager myFragmentManager;
    FragmentTransaction fTrans;
    ArrayList<String> data;
    public ListView list;
    private static final String FILE_NAME = "result.json";
    private static final String FILE_NAME_RESULT = "result.json";
    private static final int SHOW_SUBACTIVITY = 1;
    public IStore Store;
    public ServiceDatabase servicedb;
    private boolean isBound;
    Intent intentService;

    // Здесь необходимо создать соединение с сервисом
    ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // используем mService экземпляр класса для доступа к публичному LocalService
            ServiceDatabase.LocalService localService = (ServiceDatabase.LocalService) service;
            servicedb = localService.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        intentService = new Intent(this, ServiceDatabase.class);
        bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
        delFragment = new FragmentDelete();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, SHOW_SUBACTIVITY);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_dynamic_land);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
        }
        fragmentChange = new FragmentChange();
        mainFragment = new MainFragment();


        //обработка нажатия на кнопку
        Button bAdd = findViewById(R.id.buttonAdd);
        Button bS = findViewById(R.id.buttonSel);
        Button bNS = findViewById(R.id.buttonNoSel);
        Button bP = findViewById(R.id.buttonPrint);
        Button bSearch = findViewById(R.id.buttonSearch);
        Button bChange = findViewById(R.id.buttonChange);
        Button bDel = findViewById(R.id.buttonDel);
        Button bLoad = findViewById(R.id.buttonLoad1);
        Button bSave = findViewById(R.id.buttonSave);

        // находим список
        list = findViewById(R.id.list);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragmentManager = getFragmentManager();
                fTrans = myFragmentManager.beginTransaction();
                mainFragment.servicedb = servicedb;
                mainFragment.isBound = isBound;
                fTrans.replace(R.id.containerChange, mainFragment);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        bS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setItemChecked((data.size() - 1), true);
            }
        });

        bNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setItemChecked((data.size() - 1), false);
            }
        });

        bP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = "Вы выбрали: ";
                // получим булев массив для каждой позиции списка
                // Объект SparseBooleanArray содержит массив значений, к которым можно получить доступ
                // через valueAt(index) и keyAt(index)
                SparseBooleanArray chosen = list.getCheckedItemPositions();
                for (int i = 0; i < chosen.size(); i++) {
                    // если пользователь выбрал пункт списка,
                    // то выводим его в TextView.
                    if (chosen.valueAt(i)) {
                        result += data.get(chosen.keyAt(i)).split(";")[1] + "\n";
                    }
                }
                if (chosen.size() > 0)
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
            }
        });

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText text = findViewById(R.id.text);

                if (text.getText().toString().length() > 0) {
                    String value = search(data, text.getText().toString());
                    if (value != "non") {
                        Intent i = new Intent(MainActivity.this, MainActivity2.class);
                        i.putExtra("data", value);
                        i.putExtra("type_store", store);
                        startActivity(i);
                        text.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "Нет такого значения", Toast.LENGTH_LONG).show();
                        text.setText("");
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
            }
        });

        bChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragmentManager = getFragmentManager();
                fTrans = myFragmentManager.beginTransaction();
                fragmentChange.servicedb = servicedb;
                fragmentChange.isBound = isBound;
                fTrans.replace(R.id.containerChange, fragmentChange);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        bDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.remove((data.size() - 1));
                int index = data.size() - 1;
                Store.RemoveStudent(data.get(index).split(";")[0]);

                fTrans = getFragmentManager().beginTransaction();
                delFragment.servicedb = servicedb;
                delFragment.isBound = isBound;
                fTrans.replace(R.id.containerChange, delFragment);
                fTrans.addToBackStack(null);
                fTrans.commit();
                Store.PrintData(data, list);
            }
        });
        bLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler h = new Handler();
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Store.Clear();
                        ArrayList<Student> dataJSON = ReadJSON();
                        System.out.println(dataJSON.size());

                        ArrayList<String> bufData = new ArrayList<String>();
                        for (int i = 0; i < dataJSON.size(); i++) {
                            System.out.println(dataJSON.get(i));
                            bufData.add(dataJSON.get(i).foDataStr());
                        }
                        Store.InsertStudents(bufData);
                    }
                });
                Toast.makeText(getApplicationContext(), "All OK!!!!", Toast.LENGTH_SHORT).show();
                ArrayList<String> bufData = Store.getStudents();
                Store.PrintData(bufData, list);
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler h = new Handler();
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Student> students = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            String[] buf = data.get(i).split(";");
                            System.out.println("SAVE: " + data.get(i));
                            Student p = new Student(Integer.parseInt(buf[0]), buf[1], buf[2]);
                            students.add(p);
                        }
                        boolean result = SaveJSON(students);
                        if (result)
                            Toast.makeText(getApplicationContext(), "Сохранение прошло успешно", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            store = intent.getExtras().getString("result");
            Toast.makeText(getApplicationContext(), store, Toast.LENGTH_LONG).show();
            if (store.equals("file")) {
                System.out.println("File!!!");
                Store = new File_Separate_Thread(this);
                fragmentChange.type_store = "file";
                mainFragment.type_store = "file";
                delFragment.type_store = "file";

                data = Store.getStudents();
                Store.PrintData(data, list);
            } else if (store.equals("db")) {
                fragmentChange.type_store = "db";
                mainFragment.type_store = "db";
                delFragment.type_store = "db";

                if (!isBound) {
                    Toast.makeText(getApplicationContext(), "Not", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "DB work!!! YES!!!!", Toast.LENGTH_SHORT).show();
                data = servicedb.getListStudent();
                Store = servicedb.getIStore();
                Store.PrintData(null, list);
            }
        }
    }

    private String search(ArrayList<String> data, String text) {
        System.out.println(text);
        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i));
            System.out.println(data.get(i).split(";")[1]);
            if (data.get(i).contains(text)) {
                System.out.println("YESSSSSSSSSSSSSS");
                return data.get(i);
            }
        }
        return "non";
    }


    @Override
    public void onFragmentClosed() {
        data = Store.getStudents();
        Store.PrintData(data, list);
    }

    public ArrayList<Student> ReadJSON() {
        System.out.println("ReadJson");

        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        System.out.println("ROOT: " + Environment.getDownloadCacheDirectory().getAbsolutePath());
        try {
            fileInputStream = getApplicationContext().openFileInput(FILE_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
            return dataItems.getStudents();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean SaveJSON(ArrayList<Student> student) {
        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setStudents(student);
        String jsonString = gson.toJson(dataItems);
        FileOutputStream fileOutputStream = null;

        try {
            System.out.println("try");
            fileOutputStream = getApplicationContext().openFileOutput(FILE_NAME_RESULT,
                    getApplicationContext().MODE_PRIVATE);
            System.out.println("fileOutputStream");
            fileOutputStream.write(jsonString.getBytes());
            System.out.println("jsonString write");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private static class DataItems {
        private ArrayList<Student> students;

        ArrayList<Student> getStudents() {
            return students;
        }

        void setStudents(ArrayList<Student> students) {
            this.students = students;
        }
    }
}