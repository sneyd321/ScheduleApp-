package com.example.ryan.scheduleapp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    String day;
    String className;
    String classRoom;
    String time;

    Schedule schedule;

    Button addClass;

    ListView lstSchedule;

    int globalPosition;

    ArrayList<Schedule> scheduleList = new ArrayList<>(0);

    ArrayList<String> hours = new ArrayList<>();
    ArrayList<String> minutes = new ArrayList<>();

    ScheduleListAdapter adapter;

    private Dialog IntroDialog;

    private Dialog updateDialog;
    private final int My_NOTIFICATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstSchedule = (ListView) findViewById(R.id.lstSchedule);

        addClass = (Button) findViewById(R.id.btnAddClass);


        lstSchedule.setOnItemClickListener(new scheduleAdapterListener());


        displaySchedules();

        if (SettingsActivity.isChecked){
            getTimes(true);
        }


    }

    protected void onResume(){
        super.onResume();
        if (SettingsActivity.isChecked){
            getTimes(true);
        }

    }


    private void getTimes(boolean itWorked){

        if (itWorked){
            ArrayList<String> timeArray = new ArrayList<>();
            ArrayList<String> dayArray = new ArrayList<>();



            for (int i = 0; i < scheduleList.size(); i++){

                timeArray.add(scheduleList.get(i).getTime());
                dayArray.add(scheduleList.get(i).getDay());
                String[] times = timeArray.get(i).split(":");
                hours.add(times[0]);
                minutes.add(times[1]);
            }


            Calendar cal = Calendar.getInstance();
            int hour = new Time(System.currentTimeMillis()).getHours();
            int days = cal.get(cal.DAY_OF_WEEK);

            String dayOfWeek;
            if (days == 1){
                dayOfWeek = "Sunday";
            }
            else if (days == 2){
                dayOfWeek = "Monday";
            }
            else if (days == 3){
                dayOfWeek = "Tuesday";
            }
            else if (days == 4){
                dayOfWeek = "Wednesday";
            }
            else if (days == 5){
                dayOfWeek = "Thursday";
            }
            else if (days == 6){
                dayOfWeek = "Friday";
            }else if (days == 7){
                dayOfWeek = "Saturday";
            }
            else{
                dayOfWeek = "";
                Toast.makeText(MainActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
            }





            for(int i = 0; i < hours.size(); i++ ){


                if (hour == Integer.parseInt(hours.get(i)) && dayOfWeek.equals(dayArray.get(i))){
                    Notification notification = new Notification.Builder(this)

                            .setTicker("Sheridan On-The-Go")
                            .setAutoCancel(true)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Sheridan On-The-Go")
                            .setContentText(scheduleList.get(i).getClassName() + " class has begun!")

                            .build();
                    //get notification service
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    //create notification
                    notificationManager.notify(i, notification);
                    break;
                }

            }
        }




    }




    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);

            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySchedules(){

        ScheduleDb database = new ScheduleDb(this);

        scheduleList = database.selectSchedules();


        adapter = new ScheduleListAdapter(this, R.layout.day_row, scheduleList);



        lstSchedule.setAdapter(adapter);


    }


    private void buildDialog() {


        AlertDialog.Builder introDialog = new AlertDialog.Builder(this);


        introDialog.setMessage("Would like to add a class?")
                .setTitle("Schedule Setup Wizard")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        IntroDialog.show();

                    }

                });

        final AlertDialog dialog = introDialog.create();

        IntroDialog = new Dialog(this);

        IntroDialog.setContentView(R.layout.update_schedule);

        IntroDialog.setTitle("Schedule Setup Wizard");

        final Button addClass = (Button) IntroDialog.findViewById(R.id.btnUpdate);

        final Button cancelContent = (Button) IntroDialog.findViewById(R.id.btnCancel);




        dialogDaySpinner();
        dialogTimeSpinner();

        addClass.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateContent();

            }

        });
        dialog.show();



        cancelContent.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntroDialog.dismiss();
            }
        });
    }

    private void dialogDaySpinner(){

        Spinner spnDayOfWeek = (Spinner) IntroDialog.findViewById(R.id.spnDaysOfWeek);

        spnDayOfWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view;

                day = text.getText().toString();
                schedule.setDay(day);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    private void dialogTimeSpinner(){

        Spinner spnTime = (Spinner) IntroDialog.findViewById(R.id.spnTimes);

        spnTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view;

                time = text.getText().toString();
                schedule.setTime(time);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void updateContent(){

        EditText edtClassName = (EditText) IntroDialog.findViewById(R.id.edtClassName);
        className = edtClassName.getText().toString();
        schedule.setClassName(className);

        EditText edtClassRoom = (EditText) IntroDialog.findViewById(R.id.edtClassRoom);
        classRoom = edtClassRoom.getText().toString();
        schedule.setClassRoom(classRoom);




        ScheduleDb database = new ScheduleDb(this);

        database.saveSchedule(schedule);

        displaySchedules();

        IntroDialog.dismiss();
    }



    public void onAddClass(View view) {
        schedule = new Schedule(null, null, null, null);
        buildDialog();
    }



    private class scheduleAdapterListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


            globalPosition = position;


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage("Would you like to update or delete?")
                    .setTitle("Schedule setup Wizard")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateDialog.dismiss();
                        }
                    })
                    .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ScheduleDb database = new ScheduleDb(MainActivity.this);

                            database.deletePlayer(scheduleList.get(position).getId());

                            displaySchedules();





                        }
                    })
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                            EditText edtClassName = (EditText) updateDialog.findViewById(R.id.edtClassName);
                            edtClassName.setText(scheduleList.get(position).getClassName());
                            EditText edtClassRoom = (EditText) updateDialog.findViewById(R.id.edtClassRoom);
                            edtClassRoom.setText(scheduleList.get(position).getClassRoom());




                            updateDialog.show();

                        }
                    });
            AlertDialog dialog = builder.create();

            dialog.show();


            updateDialog = new Dialog(MainActivity.this);

            updateDialog.setContentView(R.layout.update_schedule);

            updateDialog.setTitle("Schedule Setup Wizard");

            final Button updateClass = (Button) updateDialog.findViewById(R.id.btnUpdate);

            final Button cancelContent = (Button) updateDialog.findViewById(R.id.btnCancel);

            final Spinner spnDayOfWeek = (Spinner) updateDialog.findViewById(R.id.spnDaysOfWeek);

            spnDayOfWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int spnPosition, long id) {


                    TextView text = (TextView) view;

                    day = text.getText().toString();
                    scheduleList.get(position).setDay(day);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });



            Spinner spnTime = (Spinner) updateDialog.findViewById(R.id.spnTimes);

            spnTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int spnPosition, long id) {

                    TextView text = (TextView) view;

                    time = text.getText().toString();
                    scheduleList.get(position).setTime(time);



                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });





            updateClass.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText edtClassName = (EditText) updateDialog.findViewById(R.id.edtClassName);
                    className = edtClassName.getText().toString();
                    scheduleList.get(position).setClassName(className);
                    EditText edtClassRoom = (EditText) updateDialog.findViewById(R.id.edtClassRoom);
                    classRoom = edtClassRoom.getText().toString();
                    scheduleList.get(position).setClassRoom(classRoom);



                    ScheduleDb database = new ScheduleDb(MainActivity.this);

                    database.update(scheduleList.get(position));

                    displaySchedules();

                    updateDialog.dismiss();
                }
            });


            cancelContent.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDialog.dismiss();
                }
            });

        }

    }


}
