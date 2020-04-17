package com.example.scheduleapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddNewEmployee extends AppCompatActivity{
    private EditText fname;
    private EditText lname;
    private EditText id;
    private EditText email;
    private EditText phoneNum;
    private Button saveEmployee;
    private Button cancel;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        fname = findViewById(R.id.employee_fname);
        lname = findViewById(R.id.employee_lname);
        id = findViewById(R.id.employee_id);
        email = findViewById(R.id.employee_email);
        phoneNum = findViewById(R.id.employee_phone);
        phoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        saveEmployee = findViewById(R.id.save_employee);
        saveEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Employee employee = new Employee();
                employee.setName(fname.getText() + " " + lname.getText());
                employee.setId(id.getText().toString());
                employee.setEmail(email.getText().toString());
                employee.setPhoneNum(phoneNum.getText().toString());
                Task<DocumentReference> firebaseFirestore = FirebaseFirestore.getInstance().collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("employees").add(employee);

                Intent intent = new Intent(AddNewEmployee.this, MainActivity.class);
                startActivity(intent);
            }
        });
        cancel = findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewEmployee.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


}
