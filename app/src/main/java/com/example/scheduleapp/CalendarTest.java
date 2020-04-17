package com.example.scheduleapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarTest extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public GregorianCalendar cal_month, cal_month_copy;
    private EventAdapter eventAdapter;
    private TextView tv_month;
    private Button addShift;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_calendar);
        addShift = findViewById(R.id.add_shift);
        addShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new AddShiftDialog();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        HomeCollection.date_collection_arr.add( new HomeCollection("2020-04-08" ,"Temp Name","Shift","Full day shift"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2020-04-08" ,"Temp Name","Shift","Full day shift"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2020-04-08" ,"Temp Name","Shift","Full day shift"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2020-03-08" ,"Temp Name","Shift","Full day shift"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2020-04-09" ,"Temp Name","Shift","Full day shift"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2020-03-15" ,"Temp Name","Shift","Full day shift"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2020-04-26" ,"Temp Name","Shift","Full day shift"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2020-03-08" ,"Temp Name","Shift","Full day shift"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2020-05-16" ,"Temp Name","Shift","Full day shift"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2020-05-09" ,"Temp Name","Shift","Full day shift"));



        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        eventAdapter = new EventAdapter(this, cal_month, HomeCollection.date_collection_arr);

        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));


        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 3&&cal_month.get(GregorianCalendar.YEAR)==2020) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(CalendarTest.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setPreviousMonth();
                    refreshCalendar();
                }


            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 5&&cal_month.get(GregorianCalendar.YEAR)==2020) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(CalendarTest.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });

        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(eventAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = EventAdapter.day_string.get(position);
                ((EventAdapter) parent.getAdapter()).getPositionList(selectedGridDate, CalendarTest.this);


                Toast.makeText(CalendarTest.this , selectedGridDate , Toast.LENGTH_SHORT).show();
            }

        });
    }
    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                          cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        eventAdapter.refreshDays();
        eventAdapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    @Override
    public void onDateSet(DatePicker view , int year , int month , int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance().format(c.getTime());
        Toast.makeText(this , currentDate , Toast.LENGTH_SHORT).show();
    }
}