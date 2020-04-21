package com.example.scheduleapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.MyViewHolder>{

    Context context;
    ArrayList<HomeCollection> myDoes;
    String name, s, e, date;

    public ShiftAdapter(Context c, ArrayList<HomeCollection> p) {
        context = c;
        myDoes = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does, viewGroup, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

        String startTime = myDoes.get(i).getStartTime();
        String endTime = myDoes.get(i).getEndTime();

        LocalTime start = LocalTime.parse(startTime, timeFormatter);
        LocalTime end = LocalTime.parse(endTime, timeFormatter);

        Duration diff = Duration.between(start, end);

        long hours = diff.toHours();
        long minutes = diff.minusHours(hours).toMinutes();
        String totalTimeString = String.format("%02d hrs : %02d min", hours, minutes);


        String time = myDoes.get(i).getStartTime() + " - " + myDoes.get(i).getEndTime();//myDoes.get(i).getStartTime() + " - " + myDoes.get(i).getEndTime();
        myViewHolder.employeeName.setText(myDoes.get(i).getName());
        myViewHolder.dateSelect.setText(time);
        myViewHolder.startTime.setText(totalTimeString);

        final String getemployeeName = myDoes.get(i).getName();
        //final String getdateSelect = time;
        final String getstartTime = "(X) Hours";//myDoes.get(i).getDate();
       // final String getKeyDoes = myDoes.get(i).getKeydoes();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShiftEdit.class);
                Bundle extras = new Bundle();
                name=myDoes.get(i).getName();
                date = myDoes.get(i).getDate();
                s = myDoes.get(i).getStartTime();
                e = myDoes.get(i).getEndTime();
                extras.putString("name", name);
                extras.putString("date", date);
                extras.putString("start", s);
                extras.putString("end", e);
                intent.putExtras(extras);
                context.startActivity(intent);
                /*Intent aa = new Intent(context, NewTaskAct.class);
                aa.putExtra("name", getemployeeName);
                //aa.putExtra("time", getdateSelect);
                //aa.putExtra("hours", getstartTime);
                //aa.putExtra("keydoes", getKeyDoes);
                context.startActivity(aa);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView employeeName, dateSelect, startTime, keydoes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = (TextView) itemView.findViewById(R.id.employeeName);
            dateSelect = (TextView) itemView.findViewById(R.id.dateSelect);
            startTime = (TextView) itemView.findViewById(R.id.startTime);
        }
    }

}
