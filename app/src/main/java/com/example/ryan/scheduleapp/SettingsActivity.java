package com.example.ryan.scheduleapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class SettingsActivity extends PreferenceActivity {



    //boolean isPreferenceChecked = preference.isChecked();

    public static boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        final CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("timeNotification");

        checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                Intent data = new Intent();

                if (newValue.toString().equals("true")) {


                    isChecked = true;
                    data.putExtra("message", "true");

                    setResult(1, data);
                }
                else{

                    isChecked = false;
                    data.putExtra("message", "false");

                    setResult(1, data);
                }


                    return true;
            }


        });

        Intent data = new Intent();

        if (checkBoxPreference.isChecked() == true) {


            isChecked = true;
            data.putExtra("message", "true");

            setResult(1, data);
        }
        else{
            isChecked = false;

            data.putExtra("message", "false");

            setResult(1, data);
        }


    }





}
