package com.example.shadman.notetaker;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


import com.example.shadman.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;


import static android.support.v7.appcompat.R.id.info;

public class ListViewActivity extends AppCompatActivity {

    ArrayList<String> allTitles;
    ArrayList<Note> allNotes;
    boolean sortedState;
    ListView listView;
    CustomAdapter myAdapter;
    SimpleCursorAdapter cursorAdapter;
    ArrayList<String> filteredTitles;
    String selectedNote;
    SearchView searchView;
    SearchView tsearchView;
    DatabaseHandler dh;
    Parcelable state;
    Parcelable listState;
    Runnable runnable;
    MenuItem search;
    MenuItem sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        dh = new DatabaseHandler(this);

        allNotes = dh.getAllNotes();

        allTitles = getTitles(allNotes);

        myAdapter = new CustomAdapter(getApplicationContext(), allNotes);

        listView = (ListView) findViewById(R.id.list);

        listView.setTextFilterEnabled(true);

        listView.setAdapter(myAdapter);
        registerForContextMenu(listView);

        searchView = (SearchView) findViewById(R.id.my_searchview);

        /*
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            myAdapter.getFilter().filter(query);
            Toast.makeText(this, "Typing", Toast.LENGTH_SHORT).show();
        }

        */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNote = myAdapter.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), NoteViewActivity.class);
                i.putExtra("Notename", selectedNote);
                startActivity(i);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<Note> newList = new ArrayList<Note>();
                for (Note n : allNotes) {
                    if (n.getTitle().toUpperCase().startsWith(s.toUpperCase())) {
                        newList.add(n);
                    }
                }

                myAdapter = new CustomAdapter(getApplicationContext(), newList);
                listView.setAdapter(myAdapter);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<Note> newList = new ArrayList<Note>();
                for (Note n : allNotes) {
                    if (n.getTitle().toUpperCase().startsWith(s.toUpperCase())) {
                        newList.add(n);
                    }
                }

                myAdapter = new CustomAdapter(getApplicationContext(), newList);
                listView.setAdapter(myAdapter);


                // myAdapter.getFilter().filter(s);

                //listView.refreshDrawableState();

                if (newList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No Note found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    search.setVisible(false);
                } else {
                    search.setVisible(true);
                }
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_search_menu, menu);

        search = menu.findItem(R.id.y);
        sort = menu.findItem(R.id.x);
        //tsearchView = (SearchView) menu.findItem(R.id.search_action);

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;

        selectedNote = myAdapter.getItemAtPosition(listPosition);

        if (item.getTitle() == "Edit") {
            goToEditNoteActivity();
        } else if (item.getTitle() == "Delete") {
            state = listView.onSaveInstanceState();
            dh.deleteNoteByTitle(selectedNote);
            deleteNote();
        } else {
            return false;
        }

        return true;
    }

    protected void goToEditNoteActivity() {
        Toast.makeText(this, "Edit Mode", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getApplicationContext(), EditNoteActivity.class);
        i.putExtra("Title", selectedNote);

        allNotes = dh.getAllNotes();
        String c = "";
        String d = "";

        for (Note n : allNotes) {
            if (n.getTitle().equals(selectedNote)) {
                c = n.getContents();
                d = n.getDate();
                break;
            }
        }

        i.putExtra("Content", c);
        i.putExtra("Date", d);
        startActivity(i);
    }

    protected void deleteNote() {
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        update();
        listView.onRestoreInstanceState(state);
    }

    protected void update() {
        allNotes = dh.getAllNotes();

        if (sortedState) Collections.sort(allTitles, String.CASE_INSENSITIVE_ORDER);

        if (sortedState) Collections.sort(allNotes, new CustomComparator());


        myAdapter = new CustomAdapter(getApplicationContext(), allNotes);
        listView.setAdapter(myAdapter);
    }


    protected ArrayList<String> getTitles(ArrayList<Note> notes) {
        ArrayList<String> titles = new ArrayList();
        for (Note n : notes)
            titles.add(n.getTitle());

        return titles;
    }

    @Override
    protected void onPause() {
        super.onPause();
        state = listView.onSaveInstanceState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();

        if (state != null)
            listView.onRestoreInstanceState(state);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.x:
                sortedState = !sortedState;
                update();
                item.setTitle(sortedState ? "Unsort" : "Sort");
                break;

            case R.id.y:
                searchView.onActionViewExpanded();
                searchView.requestFocus();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


}

