package com.example.shadman.notetaker;

        import android.content.Intent;
        import android.content.res.Configuration;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.NavigationView;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.KeyEvent;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Toast;
        import com.example.shadman.database.DatabaseHandler;
        import java.util.ArrayList;
        import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    EditText mainText;
    EditText titleText;
    FloatingActionButton saveButton;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    String activityTitle;
    ArrayList<Note> allNotes;
    String title;
    String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("NoteKeeper");

        mainText = (EditText) findViewById(R.id.mainET);
        titleText = (EditText) findViewById(R.id.titleET);

        saveButton = (FloatingActionButton) findViewById(R.id.imageBT);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setupDrawer();
        drawerLayout.setVisibility(View.GONE);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final DatabaseHandler dh = new DatabaseHandler(this);

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        title = titleText.getText().toString();
                        content = mainText.getText().toString();

                        if (!title.equals("") && !content.equals("")) {

                            allNotes = dh.getAllNotes();

                            Note note = new Note(title, content, getTimeAndDate());

                            if (allNotes.contains(note)) {
                                noteAlreadyExists();
                            } else {
                                dh.addNote(note);
                                noteSaved();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

        mainText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT)) {
                    title = titleText.getText().toString();
                    content = mainText.getText().toString();

                    if (!title.equals("") && !content.equals("")) {

                        allNotes = dh.getAllNotes();

                        Note note = new Note(title, content, getTimeAndDate());

                        if (allNotes.contains(note)) {
                            noteAlreadyExists();
                        } else {
                            dh.addNote(note);
                            noteSaved();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //if(menuItem.isChecked())
                        //menuItem.setChecked(false);
                        //else
                        // menuItem.setChecked(true);


                        if (menuItem.getItemId() == R.id.d) {
                            drawerLayout.closeDrawers();
                            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(i);

                        } else if (menuItem.getItemId() == R.id.b) {
                            drawerLayout.closeDrawers();
                            Intent i = new Intent(getApplicationContext(), ListViewActivity.class);
                            startActivity(i);
                        } else {
                            //return false;
                        }


                        return true;
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        drawerLayout.setVisibility(View.VISIBLE);

        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    protected void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //drawerLayout.bringToFront();
                //getSupportActionBar().setTitle("Navigation Drawer");
                invalidateOptionsMenu();
                drawerLayout.setVisibility(View.VISIBLE);
            }


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu();
                drawerLayout.setVisibility(View.GONE);
            }

        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);

    }

    protected void noteAlreadyExists() {
        Toast.makeText(getApplicationContext(), "Note With Same Title Already Exists", Toast.LENGTH_SHORT).show();
    }

    protected void noteSaved() {
        Toast.makeText(getApplicationContext(), "Note Saved!", Toast.LENGTH_SHORT).show();
        titleText.setText("");
        mainText.setText("");
    }

    protected String getTimeAndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String date = calendar.getTime().toString().replace("GMT+06:00 ", "");

        return date;
    }

}