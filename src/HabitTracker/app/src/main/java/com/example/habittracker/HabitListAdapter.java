package com.example.habittracker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

public class HabitListAdapter extends ArrayAdapter {

    private HabitList habits;
    private Context context;

    public HabitListAdapter(Context context, HabitList habits) {
        super(context, 0, habits.getList());
        this.context = context;
        this.habits = habits;
    }

    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View view = convertView;

        //TODO: When the interface for habits is created fill in this function

        return view;
    }

}//HabitListAdapter
