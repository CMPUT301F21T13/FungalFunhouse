package com.example.habittracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProfileListAdapterGrid extends ArrayAdapter<UserProfile> {

    private ArrayList<UserProfile> profiles;
    private Context context;

    public ProfileListAdapterGrid(Context context, ArrayList<UserProfile> profiles){
        super(context, 0, profiles);
            this.context = context;
            this.profiles = profiles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.profile_list_content_home, parent,false);
        }

        Profile profile = profiles.get(position);

        TextView username = view.findViewById(R.id.username_text_grid);

        username.setText(profile.getUsername());

        return view;

    }

}
