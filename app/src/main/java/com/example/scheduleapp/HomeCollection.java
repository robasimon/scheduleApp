package com.example.scheduleapp;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

class HomeCollection {
    public String date="";
    public String timestamp;
    public String name="";
    public String subject="";


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String description="";
    public String startTime;
    public String endTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public static ArrayList<HomeCollection> date_collection_arr;
    public HomeCollection(){
        //empty constructor
    }
    public HomeCollection(String date, String name, String subject, String description){

        this.date=date;
        this.name=name;
        this.subject=subject;
        this.description= description;

    }
    public static String getTimeDate(long timestamp){
        try{
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }
}