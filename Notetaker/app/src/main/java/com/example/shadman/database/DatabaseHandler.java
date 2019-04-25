package com.example.shadman.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.shadman.notetaker.Note;

import java.util.ArrayList;

/**
 * Created by Shadman on 1/20/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION=2;

    public DatabaseHandler(Context context) {
        super(context,"NOTES",null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String  sql = "CREATE TABLE NOTES (ID INTEGER PRIMARY KEY"+ ", TITLE TEXT" + ", CONTENTS TEXT" +  ", DATE TEXT)";
        db.execSQL(sql);
        System.out.println(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // db.execSQL("DROP TABLE IF EXISTS "+"NOTES");
        //onCreate(db);
        String upgradeQuery = "ALTER TABLE NOTES ADD COLUMN DATE TEXT";
        String fillcolumnQuery = "UPDATE NOTES SET DATE = 'Jan 27 2018'";
        if(oldVersion==1 && newVersion==2) {
            db.execSQL(upgradeQuery);
            db.execSQL(fillcolumnQuery);
        }
    }

    public void addNote(Note note)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO NOTES(TITLE,CONTENTS,DATE)" + "VALUES('" + note.getTitle() + "','" + note.getContents()+ "','"+ note.getDate()+"')";

       // ContentValues value = new ContentValues();

       // value.put("TITLE", note.getTitle());
       // value.put("CONTENTS", note.getContents());

       // db.insert("Notes", null, value);

        db.execSQL(query);

        db.close();
    }

    public Note getSingleNote(int id, String title)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT TITLE, CONTENTS, DATE FROM CONTACT WHERE ID = "+id+" OR TITLE ='" +title+"'";
        Cursor cursor=db.rawQuery(query, null);
        Note myNote = null;
        if(cursor.moveToFirst())
        {
            myNote=new Note(cursor.getString(1),cursor.getString(2), cursor.getString(3));
        }

        return myNote;
    }

    public Note getNoteByTitle(String title)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT TITLE,CONTENTS,DATE FROM NOTES WHERE TITLE = '"+title+"'";

        Cursor cursor = db.rawQuery(query, null);

        Note myNote = null;

        if(cursor.getCount()>0)
            myNote = new Note(cursor.getString(1), cursor.getString(2),cursor.getString(3));

        return myNote;
    }

    public ArrayList<Note> getAllNotes()
    {
        ArrayList<Note> noteList = new ArrayList<Note>();

        String selectquery = "SELECT * FROM " +"Notes";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectquery, null);

        if(cursor.moveToFirst()){
            do{
                Note n = new Note(cursor.getString(1), cursor.getString(2), cursor.getString(3));
                noteList.add(n);
            }
            while(cursor.moveToNext());
        }

        return noteList;

    }


    public void deleteSingleNote(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query = "DELETE From "+"NOTES"+" WHERE ID="+id;
        db.execSQL(query);

        db.close();
    }

    public void deleteNoteByTitle(String title)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query = "Delete from "+"NOTES"+" where title= '"+title+"'";
        db.execSQL(query);

        db.close();
    }

    public void updateNote(Note newNote, String oldTitle)
    {
       SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE NOTES SET TITLE = '" + newNote.getTitle()+"' , CONTENTS = '"+newNote.getContents()
                +"' , DATE = '"+newNote.getDate()
                +"' WHERE TITLE= '"+oldTitle+"'";

        db.execSQL(query);

        db.close();
    }


    public void removeAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Notes", null, null);
    }

    public Object getItemID(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT ID FROM NOTES WHERE TITLE = '" +title +"'";
        Cursor cursor = db.rawQuery(query, null);

        Object id = null;
        if(cursor.moveToFirst()){
           id = Integer.parseInt(cursor.getString(0));
        }

        return id;
    }
}

