package com.example.shadman.notetaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {


    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listView = (ListView) findViewById(R.id.setList);

        ArrayList<String> s = new ArrayList<String>();
        s.add("Build Version 1.0.0");

        SettingsListAdapter myAdapter = new SettingsListAdapter(getApplicationContext(), s);

        listView.setAdapter(myAdapter);

        setTitle("Settings");
    }
}
