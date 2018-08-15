package com.example.ryan.scheduleapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 11/21/2016.
 */
public class ScheduleListAdapter extends ArrayAdapter<Schedule> {

    private Context context;

    int resourceId;

    ArrayList<Schedule> schedules;

    public ScheduleListAdapter(Context context, int resourceId, ArrayList<Schedule> schedules) {
        super(context, resourceId, schedules);

        this.context = context;
        this.resourceId = resourceId;
        this.schedules = schedules;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            convertView = inflater.inflate(resourceId, parent, false);


        }

        TextView txtDay = (TextView) convertView.findViewById(R.id.txtDayRow);
        TextView txtClass = (TextView) convertView.findViewById(R.id.txtClassNameRow);
        TextView txtRoom = (TextView) convertView.findViewById(R.id.txtClassRoomRow);
        TextView txtTime = (TextView) convertView.findViewById(R.id.txtTimeRow);

        Schedule schedule = schedules.get(position);

        txtDay.setText(schedule.getDay());
        txtClass.setText(schedule.getClassName());
        txtRoom.setText(schedule.getClassRoom());
        txtTime.setText(schedule.getTime());

        return convertView;

    }


}
