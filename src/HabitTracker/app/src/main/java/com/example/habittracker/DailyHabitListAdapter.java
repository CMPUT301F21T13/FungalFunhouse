package com.example.habittracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailyHabitListAdapter extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;
    private Calendar calendar;
    private FirebaseFirestore db;
    private String KEY_LOCATION = "location";
    private String KEY_IMAGE = "image";
    private String KEY_COMMENT = "comment";

    public DailyHabitListAdapter(Context context, ArrayList<Habit> habits, Calendar calendar) {
        super(context, 0, habits);
        this.context = context;
        this.habits = habits;
        this.calendar = calendar;
    }

    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        db = FirebaseFirestore.getInstance();
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.daily_listview_content, parent, false);
        }

        Habit habit = habits.get(position);

        //Setup required Views (placeholders for photo and location)
        TextView dailyTitle = (TextView) view.findViewById(R.id.daily_listview_title);
        TextView dailyPhoto = (TextView) view.findViewById(R.id.daily_listview_photograph);
        TextView dailyLocation = (TextView) view.findViewById(R.id.daily_listview_location);
        TextView dailyComment = (TextView) view.findViewById(R.id.daily_listview_comment);

        habit.getHabitEventList();
        //fill in the TextViews
        dailyTitle.setText(habit.getTitle());

        //add in holders for all habit events variables afterwards


        return view;
    }



}
