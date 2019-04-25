package com.example.shadman.notetaker;

import java.util.Comparator;

/**
 * Created by Shadman on 2/9/2018.
 */

public class CustomComparator implements Comparator<Note>
{
    @Override
    public int compare(Note o1, Note o2) {

        String str1 = o1.getTitle();
        String str2 = o2.getTitle();

        int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);

        if (res == 0) res = str1.compareTo(str2);

        return res;
    }
}