package com.example.scheduleapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Employee {
    //public final static int WEEKS_IN_YR = 52, UNINITIALIZED = -1;
    //private static int numEmployees = 0;
    // Map of all employee ids->names. Used to help prevent id-collisions
    // while providing quick name lookup when id is known.
    //private static Map<String,String> idToName = new HashMap<>();
    private String name;
    private String email;
    private String phoneNum;
    private String id;
    public Employee(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

/*
    private int firstYear = UNINITIALIZED;
    // ArrayList of Lists of shifts used since each day likely only has a
    // couple of shifts so O(n) complexity of list access is not a problem
    // and shifts are quickly inserted/removed.
    private ArrayList<WeekShifts> shifts =
            new ArrayList<WeekShifts>(WEEKS_IN_YR);

    private void initWeeks() {
        for (int i = 0; i < WEEKS_IN_YR; ++i)
            shifts.add(new WeekShifts());
    }

    static public int getNumEmployees() {
        return numEmployees;
    }

    static public String getEmployeeName(long id) {
        return idToName.get(id);
    }

    // Constructors
    public Employee() {++numEmployees; initWeeks();}
    public Employee(String name, String id) {
        ++numEmployees;
        initWeeks();
        if (idToName.containsKey(id))
            throw new IllegalArgumentException("Non-unique id value entered.");
        idToName.put(id, name);
        this.name = name;
        this.id = id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addShift(Shift s) {
        if (firstYear == UNINITIALIZED)
            firstYear = s.getShiftYear();

        shifts.get(s.getShiftWeek()-1).addShift(s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void printShifts() {
        int i = 0;
        for (WeekShifts s : shifts)
            // Note s.printShifts() here refers to WeekShifts.printShifts(),
            // not a recursive call to Employee.printShifts()
            s.printShifts();
    }*/
}