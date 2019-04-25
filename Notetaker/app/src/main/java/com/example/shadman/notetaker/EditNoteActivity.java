package com.example.shadman.notetaker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shadman.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity {
    EditText titleET;
    EditText contentET;
    String initialTitle;
    String initialContent;
    String initialDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        titleET = (EditText) findViewById(R.id.editTitleET);
        contentET = (EditText) findViewById(R.id.contentET);

        initialTitle = getIntent().getStringExtra("Title").toString();
        initialContent = getIntent().getStringExtra("Content").toString();
        initialDate = getIntent().getStringExtra("Date").toString();

        titleET.setText(initialTitle);
        contentET.setText(initialContent);

        final DatabaseHandler dh = new DatabaseHandler(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionBT);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTitle, newContent, newDate;
                newTitle = titleET.getText().toString();
                newContent = contentET.getText().toString();
                newDate = getTimeAndDate();

                if (!newTitle.equals("") && !newContent.equals("")) {

                    ArrayList<Note> allNotes = dh.getAllNotes();

                    Note initialNote = new Note(initialTitle, initialContent, initialDate);
                    Note newNote = new Note(newTitle, newContent, newDate);


                    if (!initialNote.equals(newNote) && allNotes.contains(newNote)) {
                        cannotUpdate();
                    }
                    else{
                        dh.updateNote(newNote, initialTitle);
                        updatedNote();
                    }

                } else
                    Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void updatedNote()
    {
        Toast.makeText(getApplicationContext(), "Note Updated!", Toast.LENGTH_SHORT).show();
        titleET.setText("");
        contentET.setText("");
        finish();
    }

    protected void cannotUpdate()
    {
        Toast.makeText(getApplicationContext(), "Cannot Update. Note With This Title Already Exists", Toast.LENGTH_SHORT).show();
    }


    protected String getTimeAndDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String date = calendar.getTime().toString().replace("GMT+06:00 ", "");

        return date;
    }

}
