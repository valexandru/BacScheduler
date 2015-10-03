package com.example.kamiseto.bacscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class readActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);

        //read the file and set the text of the textview

        BufferedReader reader = null;

        try{
            FileInputStream noteFile = openFileInput("supernotes");

            reader = new BufferedReader(new InputStreamReader(noteFile));
            String eol = System.getProperty("line.separator");
            String line;
            StringBuilder sb = new StringBuilder();

            TextView tvNotes = (TextView) findViewById(R.id.notes);

            while((line = reader.readLine()) != null){
                sb.append(line).append(eol);
            }

            tvNotes.setText(sb.toString());

        }catch(FileNotFoundException e) {

            e.printStackTrace();
        } catch(IOException e){

            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}