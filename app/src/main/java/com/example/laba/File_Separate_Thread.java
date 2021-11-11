package com.example.laba;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class File_Separate_Thread implements IStore {
    public final String SAVED_TEXT = "saved_text.txt";
    Activity activity;

    public File_Separate_Thread(Activity activity) {
        this.activity = activity;
    }

    public void InsertStudents(ArrayList<String> dataCh) {
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sPref = activity.getPreferences(activity.MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                Set<String> data = new HashSet<String>(dataCh);
                ed.putStringSet(SAVED_TEXT, data);
                ed.commit();
                Toast.makeText(activity, "Студенты сохранены", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void InsertStudent(String student) {
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sPref = activity.getPreferences(activity.MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(SAVED_TEXT, student);
                ed.commit();
                Toast.makeText(activity, "Студент сохранен", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<String> getStudents() {
        SharedPreferences sPref = activity.getPreferences(activity.MODE_PRIVATE);
        try {

            Set<String> data = sPref.getStringSet(SAVED_TEXT, null);
            ArrayList<String> result = new ArrayList<>(data);
            return result;
        } catch (Exception ex) {
            ArrayList<String> result = new ArrayList<>();
            Toast.makeText(activity, "ex", Toast.LENGTH_SHORT).show();
            return result;
        }
    }

    public void UpdateStudent(String student) {
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sPref = activity.getPreferences(activity.MODE_PRIVATE);
                try {
                    Set<String> data = sPref.getStringSet(SAVED_TEXT, null);
                    ArrayList<String> students = new ArrayList<>(data);
                    int index = 0;
                    for (int i = 0; i < students.size(); i++) {
                        String ID = students.get(i).split(";")[0];
                        if (ID == student.split(";")[0]) {
                            index = i;
                            return;
                        }
                    }
                    students.remove(index);
                    students.add(index, student);
                    Clear();
                    InsertStudents(students);
                } catch (Exception ex) {
                    Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void RemoveStudent(String id) {
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sPref = activity.getPreferences(activity.MODE_PRIVATE);
                try {
                    Set<String> data = sPref.getStringSet(SAVED_TEXT, null);
                    ArrayList<String> students = new ArrayList<>(data);
                    int index = 0;
                    for (int i = 0; i < students.size(); i++) {
                        String ID = students.get(i).split(";")[0];
                        if (ID == id) {
                            index = i;
                            return;
                        }
                    }
                    students.remove(index);
                    Clear();
                    InsertStudents(students);
                } catch (Exception ex) {
                    Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void Clear() {
        try {
            FileWriter fstream1 = new FileWriter(SAVED_TEXT);// конструктор с одним параметром - для перезаписи
            BufferedWriter out1 = new BufferedWriter(fstream1); //  создаём буферезированный поток
            out1.write(""); // очищаем, перезаписав поверх пустую строку
            out1.close(); // закрываем
            fstream1.close();
        } catch (Exception e) {
            System.err.println("Error in file cleaning: " + e.getMessage());
        }
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.simple_list, buf);
        list.setAdapter(adapter);

        //устанавливаем множественный выбор
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void Print() {
        Toast.makeText(activity, "File", Toast.LENGTH_SHORT).show();
    }
}