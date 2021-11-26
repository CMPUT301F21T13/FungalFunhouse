package com.example.habittracker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Date;

/**
 * This is a Fragment for the DAILY tab
 * that uses the xml file daily_fragment.xml
 */
public class DailyFragment extends Fragment {
    public DailyFragment(){
        super(R.layout.daily_fragment);
    }

    //declare variables
    ListView dailyListView;
    private HabitListAdapter dailyListAdapter;
    private ArrayList<Habit> dailyDataList;
    private UserProfile currentUser;
    private String usernameStr;
    private Calendar calendar;
    private Date dateToCheck;
    private FirebaseFirestore db;

    private static final String TAG = "DailyFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.daily_fragment, container, false);

        //initialize variables
        dailyListView = view.findViewById(R.id.daily_listview);
        calendar = Calendar.getInstance();
        dateToCheck = calendar.getTime();

        // Grab the username of the current logged in user
        Bundle bundle = getArguments();
        try {
            currentUser = bundle.getParcelable("user");
        } catch (NullPointerException e) {
            Log.e(TAG, "Could not get 'user' from bundle" + e);
        }


        usernameStr = currentUser.getUsername();
        dailyDataList = new ArrayList<>();

        // Grab all of the habits from the database and fill the ListView
        // Use a snapshot listener so whenever the database is updated so is the app
        db.collection("users").document(usernameStr).collection("habits")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        dailyDataList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Log.d(TAG, doc.getId());
                            String title = (String) doc.getData().get("title");
                            String reason = (String) doc.getData().get("reason");
                            String hid = (String) doc.getData().get("hid");
                            String dateToStart = (String) doc.getData().get("dateToStart");
                            boolean publicVisibility = (boolean) doc.getData().get("publicVisibility");
                            ArrayList<String> weekdays = (ArrayList<String>) doc.getData().get("weekdays");

                            Habit habit = new Habit(title, reason, hid, dateToStart, publicVisibility, weekdays);
                            Log.d(TAG, habit.toString());

                            if(habitHappensToday(habit, dateToCheck)) {
                                dailyDataList.add(habit);
                            }

                            Context context = getContext();
                            dailyListAdapter = new HabitListAdapter(context, dailyDataList);
                            dailyListView.setAdapter(dailyListAdapter);
                        }
                    }
                });


        return view; }

    /**
     * Tests if the habit inputted occurs during the
     * inputted date
     * @param habit : the habit we are checking
     * @param currentDate : the day we are checking for
     * @return true if the habit happened at this date, false if not
     */
    public boolean habitHappensToday(Habit habit, Date currentDate){
        //Initialize Variables
        String currentDayOfWeek;
        Date habitStartDate;

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        currentDayOfWeek = simpleDateformat.format(currentDate);

        //Grab startDate from habit
        try {
            habitStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(habit.getDateToStart());
        } catch (ParseException e) {
            Log.d(TAG, "Start-Date failed to parse: " + habit.getDateToStart());
            return false;
        }

        //Grab current Date's day of the week position

        //Test the two dates against each other
        if(currentDate.before(habitStartDate)){
            Log.d(TAG, "This date occurs before habit's start date");
            return false;
        }else{
            //check if the weekdays match up;
            ArrayList<String> habitWeeklySchedule = habit.getWeeklySchedule().getSchedule();
            for (String habitDayOfWeek : habitWeeklySchedule){
                if(currentDayOfWeek.equals(habitDayOfWeek)){
                    Log.d(TAG, "This date works");
                    return true;
                }
            }
        }

        Log.d(TAG, "Day of the Week is not selected for this day");
        return false;
        }
}
