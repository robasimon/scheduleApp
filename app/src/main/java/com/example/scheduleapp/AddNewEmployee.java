package com.example.scheduleapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Scanner;

public class AddNewEmployee extends AppCompatActivity{
    private EditText fname;
    private EditText lname;
    private EditText id;
    private EditText email;
    private EditText phoneNum;
    private Button saveEmployee;
    private Button cancel;
    private static boolean uniqueID = true;
    private static boolean failure = false;
    private static boolean idFlag = true;
    private static boolean complete = false;
    private static int invocations = 0;

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
                // Validate fields prior to setting them in employee
                String fnameIn = fname.getText().toString(), lnameIn = lname.getText().toString(), idIn = id.getText().toString(),
                       emailIn = email.getText().toString(), phoneNumIn = phoneNum.getText().toString();
                if (nameIsValid(fnameIn) && nameIsValid(lnameIn) && idIsUnique(idIn) && idIsValid(idIn) && isValidEmail(emailIn)
                        && isValidPhoneNum(phoneNumIn)) {
                    MainActivity.ids.add(idIn);
                    employee.setName(fnameIn + " " + lnameIn);
                    employee.setId(idIn);
                    employee.setEmail(emailIn);
                    employee.setPhoneNum(phoneNumIn);
                    Task<DocumentReference> firebaseFirestore = FirebaseFirestore.getInstance().collection("users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("employees").add(employee);

                    Intent intent = new Intent(AddNewEmployee.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(AddNewEmployee.this).create();
                    alertDialog.setTitle("Error creating new Employee");
                    alertDialog.setMessage(genErrorMsg(fnameIn, lnameIn, idIn, emailIn, phoneNumIn));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
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

    // Predicate methods for validating user input fields prior to syncing with firebase

    // Return the first applicable error message regarding the user's input
    public static String genErrorMsg(String fname, String lname, String id, String email, String phoneNum) {
        if (!nameIsValid(fname))
            return "First name field is invalid.\n\nFirst name must not be empty and only contain letters.";
        else if (!nameIsValid(lname))
            return "Last name field is invalid.\n\nLast name must not be empty and only contain letters.";
        else if (!idIsUnique(id))
            return "Employee id already exists.";
        else if (!idIsValid(id))
            return "Employee id field is invalid.\n\nEmployee id must not be empty and only contain numbers.";
        else if (!isValidEmail(email))
            return "Email field is invalid.\n\nEmail must not be empty and be in the form \"username@website.top-level-domain\"";
        else if (!isValidPhoneNum(phoneNum))
            return "Phone number field is invalid.\n\nPhone number must contain at least 10 numbers with no other symbols.";
        // This branch should not happen. It's appearance indicates some uncaught internal or logic error
        else
            return "Internal error adding new employee.";
    }

    // Returns true if name is valid, false otherwise
    public static boolean nameIsValid (String name) {
        for (char c : name.toCharArray())
            if (!Character.isLetter(c))
                return false;

        return name.length() > 0 ? true : false;
    }
    public static boolean idIsUnique(String id) {
        return !MainActivity.ids.contains(id);
    }
    // Returns true if id is valid, false otherwise
    public static boolean idIsValid(String id) {
        for (char c : id.toCharArray())
            if (!Character.isDigit(c))
                return false;
        return id.length() > 0 ? true : false;
    }
    // Returns true if phone number is valid, false otherwise
    public static boolean isValidPhoneNum(String phoneNum) {
        String validator = phoneNum.replaceAll("[\\(\\)\\-\\s]", "");
        for (Character c : validator.toCharArray())
            if (!Character.isDigit(c))
                return false;

        // Valid phone number with area code needs 10 digits, allow more than 10
        // in case of international numbers
        return validator.length() >= 10 ? true : false;
    }
    // Returns true if format is "username@website.top-level-domain" and
    // false otherwise
    public static boolean isValidEmail(String email) {
        Scanner strScanner = new Scanner(email);
        strScanner.useDelimiter("@");
        if (strScanner.hasNext()) {
            int emailLength = strScanner.next().length();
            if (strScanner.hasNext()) {
                String buffer = strScanner.next();
                emailLength += buffer.length();
                strScanner = new Scanner(buffer);
                // Escape dot to treat as a character delimiter rather than a regex
                strScanner.useDelimiter("\\.");
                if (strScanner.hasNext()) {
                    emailLength += strScanner.next().length();
                    if (strScanner.hasNext()) {
                        emailLength += strScanner.next().length();
                        // Ensure there are actually characters between proper delimiters
                        return emailLength > 0 ? true : false;
                    }
                }
            }
        }
        return false;
    }

}
