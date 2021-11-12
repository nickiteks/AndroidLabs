package com.example.laba;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("Id")
    public int Id;
    @SerializedName("Name")
    public String Name;
    @SerializedName("Age")
    public String Age;

    public Student(int id, String name, String age) {
        Id = id;
        Name = name;
        Age = age;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    @Override
    public String toString() {
        return "Student [Id=" + Id + ", Name=" + Name + "]";
    }

    public String foDataStr() {
        return Id + ";" + Name + ";" + Age;
    }
}