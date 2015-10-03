package com.example.kamiseto.bacscheduler;

import com.example.kamiseto.bacscheduler.AlarmReceiver;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

public class ProgressFragment extends Fragment {
    Context context =getActivity();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);
        AlarmReceiver.ContextSP=getActivity();

        //initializing textviews
        final TextView materia1_tv=(TextView) rootView.findViewById(R.id.textView5);
        final TextView materia2_tv=(TextView) rootView.findViewById(R.id.textView6);
        final TextView nrH1_tv=(TextView) rootView.findViewById(R.id.romanaTextView);
        final TextView nrH2_tv=(TextView) rootView.findViewById(R.id.option1textView);
        final TextView nrH3_tv=(TextView) rootView.findViewById(R.id.option2textView);
        final TextView nrDaysUntilBAC = (TextView) rootView.findViewById(R.id.nrDaysTillTextView);

        //initializing date of bac
        TextView dateBac= (TextView) rootView.findViewById(R.id.textView3);
        dateBac.setText("4 iulie 2016");
        Calendar calendarGivenDate = Calendar.getInstance();
        calendarGivenDate.set(Calendar.DAY_OF_MONTH, 4);
        calendarGivenDate.set(Calendar.MONTH, 6);
        calendarGivenDate.set(Calendar.YEAR, 2016);

        Calendar calendar = Calendar.getInstance();
        int dayOfYear1 = calendarGivenDate.get(Calendar.DAY_OF_YEAR);
        int dayOfYear2 = calendar.get(Calendar.DAY_OF_YEAR);
        if (calendarGivenDate.get(Calendar.YEAR)==calendar.get(Calendar.YEAR))
        {
            nrDaysUntilBAC.setText(Integer.toString(dayOfYear1 - dayOfYear2));
        } else nrDaysUntilBAC.setText(Integer.toString(dayOfYear2 + (365 - dayOfYear1)));

        //initializing SharedPreferences
        final Context contextSP=getActivity();
        final SharedPreferences pref = contextSP.getSharedPreferences("MyPref", contextSP.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        //getting data from SharedPreferences
        String option1=pref.getString("materia1",null);
        String nrh1=pref.getString("nrHours1",null);

        if (option1==null|| nrh1==null) {
            context=rootView.getContext();
        } else {
            String m1= pref.getString("materia1", null);
            String m2= pref.getString("materia2", null);
            String h1= pref.getString("nrHours1", null);
            String h2= pref.getString("nrHours2", null);
            String h3= pref.getString("nrHours3", null);

            materia1_tv.setText("Nr ore/sapt la " + m1+":");
            materia2_tv.setText("Nr ore/sapt la " + m2+":");
            nrH1_tv.setText(h1);
            nrH2_tv.setText(h2);
            nrH3_tv.setText(h3);
        }

        return rootView;
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

