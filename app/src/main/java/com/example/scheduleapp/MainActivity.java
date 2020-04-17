package com.example.scheduleapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static final int RC_SIGN_IN = 99;
    private Button newEmployee;
    private Button viewCalendar;
    private Button signOut;
    private MainActivityViewModel mViewModel;
    TextView titlepage, subtitlepage, endpage;
    Button btnAddNew;

    DatabaseReference reference;
    RecyclerView ourdoes;
    ArrayList<HomeCollection> list;
    ShiftAdapter doesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        titlepage = findViewById(R.id.titlepage);
        subtitlepage = findViewById(R.id.subtitlepage);
        endpage = findViewById(R.id.endpage);

        btnAddNew = findViewById(R.id.btnAddNew);

        // import font
        //Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        //Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
       // titlepage.setTypeface(MMedium);
        //subtitlepage.setTypeface(MLight);
        //endpage.setTypeface(MLight);

       // btnAddNew.setTypeface(MLight);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this,NewTaskAct.class);
                startActivity(a);
            }
        });


        // working with data
        ourdoes = findViewById(R.id.ourdoes);
        ourdoes.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<HomeCollection>();

        // get data from firebase
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String sortedDate = dateFormat.format(new Date());
        String date = dateFormat.format(calendar.getTime());
        reference = FirebaseDatabase.getInstance().getReference().child("shifts").child(date);
        reference.addValueEventListener(new ValueEventListener() {
        //reference.orderByChild("startTime").startAt(sortedDate).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // set code to retrieve data and replace layout
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    HomeCollection p = dataSnapshot1.getValue(HomeCollection.class);
                    list.add(p);
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                Collections.sort(list , new Comparator<HomeCollection>() {
                                     @Override
                                     public int compare(HomeCollection o1 , HomeCollection o2) {
                                         try {
                                             return new SimpleDateFormat("hh:mm a").parse(o1.getStartTime()).compareTo(new SimpleDateFormat("hh:mm a").parse(o2.getStartTime()));
                                         } catch (ParseException e) {
                                             return 0;
                                         }
                                     }
                                 });
                                 doesAdapter = new ShiftAdapter(MainActivity.this , list);
                ourdoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(

                        new AuthUI.IdpConfig.EmailBuilder().build()
                ))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(true);
    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        if (requestCode == RC_SIGN_IN) {
            mViewModel.setIsSigningIn(false);

            if (resultCode != RESULT_OK && shouldStartSignIn()) {
                startSignIn();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_employee:
                Intent intent = new Intent(MainActivity.this, AddNewEmployee.class);
                startActivity(intent);
                break;
            case R.id.menu_view_calendar:
                Intent intent2 = new Intent(MainActivity.this, CalendarTest.class);
                startActivity(intent2);
                break;
            case R.id.menu_sign_out:
                AuthUI.getInstance().signOut(this);
                startSignIn();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {

    }
    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);
    }
}