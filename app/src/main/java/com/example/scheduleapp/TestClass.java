package com.example.scheduleapp;

public class TestClass {

    String employeeName;
    String startTime;
    String dateSelect;
    String keydoes;

    public TestClass() {
    }

    public TestClass(String employeeName, String startTime, String dateSelect, String keydoes) {
        this.employeeName = employeeName;
        this.startTime = startTime;
        this.dateSelect = dateSelect;
        this.keydoes = keydoes;
    }

    public String getKeydoes() {
        return keydoes;
    }

    public void setKeydoes(String keydoes) {
        this.keydoes = keydoes;
    }

    public String getemployeeName() {
        return employeeName;
    }

    public void setemployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getstartTime() {
        return startTime;
    }

    public void setstartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getdateSelect() {
        return dateSelect;
    }

    public void setdateSelect(String dateSelect) {
        this.dateSelect = dateSelect;
    }
}
