package com.example.shadman.notetaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.shadman.database.DatabaseHandler;
import java.util.ArrayList;

public class NoteViewActivity extends AppCompatActivity {

    String name;
    TextView title;
    TextView mainText;
    ArrayList<Note> allNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        title = (TextView) findViewById(R.id.textView1);
        mainText = (TextView) findViewById(R.id.textView2);

        name = (String) getIntent().getStringExtra("Notename");

        final DatabaseHandler dh = new DatabaseHandler(getApplicationContext());
        allNotes = dh.getAllNotes();


        title.setText("\"" + name + "\"");
        mainText.setText(findNote().getContents());

    }

    protected Note findNote()
    {
        Note note= null;
        for(Note n: allNotes)
            if(n.getTitle().equals(name)){
                note = n;
                break;
            }
        return note;
    }

}
