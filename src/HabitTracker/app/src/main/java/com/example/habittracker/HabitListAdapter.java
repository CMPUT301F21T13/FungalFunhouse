package com.example.habittracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

/**
 * Custom adapter for the listview in habit_fragment, uses habit_listview_context
 *
 * NOTE: In the storyboard we show that the items in the list will expand when you click
 *      on them, for now they do not expand. Instead they just show all the content.
 */
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

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.habit_listview_content, parent, false);
        }

        Habit habit = habits.getHabit(position);

        //Setup required TextViews
        TextView habitTitle = (TextView) view.findViewById(R.id.habit_title);
        TextView habitReason = (TextView) view.findViewById(R.id.habit_reason);
        TextView habitDateToStart = (TextView) view.findViewById(R.id.habit_datetostart);
        TextView habitWeekdays = (TextView) view.findViewById(R.id.habit_weekdays);

        //fill in the TextViews
        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        habitDateToStart.setText(habit.getDateToStart());
        habitWeekdays.setText(habit.getWeeklySchedule().getSchedule().toString()); //getSchedule().toString() needs to be tested

        return view;
    }

}//HabitListAdapter