package com.example.shadman.notetaker;

import java.io.Serializable;

/**
 * Created by Shadman on 1/20/2018.
 */

public class Note
{
    String title;
    String contents;
    String date;

    public Note(String title, String contents, String date){
        this.title = title;
        this.contents = contents;
        this.date = date;
    }

    public String getTitle(){
      return title;
    }

    public String getContents(){
        return contents;
    }

    @Override
    public boolean  equals (Object object)
    {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            Note n = (Note) object;
            if (this.title.equals(n.getTitle())) {
                result = true;
            }
        }
        return result;
    }

    public String getDate() {return date;}
}
