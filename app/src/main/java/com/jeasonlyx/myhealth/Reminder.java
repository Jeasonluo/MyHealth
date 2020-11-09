package com.jeasonlyx.myhealth;

import java.util.Objects;

public class Reminder {
    private int id;

    private int time;

    private int end_date;

    private int repeat;

    public Reminder(int time, int end_date, int repeat) {
        this.time = time;
        this.end_date = end_date;
        this.repeat = repeat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getHour()
    {
        return time/100;
    }

    public int getMinute(){
        return time%100;
    }

    public void setTime(int hour, int minute){
        this.time =  hour*100 + minute;
    }

    public int getEnd_date() {
        return end_date;
    }

    public void setEnd_date(int end_date) {
        this.end_date = end_date;
    }

    public int getMonth(){
        return end_date/1000000;
    }

    public int getDay(){
        return end_date%1000000/10000;
    }

    public int getYear(){
        return end_date%10000;
    }

    public void setEnd_date(int m, int d, int y){
        this.end_date = y + d * 10000 + m * 1000000;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return time == reminder.time &&
                end_date == reminder.end_date &&
                repeat == reminder.repeat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, end_date, repeat);
    }


    public String getTimeString(){
        return getHour() + ":" + getMinute();
    }

    public String getDateString(){
        return " " + getMonth() + "/" + getDay() + "/" + getYear() + " ";
    }

    public String getRepeatString(){
        switch(repeat){
            case 0: return "Daily";
            case 1: return "Weekly";
            case 2: return "Monthly";
            default: return "";
        }
    }

    public static int getTime(int hour, int minute){
        return hour*100 + minute;
    }

    public static int getEnd_date(int month, int day, int year){
        return year + day * 10000 + month * 1000000;
    }

    public static String getTimeString(int hour, int minutes){
        return hour + ":" + minutes;
    }

    public static String getRepeatString(int repeat){
        switch(repeat){
            case 0: return "Daily";
            case 1: return "Weekly";
            case 2: return "Monthly";
            default: return "";
        }
    }

    public static String getDateString(int month, int day, int year){
        return " " + month + "/" + day + "/" + year + " ";
    }
}
