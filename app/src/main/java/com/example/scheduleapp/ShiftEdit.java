package com.example.scheduleapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class ShiftEdit extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView shiftRange, shiftName, dateforedit;
    Spinner empSelect;
    Button delShift, dateEdit, startEdit, endEdit, cancelShift, updateShift;
    String date, start, end, s ,e;
    String intentDate, intentName, intentStart, intentEnd;
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    private int CalendarHour, CalendarMinute;
    public static String defaultStr = "Select Employee";
    String format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_shift);
        List<String> employees = new ArrayList<>();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        dateforedit = findViewById(R.id.dateforedit);
        shiftRange = findViewById(R.id.shiftRange);
        shiftName = findViewById(R.id.shiftName);
        intentDate = extras.getString("date");
        intentName = extras.getString("name");
        intentStart = extras.getString("start");
        intentEnd = extras.getString("end");
        String range = intentStart + " - " + intentEnd;
        shiftRange.setText(range);
        shiftName.setText(intentName);
        dateforedit.setText(intentDate);
        empSelect = findViewById(R.id.employee_edit);
        delShift = findViewById(R.id.delete_shift);
        CollectionReference reference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection(
                "shifts");
        delShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.whereEqualTo("name" , intentName).whereEqualTo("date" ,
                                                                         intentDate).get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot snapshot : task.getResult()) {
                                        reference.document(snapshot.getId()).delete();
                                    }
                                    Intent intent = new Intent(ShiftEdit.this , MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
        employees.add("Select Employee");
        FirebaseFirestore mRef = FirebaseFirestore.getInstance();
        CollectionReference employeeRef = mRef.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection(
                "employees");
        employeeRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String phoneName = document.getString("name");
                        employees.add(phoneName);
                    }
                    ArrayList<String> emp2 = new ArrayList<>(new LinkedHashSet<>(employees));
                    @SuppressLint("RestrictedApi") ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getApplicationContext() ,
                            android.R.layout.simple_spinner_item ,
                            emp2);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    empSelect.setAdapter(adapter);
                }
            }
        });
        endEdit = findViewById(R.id.end_edit);

        startEdit = findViewById(R.id.start_edit);
        startEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(ShiftEdit.this ,
                                                        android.R.style.Theme_Holo_Light_Dialog ,
                                                        new TimePickerDialog.OnTimeSetListener() {
                                                            String temp, temp2;

                                                            @Override
                                                            public void onTimeSet(TimePicker view , int hourOfDay , int minute) {
                                                                if (hourOfDay == 0) {

                                                                    hourOfDay += 12;

                                                                    format = "AM";
                                                                } else if (hourOfDay == 12) {

                                                                    format = "PM";

                                                                } else if (hourOfDay > 12) {

                                                                    hourOfDay -= 12;

                                                                    format = "PM";

                                                                } else {
                                                                    format = "AM";
                                                                }

                                                                if (minute >= 0 && minute <= 9) {
                                                                    temp = "0" + minute;
                                                                } else {
                                                                    temp = String.valueOf(minute);
                                                                }
                                                                if (hourOfDay > -1 && hourOfDay < 10) {
                                                                    temp2 = "0" + hourOfDay;
                                                                } else {
                                                                    temp2 = String.valueOf(hourOfDay);
                                                                }

                                                                startEdit.setText(temp2 + ":" + temp + " " + format);
                                                                start = startEdit.getText().toString();
                                                            }
                                                        } ,
                                                        CalendarHour ,
                                                        CalendarMinute ,
                                                        false);
                timepickerdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timepickerdialog.show();

            }
        });


        endEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(ShiftEdit.this ,
                                                        android.R.style.Theme_Holo_Light_Dialog ,
                                                        new TimePickerDialog.OnTimeSetListener() {

                                                            @Override
                                                            public void onTimeSet(TimePicker view , int hourOfDay , int minute) {
                                                                String temp, temp2;
                                                                if (hourOfDay == 0) {

                                                                    hourOfDay += 12;

                                                                    format = "AM";
                                                                } else if (hourOfDay == 12) {

                                                                    format = "PM";

                                                                } else if (hourOfDay > 12) {

                                                                    hourOfDay -= 12;

                                                                    format = "PM";

                                                                } else {
                                                                    format = "AM";
                                                                }
                                                                if (minute >= 0 && minute <= 9) {
                                                                    temp = "0" + minute;
                                                                } else {
                                                                    temp = String.valueOf(minute);
                                                                }
                                                                if (hourOfDay > -1 && hourOfDay < 10) {
                                                                    temp2 = "0" + hourOfDay;
                                                                } else {
                                                                    temp2 = String.valueOf(hourOfDay);
                                                                }

                                                                endEdit.setText(temp2 + ":" + temp + " " + format);
                                                                end = endEdit.getText().toString();
                                                            }
                                                        } ,
                                                        CalendarHour ,
                                                        CalendarMinute ,
                                                        false);
                timepickerdialog.show();
                timepickerdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            }
        });
        //////////////////////////



        dateEdit = findViewById(R.id.date_edit);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new AddShiftDialog();
                datePicker.show(getSupportFragmentManager() , "date picker");


            }
        });



        updateShift = findViewById(R.id.update_shift);
        cancelShift = findViewById(R.id.cancel_shift);
        cancelShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShiftEdit.this , MainActivity.class);
                startActivity(i);
            }
        });
        updateShift.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                LocalTime start2 = null, end2=null;
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a" ,
                                                                              Locale.ENGLISH);
                String newstart, newend, newemp;
                if(startEdit.getText().toString().length() > 0){
                    newstart = startEdit.getText().toString();

                }
                else{
                    newstart = intentStart;

                }
                start2 = LocalTime.parse(newstart, timeFormatter);
                if(endEdit.getText().toString().length() > 0) {
                    newend = endEdit.getText().toString();

                }
                else{
                    newend = intentEnd;
                }
                end2 = LocalTime.parse(newend , timeFormatter);
                if(dateEdit.getText().toString().length() > 0){
                    date = dateEdit.getText().toString();
                }
                else {
                    date = intentDate;
                }

                Duration diff = Duration.between(start2 , end2);
                if (diff.isNegative() || diff.isZero()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ShiftEdit.this).create();
                    alertDialog.setTitle("Error creating new shift");
                    alertDialog.setMessage("End time must be after start time");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL , "OK" ,
                                          new DialogInterface.OnClickListener() {
                                              public void onClick(DialogInterface dialog , int which) {
                                                  dialog.dismiss();
                                              }
                                          });
                    alertDialog.show();
                    return;
                }
                LocalDate now = LocalDate.now(), setDate = LocalDate.parse(date ,
                                                                           DateTimeFormatter.ofPattern(
                                                                                   "MM-dd-yyyy"));
                Period dateDiff = Period.between(now , setDate);

                if (dateDiff.isNegative()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ShiftEdit.this).create();
                    alertDialog.setTitle("Error creating new shift");
                    alertDialog.setMessage("Date of shift must be either current day or later date.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL , "OK" ,
                                          new DialogInterface.OnClickListener() {
                                              public void onClick(DialogInterface dialog , int which) {
                                                  dialog.dismiss();
                                              }
                                          });
                    alertDialog.show();
                    return;
                }

                long hours = diff.toHours();
                long minutes = diff.minusHours(hours).toMinutes();

                String totalTimeString = String.format("%02d hrs : %02d min" , hours , minutes);
                if (empSelect.getSelectedItem().toString().equals(defaultStr)) {
                    newemp = intentName;
                }else{
                    newemp = empSelect.getSelectedItem().toString();
                }
                HomeCollection shift = new HomeCollection();
                //shift.setTimestamp(ServerValue.TIMESTAMP.toString());
                shift.setDate(date);
                shift.setStartTime(newstart);
                shift.setEndTime(newend);
                shift.setName(newemp);
                shift.setTotalHours(totalTimeString);
                //String hours = shift.startTime + " - " + shift.endTime;
                reference.whereEqualTo("name" , intentName).whereEqualTo("date" ,
                                                                         intentDate).get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(QueryDocumentSnapshot snapshot : task.getResult())
                                {
                                    reference.document(snapshot.getId()).set(shift);
                                }
                                Intent a = new Intent(ShiftEdit.this , MainActivity.class);
                                startActivity(a);
                            }
                        });


            }

        });
    }
    @Override
    public void onDateSet(DatePicker view , int year , int month , int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        //String currentDate = DateFormat.getDateInstance().format(c.getTime());
        String currentDate = format.format(c.getTime());
        date = currentDate;
        dateEdit.setText(date);
        Toast.makeText(this , currentDate , Toast.LENGTH_SHORT).show();
    }
}
