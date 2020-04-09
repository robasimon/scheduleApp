package com.example.scheduleapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.*;

    // Stores a particular week of shifts (for a single employee)
    public class WeekShifts {
        public final static int DAYS_IN_WK = 7;

        // Ensure capacity for each day of the week
        private ArrayList<List<Shift>> shifts =
                new ArrayList<List<Shift>>(DAYS_IN_WK);

        private void initDays() {
            for (int i = 0; i < DAYS_IN_WK; ++i)
                shifts.add(new LinkedList<Shift>());
        }

        public WeekShifts() {initDays();}

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void addShift(Shift s) {
            List<Shift> dayRef = shifts.get(s.getStart().getDayOfWeek().getValue()
                                                    % (DAYS_IN_WK-1));
            dayRef.add(s);


            // Sort new list chronologically
            Collections.sort(dayRef, new ShiftComparator());

            // Check for overlap
            for (int i = 1; i < dayRef.size(); ++i)
                if(Shift.shiftsOverlap(dayRef.get(i), dayRef.get(i-1))) {
                    // Since overlap check is called each time a new shift is added. We
                    // know that s is causing the conflict and should be removed.
                    dayRef.remove(s);
                    System.out.println("Shift: \n");
                    s.printShift();
                    System.out.println("\nconflicts with another shift and cannot" +
                                               " be added for employee.");
                }
        }

        // Note days go from 0-6, Sunday-Saturday
        public Shift getShift(int d, int i) {
            return shifts.get(d).get(i);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void removeShift(Shift s) {
            if (shifts.get(s.getStart().getDayOfWeek().getValue()
                                   % (DAYS_IN_WK-1)).contains(s))
                shifts.get(s.getStart().getDayOfWeek().getValue()
                                   % (DAYS_IN_WK-1)).remove(s);
            else
                System.out.println("Shift not found");
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void printShifts() {
            for (List<Shift> day : shifts)
                for (Shift shift : day)
                    shift.printShift();
        }

        // Note days go from 0-6, Sunday-Saturday
        public void removeAllShifts(int day) {
            shifts.get(day).clear();
        }
    }


