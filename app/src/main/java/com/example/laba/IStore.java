package com.example.laba;

import android.widget.ListView;

import java.util.ArrayList;

public interface IStore {
    void PrintData(ArrayList<String> dataCh, ListView list);

    void Clear();

    void RemoveStudent(String id);

    void UpdateStudent(String product);

    ArrayList<String> getStudents();

    void InsertStudent(String product);

    void InsertStudents(ArrayList<String> dataCh);

    void Print();
}
