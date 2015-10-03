package com.example.kamiseto.bacscheduler;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.*;

public class SettingsFragment extends Fragment {

    public Context context=getActivity();
    private AlarmManager manager;
    private PendingIntent pendingIntent, pendingIntent1, pendingIntent2,pendingIntent3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Intent alarmIntent1 = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent1 = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent1, 0);

        Intent alarmIntent2 = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent2 = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent2, 0);

        Intent alarmIntent3 = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent3 = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent3, 0);

        //initializing SharedPreferences
        final Context contextSP=getActivity();
        final SharedPreferences pref = contextSP.getSharedPreferences("MyPref", contextSP.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        // Array of choices

        final ArrayList<String> spinnerArray = new ArrayList<String>();
        String subj="--"; spinnerArray.add(subj);
        subj="romana"; spinnerArray.add(subj);
        subj=pref.getString("materia1", "matematica"); spinnerArray.add(subj);
        subj=pref.getString("materia2", "informatica"); spinnerArray.add(subj);

        // Selection of the spinner
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerSubject);

        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(rootView.getContext(),   android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       final int position, long id) {
                context=(Context) getActivity();
                if (spinner.getItemAtPosition(position)!="--") {
                    promptForResult("Seteaza programul", "Introdu ziua, ora si durata:", new DialogInputInterface() {

                        EditText day_ed, hour_ed, time_ed;

                        public View onBuildDialog() {
                            day_ed = new EditText(context);
                            day_ed.setHint("ex: luni");
                            hour_ed = new EditText(context);
                            hour_ed.setHint("ex: 14:30");
                            time_ed = new EditText(context);
                            time_ed.setHint("nr ore la"+spinner.getItemAtPosition(position).toString());

                            LinearLayout ll = new LinearLayout(context);
                            ll.setOrientation(LinearLayout.VERTICAL);
                            ll.addView(day_ed);
                            ll.addView(hour_ed);
                            ll.addView(time_ed);
                            return ll;
                        }

                        public void onCancel() {
                            if (position==1) pendingIntent=pendingIntent1;
                            else if (position==2) pendingIntent=pendingIntent2;
                            else if (position==3) pendingIntent=pendingIntent3;
                            cancelAlarm(rootView, pendingIntent);
                        }

                        public void onResult(View v) {

                            String day = day_ed.getText().toString();
                            String hour = hour_ed.getText().toString();
                            String time = time_ed.getText().toString();

                            editor.putString("day" + spinner.getItemAtPosition(position).toString(), day);
                            editor.putString("hour" + spinner.getItemAtPosition(position).toString(), hour);
                            editor.putString("time" + spinner.getItemAtPosition(position).toString(), time);
                            editor.commit();
                            Toast.makeText(context, "Program setat pentru " + spinner.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();

                            AlarmReceiver.Subject=spinner.getItemAtPosition(position).toString();
                            AlarmReceiver.ContextSP=getActivity();
                            if (position==1) pendingIntent=pendingIntent1;
                            else if (position==2) pendingIntent=pendingIntent2;
                            else if (position==3) pendingIntent=pendingIntent3;
                            cancelAlarm(rootView, pendingIntent);
                            //Toast.makeText(getActivity().getApplicationContext(), "previous alarm canceled spinner 1", Toast.LENGTH_SHORT).show();
                            startAlarm(rootView, 1, pendingIntent);
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button cancelA= (Button) rootView.findViewById(R.id.cancelButton);
        cancelA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm(rootView, pendingIntent);
            }
        });

        return rootView;
    }

    //function for setting automatic action
    public void startAlarm(View view,int period, PendingIntent pendingIntent) {
        manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+period*86400000, period*86400000, pendingIntent);
        Toast.makeText(getActivity().getApplicationContext(), "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    //function for canceling automatic action
    public void cancelAlarm(View view, PendingIntent pendingIntent) {
        if (manager != null) {

            manager.cancel(pendingIntent);
            Toast.makeText(getActivity().getApplicationContext(), "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    interface DialogInputInterface {
        // onBuildDialog() is called when the dialog builder is ready to accept a view to insert
        // into the dialog
        View onBuildDialog();
        // onCancel() is called when the user clicks on 'Cancel'
        void onCancel();
        // onResult(View v) is called when the user clicks on 'Ok'
        void onResult(View v);
    }

    void promptForResult(String dlgTitle, String dlgMessage, final DialogInputInterface dlg) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(dlgTitle);
        alert.setMessage(dlgMessage);
        // build the dialog
        final View v = dlg.onBuildDialog();
        // put the view obtained from the interface into the dialog
        if (v != null) { alert.setView(v);}
        // procedure for when the ok button is clicked.
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // ** HERE IS WHERE THE MAGIC HAPPENS! **
                dlg.onResult(v);
                dialog.dismiss();
                return;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dlg.onCancel();
                dialog.dismiss();
                return;
            }
        });
        alert.show();
    }
}

