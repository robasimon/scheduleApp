package com.example.scheduleapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class NewTaskAct extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    TextView titlepage, addtitle, adddesc, adddate;
    //EditText employeeName, startTime;
    Button btnSaveTask, btnCancel;
    //DatabaseReference reference;
    Task<DocumentReference> reference;
    Integer doesNum = new Random().nextInt();
    String keydoes = Integer.toString(doesNum);
    String start, end, date;
    private Button addDate;
    Button endTime, startTime;
    String s, e;
    Button timePickerDialogButton;
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    private int CalendarHour, CalendarMinute;
    Spinner spinner;
    String format;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        List<String> employees = new ArrayList<>();
        spinner = findViewById(R.id.employeeName);
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
                    spinner.setAdapter(adapter);
                }
            }
        });
        endTime = findViewById(R.id.endTime);

        startTime = findViewById(R.id.startTime);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(NewTaskAct.this ,
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

                                                                startTime.setText(temp2 + ":" + temp + " " + format);
                                                                start = startTime.getText().toString();
                                                            }
                                                        } ,
                                                        CalendarHour ,
                                                        CalendarMinute ,
                                                        false);
                timepickerdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timepickerdialog.show();

            }
        });


        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(NewTaskAct.this ,
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

                                                                endTime.setText(temp2 + ":" + temp + " " + format);
                                                                end = endTime.getText().toString();
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


        titlepage = findViewById(R.id.titlepage);
        addDate = findViewById(R.id.dateSelect);
        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new AddShiftDialog();
                datePicker.show(getSupportFragmentManager() , "date picker");


            }
        });
        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        adddate = findViewById(R.id.adddate);


        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancelShift);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewTaskAct.this , MainActivity.class);
                startActivity(i);
            }
        });
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //Task<DocumentReference> firebaseFirestore = FirebaseFirestore.getInstance().collection("users")
                //.document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("shifts").add(shift);
                // insert data to database
                //Calendar calendar = Calendar.getInstance();
                //SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                //String date2 = dateFormat.format(calendar.getTime());
                //reference = FirebaseDatabase.getInstance().getReference().child("shifts").
                //child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(spinner.getSelectedItem().toString());

                /*reference.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {*/
                //Calendar calendar = Calendar.getInstance();
                //SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                //String date = dateFormat.format(calendar.getTime());
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a" ,
                                                                              Locale.ENGLISH);

                s = start;
                e = end;

                LocalTime start2 = LocalTime.parse(s , timeFormatter);
                LocalTime end2 = LocalTime.parse(e , timeFormatter);

                Duration diff = Duration.between(start2 , end2);

                long hours = diff.toHours();
                long minutes = diff.minusHours(hours).toMinutes();
                String totalTimeString = String.format("%02d hrs : %02d min" , hours , minutes);
                HomeCollection shift = new HomeCollection();
                //shift.setTimestamp(ServerValue.TIMESTAMP.toString());
                shift.setDate(date);
                shift.setStartTime(start);
                shift.setEndTime(end);
                shift.setName(spinner.getSelectedItem().toString());
                shift.setTotalHours(totalTimeString);
                //String hours = shift.startTime + " - " + shift.endTime;
                Task<DocumentReference> ref = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .collection("shifts").add(shift);
                ref.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Intent a = new Intent(NewTaskAct.this , MainActivity.class);
                        startActivity(a);
                    }
                });
                //reference.setValue(shift);
                //dataSnapshot.getRef().child("name").setValue(employeeName.getText().toString());
                //dataSnapshot.getRef().child("time").setValue("8AM - 4PM");
                // dataSnapshot.getRef().child("hours").setValue(startTime.getText().toString());
                // dataSnapshot.getRef().child("keydoes").setValue(keydoes);



            }
/*
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
           }
        });
*/
            // import font
            //Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
            //Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

            // customize font
            //titlepage.setTypeface(MMedium);

            //addtitle.setTypeface(MLight);
            //employeeName.setTypeface(MMedium);

            //adddesc.setTypeface(MLight);
            //dateSelect.setTypeface(MMedium);

            //adddate.setTypeface(MLight);
            //startTime.setTypeface(MMedium);

            //btnSaveTask.setTypeface(MMedium);
            //btnCancel.setTypeface(MLight);

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
        addDate.setText(date);
        Toast.makeText(this , currentDate , Toast.LENGTH_SHORT).show();
    }


}
