package com.example.scheduleapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder2> {

    ArrayList<Employee> list;
    Context context;
    String textName, textEmail, textId, textPhone;

    public EmployeeAdapter(Context c, ArrayList<Employee> p) {
        context = c;
        list = p;
    }


    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder2(LayoutInflater.from(context).inflate(R.layout.item_employee, viewGroup, false));
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder2 myViewHolder, final int i) {

        myViewHolder.empName.setText(list.get(i).getName());
        myViewHolder.email.setText("Email: " +list.get(i).getEmail());
        myViewHolder.phone.setText("Phone #: " +list.get(i).getPhoneNum());
        myViewHolder.id.setText("ID: " +list.get(i).getId());
        myViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                Bundle extras = new Bundle();
                textName=list.get(i).getName();
                textId = list.get(i).getId();
                textEmail = list.get(i).getEmail();
                textPhone = list.get(i).getPhoneNum();
                extras.putString("name", textName);
                extras.putString("id", textId);
                extras.putString("email", textEmail);
                extras.putString("phone", textPhone);
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });

        /*myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa = new Intent(context, NewTaskAct.class);
                aa.putExtra("name", list.get(i).getName());
                //aa.putExtra("time", getdateSelect);
                //aa.putExtra("hours", getstartTime);
                //aa.putExtra("keydoes", getKeyDoes);
                context.startActivity(aa);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder2 extends RecyclerView.ViewHolder {

        public TextView empName, id, email, phone;
        Button editButton;


        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            empName = itemView.findViewById(R.id.employee_display);
            id = itemView.findViewById(R.id.id_display);
            email = itemView.findViewById(R.id.email_display);
            phone = itemView.findViewById(R.id.phone_display);
            editButton = itemView.findViewById(R.id.edit_button);
        }
    }

}
