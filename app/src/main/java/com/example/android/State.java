package com.example.android;

public class State {

    private String name; // название
    private String capital;  // столица
    private int flagResource; // ресурс флага
    private boolean check;

    public State(String name, int flag,boolean check){

        this.name=name;
        //this.capital=capital;
        this.flagResource=flag;
        this.check = check;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Boolean getCheck(){return this.check;}



    public int getFlagResource() {
        return this.flagResource;
    }

    public void setFlagResource(int flagResource) {
        this.flagResource = flagResource;
    }

    public void setCheck(boolean check){ this.check = check;}

}
