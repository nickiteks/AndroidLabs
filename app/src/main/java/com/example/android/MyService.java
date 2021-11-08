package com.example.android;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

public class MyService extends Service {
    ArrayList<State> states = new ArrayList();
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** method for clients */
    public ArrayList<State> getData() {
        DBHelper dbHelper;
        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        while (c.moveToNext()){
            states.add(new State(c.getString(1),c.getString(2),c.getInt(3),false));
        }
        return states;
    }

}