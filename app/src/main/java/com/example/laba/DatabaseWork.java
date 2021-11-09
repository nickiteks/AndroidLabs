package com.example.laba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseWork extends SQLiteOpenHelper implements IStore {
    // Имя файла базы данных
    private static final String DATABASE_NAME = "list_of_students.db";

    //Версия базы данных. При изменении схемы увеличить на единицу
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase database;
    Context con;

    public DatabaseWork(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        con = context;
    }

    //Вызывается при создании базы данных
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists students "
                + "("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "count text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void InsertStudents(ArrayList<String> data) {
        if (!database.isOpen())
            database = this.getWritableDatabase();
        else if (database.isReadOnly())
            database = this.getWritableDatabase();
        Clear();

        for (int i = 0; i < data.size(); i++) {
            InsertStudent(data.get(i));
        }
    }

    public void InsertStudent(String student) {
        if (!database.isOpen())
            database = this.getWritableDatabase();
        else if (database.isReadOnly())
            database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", student.split(";")[1]);
        cv.put("count", student.split(";")[2]);
        long rowId = database.insert("students", null, cv);
    }

    public ArrayList<String> getStudents() {
        if (!database.isOpen())
            database = this.getWritableDatabase();

        ArrayList<String> students = new ArrayList<>();
        Cursor c = database.query("students", null, null, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            if (!c.isAfterLast()) {
                do {
                    int index_id = c.getColumnIndex("id");
                    int index_name = c.getColumnIndex("name");
                    int index_count = c.getColumnIndex("count");
                    String id = c.getString(index_id);
                    String name = c.getString(index_name);
                    String count = c.getString(index_count);
                    String val = id + ";" + name + ";" + count;
                    students.add(val);
                }
                while (c.moveToNext());
            }
        }
        return students;
    }

    public void UpdateStudent(String data) {
        if (!database.isOpen())
            database = this.getWritableDatabase();
        else if (database.isReadOnly())
            database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", data.split(";")[1]);
        cv.put("count", data.split(";")[2]);

        database.update("students", cv, "id = ?",
                new String[]{data.split(";")[0]});
    }

    public void RemoveStudent(String id) {
        ArrayList<String> students = getStudents();
        int index = 0;
        for (int i = 0; i < students.size(); i++) {
            String ID = students.get(i).split(";")[0];
            if (ID == id) {
                index = i;
                return;
            }
        }
        database.delete("students", "id= ?", new String[]{id});
    }

    public void Clear() {
        database.delete("students", null, null);
        database.execSQL("create table if not exists students "
                + "("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "count text" + ");");
    }

    public void PrintData(ArrayList<String> dataCh, ListView list) {
        // создаем адаптер
        ArrayList<String> data = getStudents();
        ArrayList<String> buf = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            buf.add(String.valueOf(Integer.parseInt(data.get(i).split(";")[0]))
                    + "              " + data.get(i).split(";")[1]
                    + "              " + data.get(i).split(";")[2]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(con, R.layout.simple_list, buf);
        list.setAdapter(adapter);
        //устанавливаем множественный выбор
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void Print() {
        Toast.makeText(con, "DB", Toast.LENGTH_SHORT).show();
    }
}