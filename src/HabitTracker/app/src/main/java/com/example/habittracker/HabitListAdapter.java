package com.example.habittracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Custom adapter for the listview in habit_fragment, uses habit_listview_context
 */
public class HabitListAdapter extends ArrayAdapter<Habit>{

    private ArrayList<Habit> habits;
    private Context context;

    public HabitListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.context = context;
        this.habits = habits;
    }

    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.events_habit_listview_content, parent, false);
        }

        Habit habit = habits.get(position);

        //Setup required TextViews
        TextView habitTitle = (TextView) view.findViewById(R.id.events_habit_title);
        TextView habitReason = (TextView) view.findViewById(R.id.events_habit_reason);
        TextView habitDateToStart = (TextView) view.findViewById(R.id.events_habit_datetostart);
        TextView habitWeekdays = (TextView) view.findViewById(R.id.events_habit_weekdays);

        //fill in the TextViews
        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        habitDateToStart.setText(habit.getDateToStart());
        String weekdays = habit.getWeeklySchedule().getSchedule().toString().replace("[", "").replace("]", "");
        habitWeekdays.setText(weekdays);

        //Hide extra info until item is clicked
        habitReason.setVisibility(View.GONE);
        habitDateToStart.setVisibility(View.GONE);
        habitWeekdays.setVisibility(View.GONE);

        return view;
    }

}//HabitListAdapter
