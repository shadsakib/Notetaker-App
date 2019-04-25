package com.example.shadman.notetaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shadman.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Shadman on 1/22/2018.
 */

public class CustomAdapter extends ArrayAdapter implements Filterable{

    ArrayList<Note> filteredList;
    ArrayList<Note> allNotes;
    CustomFilter filter;
    String item;
    Note noteItem;
    String noteTitle;
    Context context;

    public CustomAdapter(Context context, ArrayList<Note> resources) {
        super(context, R.layout.activity_custom_note_view, resources);
        filteredList = resources;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View customView = convertView;

        if (customView == null) {
            //LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //customView = inflater.inflate(R.layout.activity_custom_note_view, null);
            customView = layoutInflater.inflate(R.layout.activity_custom_note_view, parent, false);
        }

        DatabaseHandler dh = new DatabaseHandler(getContext());
        allNotes = (ArrayList<Note>) dh.getAllNotes();

        //item = (String) getItem(position);
        TextView text1 = (TextView) customView.findViewById(R.id.textView);
        ImageView image = (ImageView) customView.findViewById(R.id.icon);
        TextView text2 = (TextView) customView.findViewById(R.id.dateText);
       try {
           noteItem = (Note) filteredList.get(position);

           if(noteItem !=null) {
               noteTitle = noteItem.getTitle().toString();

               String date = "Last Edited on " + getDate();

               text1.setText(noteTitle);
               text2.setText(date);
               image.setImageResource(R.drawable.icon_note);
           }
       }
       catch(Exception e)
       {
           //Toast.makeText(getContext(), "Exception", Toast.LENGTH_SHORT).show();
       }

       return customView;
    }



    private class ViewHolder {
        private TextView title;
        private TextView date;

    }

    public String getItemAtPosition(int position)
    {
        Note n = (Note) filteredList.get(position);
        return n.getTitle();
    }

    class CustomFilter extends Filter
    {
        int count;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();

                ArrayList<Note> filters = new ArrayList<Note>();
                count=0;

              for(Note n: allNotes) {
                  if(n.getTitle().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                      filters.add(n);
                      count++;
                  }
              }

                results.count = filters.size();
                results.values = filters;

            } else {
                    results.count = allNotes.size();
                    results.values = allNotes;

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0)
            {
                filteredList = (ArrayList<Note>) results.values;
                notifyDataSetChanged();
            }
            else
            {
                notifyDataSetInvalidated();
            }

        }


    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }

        return filter;
    }



    protected String getDate()
    {
        String d ="";
        for(Note n: allNotes)
            if (n.getTitle().equals(noteTitle)) return n.getDate();

        return d;
    }

}
