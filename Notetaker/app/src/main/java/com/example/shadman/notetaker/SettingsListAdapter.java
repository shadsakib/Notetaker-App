package com.example.shadman.notetaker;

/**
 * Created by Shadman on 2/9/2018.
 */

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Filterable;
        import android.widget.TextView;

        import java.util.ArrayList;

public class SettingsListAdapter extends ArrayAdapter{

    String item;
    TextView title;

    public SettingsListAdapter(Context context, ArrayList<String> resources) {
        super(context, R.layout.settings_view, resources);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View customView = convertView;
        if (customView == null){
            customView = LayoutInflater.from(getContext()).inflate(R.layout.settings_view, parent, false);
        }

        item = getItem(position).toString();

        title = (TextView) customView.findViewById(R.id.settingsText);

        title.setText(item);

        return customView;
    }




}
