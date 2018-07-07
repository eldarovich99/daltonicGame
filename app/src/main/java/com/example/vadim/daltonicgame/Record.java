package com.example.vadim.daltonicgame;

public class Record {
    private String name;
    private String date;
    private int result;

    public Record(String name, String date, int result) {
        this.name = name;
        this.date = date;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
