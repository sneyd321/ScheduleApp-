package com.example.ryan.scheduleapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ryan on 11/21/2016.
 */
public class Schedule implements Parcelable{


    long id;
    String day;
    String className;
    String classRoom;
    String time;



    public Schedule(long id, String day, String className, String classRoom, String time) {
        this.id = id;
        this.day = day;
        this.className = className;
        this.classRoom = classRoom;
        this.time = time;
    }

    public Schedule(String day, String className, String classRoom, String time) {
        this.day = day;
        this.className = className;
        this.classRoom = classRoom;
        this.time = time;
    }


    public Schedule(Parcel parcel){
        day = parcel.readString();
        className = parcel.readString();
        classRoom = parcel.readString();
        time = parcel.readString();
    }

    public final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    } ;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(className);
        dest.writeString(classRoom);
        dest.writeString(time);

    }
}
