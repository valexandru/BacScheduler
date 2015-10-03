package com.example.kamiseto.bacscheduler;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    public String titlex;
    public String notex;
    public String x;
    Boolean ok=false;

    ListView lista;
    List<String> android_versions = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        super.onCreate(savedInstanceState);

        int ct = 0;

        //cream lista de notite
        lista = (ListView) rootView.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, android_versions);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), parent.getItemAtPosition(position)+" is selected", Toast.LENGTH_LONG).show();
            }
        });

        //1. get text from two input fields
        final EditText edTitle = (EditText) rootView.findViewById(R.id.edTitle);
        String title = edTitle.getText().toString();

        final EditText edNote = (EditText) rootView.findViewById(R.id.edNote);
        String note = edNote.getText().toString();

        //2. on click, save the data into the internal storage
        Button btnSave = (Button) rootView.findViewById(R.id.button1);


        final String finalTitle = title;
        final String finalNote = note;
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                titlex = edTitle.getText().toString();
                notex = edNote.getText().toString();
                Toast.makeText(getActivity(), titlex, Toast.LENGTH_LONG).show();
                BufferedWriter writer = null;
                //if (notex!=null) android_versions.set(1,notex);


                try {
                    FileOutputStream openedFile = getActivity().openFileOutput("supernotes", Context.MODE_WORLD_WRITEABLE);

                    writer = new BufferedWriter(new OutputStreamWriter(openedFile));

                    if (notex != null)
                        if (!notex.isEmpty() || notex != "") {
                            if (ok)
                            {
                                adapter.clear();
                                ok=false;
                            }
                            android_versions.add(notex);
                            //adapter.add(notex);
                            adapter.notifyDataSetChanged();
                        }

                    String eol = System.getProperty("line.separator");
                    writer.append(titlex + eol);
                    writer.append(notex + eol);
                    writer.close();

                    //ct++;


                    //go to ReadActivity to read the file
                    //gotoRead();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        });

        if (android_versions.size() == 0) {
            android_versions.add("Nu sunt notite de afisat!");
            ok = true;
        }

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android_versions);
        lista.setAdapter(adapter);

        note = notex;
        title = titlex;

        //3. after saving, we will read the data
//        BufferedWriter writer = null;
//
//        try {
//            FileOutputStream openedFile = openFileOutput("supernotes", MODE_WORLD_WRITEABLE);
//
//            writer = new BufferedWriter(new OutputStreamWriter(openedFile));
//
//            String eol = System.getProperty("line.separator");
//            writer.write(title + eol);
//            writer.write(note + eol);
//            writer.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return rootView;
    }

    private void gotoRead() {
        Intent i = new Intent(getActivity(), readActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

