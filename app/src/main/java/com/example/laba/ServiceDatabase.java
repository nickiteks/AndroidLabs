package com.example.laba;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ListView;

import java.util.ArrayList;

public class ServiceDatabase extends Service {
    public Context con;
    DatabaseWork db;
    LocalService binder = new LocalService();

    @Override
    public IBinder onBind(Intent intent) {
        con = getApplicationContext();
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createDB();
    }

    private DatabaseWork createDB() {
        db = new DatabaseWork(getApplicationContext());
        return db;
    }

    public IStore getIStore() {
        return createDB();
    }

    public void PrintData(ArrayList<String> dataCh, ListView list) {
        db.PrintData(dataCh, list);
    }

    public void Clear() {
        db.Clear();
    }

    public void RemoveStudent(String id) {
        db.RemoveStudent(id);
    }

    public void UpdateStudent(String student) {
        db.UpdateStudent(student);
    }

    public ArrayList<String> getListStudent() {
        return db.getStudents();
    }

    public void InsertStudent(String student) {
        db.InsertStudent(student);
    }

    public void InsertStudents(ArrayList<String> dataCh) {
        db.InsertStudents(dataCh);
    }

    public void Print() {
        db.Print();
    }

    class LocalService extends Binder {
        ServiceDatabase getService() {
            return ServiceDatabase.this;
        }
    }
}