package com.example.android;

import java.io.Serializable;

public class State {

    private String name; // название
    private String capital;  // столица
    private int flagResource; // ресурс флага
    private boolean check;

    public State(String name, String capital, int flag,boolean check){

        this.name=name;
        this.capital=capital;
        this.flagResource=flag;
        this.check = check;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return this.capital;
    }

    public Boolean getCheck(){return this.check;}

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public int getFlagResource() {
        return this.flagResource;
    }

    public void setFlagResource(int flagResource) {
        this.flagResource = flagResource;
    }

    public void setCheck(boolean check){ this.check = check;}

}
