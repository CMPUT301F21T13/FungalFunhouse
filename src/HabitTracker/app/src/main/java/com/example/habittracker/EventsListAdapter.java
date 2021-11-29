package com.example.habittracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventsListAdapter extends ArrayAdapter<HabitEvent> {
        private Context context;
        private ArrayList<HabitEvent> events;
        private String usernameStr;
        private FirebaseFirestore db;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        private TextView eventsTextView;
        private Calendar calendar;

        private static final String TAG = "EVENTSADAPTER";
        private String COLLECTION_USERS = "users";
        private String COLLECTION_HABITS = "habits";
        private String COLLECTION_EVENTS = "habitEvents";

    public EventsListAdapter(@NonNull Context context, ArrayList<HabitEvent> events, String username) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
        this.usernameStr = username;
    }

    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.events_listview_content, parent, false);
        }
        eventsTextView = view.findViewById(R.id.events_listview_textview);
        HabitEvent event = events.get(position);
        eventsTextView.setText(sdf.format(event.getDate().getTime()));

        //load in each habit event (all dates)
        Log.d(TAG, "Habit Event: " + sdf.format(event.getDate().getTime()));

        return view;
    }

}
