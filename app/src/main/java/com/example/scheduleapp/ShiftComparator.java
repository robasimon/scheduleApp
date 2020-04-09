package com.example.scheduleapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Comparator;

class ShiftComparator implements Comparator<Shift> {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int compare(Shift a, Shift b) {
        return a.getStart().compareTo(b.getStart());
    }
}