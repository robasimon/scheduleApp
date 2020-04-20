package com.example.scheduleapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class CalendarTest extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public GregorianCalendar cal_month, cal_month_copy;
    private EventAdapter eventAdapter;
    private TextView tv_month;
    private Button addShift;
    //DatabaseReference reference;
    CollectionReference reference;
    ArrayList<HomeCollection> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_calendar);
        addShift = findViewById(R.id.add_shift);
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        addShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogFragment datePicker = new AddShiftDialog();
                //datePicker.show(getSupportFragmentManager(), "date picker");
                Intent i = new Intent(CalendarTest.this, NewTaskAct.class);
                startActivity(i);
            }
        });
        //reference = FirebaseDatabase.getInstance().getReference().child("shifts").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //reference.addValueEventListener(new ValueEventListener() {//reference.orderByChild("startTime").startAt(sortedDate).addValueEventListener(new ValueEventListener() {
        reference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("shifts");
        reference.get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeCollection p = document.toObject(HomeCollection.class);
                                HomeCollection.date_collection_arr.add(new HomeCollection(p.getDate() ,
                                                                                          p.getName() ,
                                                                                          p.getStartTime() ,
                                                                                          p.getEndTime() ,
                                                                                          p.getTotalHours()));
                            }
                        }
           /* @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // set code to retrieve data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HomeCollection p = dataSnapshot1.getValue(HomeCollection.class);
                    HomeCollection.date_collection_arr.add(new HomeCollection(p.getDate(), p.getName(), p.getStartTime(),p.getEndTime(),p.getTotalHours()));
                    //list.add(p);
                }*/
                //HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
                //HomeCollection.date_collection_arr.add( new HomeCollection("04-08-2020" ,"TESTING NAME","07:30 AM","04:00 PM","8hr:50min"));
                /*HomeCollection.date_collection_arr.add( new HomeCollection("04-08-2020" ,"Temp Name","Shift","Full day shift"));
                HomeCollection.date_collection_arr.add( new HomeCollection("04-08-2020" ,"Temp Name","Shift","Full day shift"));
                HomeCollection.date_collection_arr.add( new HomeCollection("03-08-2020" ,"Temp Name","Shift","Full day shift"));
                HomeCollection.date_collection_arr.add( new HomeCollection("04-09-2020" ,"Temp Name","Shift","Full day shift"));
                HomeCollection.date_collection_arr.add( new HomeCollection("03-15-2020" ,"Temp Name","Shift","Full day shift"));
                HomeCollection.date_collection_arr.add( new HomeCollection("04-26-2020" ,"Temp Name","Shift","Full day shift"));
                HomeCollection.date_collection_arr.add( new HomeCollection("03-08-2020" ,"Temp Name","Shift","Full day shift"));
                HomeCollection.date_collection_arr.add( new HomeCollection("05-16-2020" ,"Temp Name","Shift","Full day shift"));
                HomeCollection.date_collection_arr.add( new HomeCollection("05-09-2020" ,"Temp Name","Shift","Full day shift"));
*/
                Collections.sort(HomeCollection.date_collection_arr , new Comparator<HomeCollection>() {
                    @Override
                    public int compare(HomeCollection o1 , HomeCollection o2) {
                        try {
                            return new SimpleDateFormat("hh:mm a").parse(o1.getStartTime()).compareTo(new SimpleDateFormat("hh:mm a").parse(o2.getStartTime()));
                        } catch (ParseException e) {
                            return 0;
                        }
                    }
                });
                cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
                cal_month_copy = (GregorianCalendar) cal_month.clone();
                eventAdapter = new EventAdapter(CalendarTest.this, cal_month, HomeCollection.date_collection_arr);

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
/*
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            });*/
/*
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

        });*/});
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