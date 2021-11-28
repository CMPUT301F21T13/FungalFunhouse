package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the activity used for adding new habits to the UserProfile habitlist
 * to be displayed in the Habits tab of HomeTabActivity
 */
public class AddHabitActivity extends AppCompatActivity
        implements AddHabitCalendarFragment.OnFragmentInteractionListener {

    private static final String TAG = "AddHabitActivity";

    // View declaration
    private EditText activityTitleEditText;
    private EditText dateToStartEditText;
    private EditText titleEditText;
    private EditText reasonEditText;
    private Chip sundayChip;
    private Chip mondayChip;
    private Chip tuesdayChip;
    private Chip wednesdayChip;
    private Chip thursdayChip;
    private Chip fridayChip;
    private Chip saturdayChip;
    private Button addStartDateButton;
    private Switch publicVisibilitySwitch;
    private Button finishButton;
    private TextView weekdaysTextView;

    // Variable declaration
    private String dateToStart;
    private boolean publicVisibility;
    private String usernameStr;
    private boolean editing;
    private String providedHID;
    private WeeklySchedule schedule;
    //private int arraySize;
    private Intent intent;

    // Firebase collection constants
    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_HABITS = "habits";

    // Habit document keys
    private static final String KEY_HABIT_TITLE = "title";
    private static final String KEY_HABIT_REASON = "reason";
    private static final String KEY_HABIT_PUBLIC_VISIBILITY = "publicVisibility";
    private static final String KEY_HABIT_HID = "hid";
    private static final String KEY_HABIT_DATE_TO_START = "dateToStart";
    //private static final String KEY_HABIT_LIST_POSITION = "listPosition";
    private static final String KEY_HABIT_WEEKDAYS = "weekdays";

    // Database declaration
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HomeTheme);
        setContentView(R.layout.add_habit_activity);

        db = FirebaseFirestore.getInstance();

        activityTitleEditText = findViewById(R.id.habit_header_edittext);
        activityTitleEditText.setFocusable(false);
        dateToStartEditText = findViewById(R.id.habit_datetostart_editText);
        dateToStartEditText.setFocusable(false);
        titleEditText = findViewById(R.id.habit_title_edittext);
        reasonEditText = findViewById(R.id.habit_reason_edittext);
        sundayChip = findViewById(R.id.habit_sunday_chip);
        mondayChip = findViewById(R.id.habit_monday_chip);
        tuesdayChip = findViewById(R.id.habit_tuesday_chip);
        wednesdayChip = findViewById(R.id.habit_wednesday_chip);
        thursdayChip = findViewById(R.id.habit_thursday_chip);
        fridayChip = findViewById(R.id.habit_friday_chip);
        saturdayChip = findViewById(R.id.habit_saturday_chip);
        addStartDateButton = findViewById(R.id.habit_addstartdate_button);
        publicVisibilitySwitch = findViewById(R.id.habit_publicVisibility_switch);
        finishButton = findViewById(R.id.habit_finish_button);
        weekdaysTextView = findViewById(R.id.habit_weekdays_textview);

        // Grab intent and all data from it
        intent = getIntent();
        usernameStr = intent.getStringExtra("user"); // grabs the current user
        editing = intent.getBooleanExtra("editing", false); //default is false for not editing
        //arraySize = intent.getIntExtra("list_size", -1); //list_size only passed when adding a new habit

        dateToStart = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        schedule = new WeeklySchedule();

        // Pulls up a fragment of a calendar to select start date
        dateToStartEditText.setText(dateToStart);// default value
        addStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHabitCalendarFragment fragment = calendarFragment();

                fragment.show(getSupportFragmentManager(), "SET_DATETOSTART");
            }
        });

        // Public visibility default is true
        publicVisibility = true;
        publicVisibilitySwitch.setChecked(true);
        publicVisibilitySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicVisibility = !publicVisibility;
            }
        });

        // Listeners for all the chips
        sundayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schedule.checkSunday()) {
                    schedule.removeSunday();
                } else {
                    schedule.addSunday();
                }
            }
        });

        mondayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schedule.checkMonday()) {
                    schedule.removeMonday();
                } else {
                    schedule.addMonday();
                }
            }
        });

        tuesdayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schedule.checkTuesday()) {
                    schedule.removeTuesday();
                } else {
                    schedule.addTuesday();
                }
            }
        });

        wednesdayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schedule.checkWednesday()) {
                    schedule.removeWednesday();
                } else {
                    schedule.addWednesday();
                }
            }
        });

        thursdayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schedule.checkThursday()) {
                    schedule.removeThursday();
                } else {
                    schedule.addThursday();
                }
            }
        });

        fridayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schedule.checkFriday()) {
                    schedule.removeFriday();
                } else {
                    schedule.addFriday();
                }
            }
        });

        saturdayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schedule.checkSaturday()) {
                    schedule.removeSaturday();
                } else {
                    schedule.addSaturday();
                }
            }
        });

        // If editing grab the selected habit from the database and fill parameters
        if (editing) {
            db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS)
                    .document(intent.getStringExtra("habitHID"))// Grab hid from intent only when editing
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String title = (String) documentSnapshot.getData().get(KEY_HABIT_TITLE);
                                String reason = (String) documentSnapshot.getData().get(KEY_HABIT_REASON);
                                String hid = (String) documentSnapshot.getData().get(KEY_HABIT_HID);
                                String startDate = (String) documentSnapshot.getData().get(KEY_HABIT_DATE_TO_START);
                                boolean publicVisibility = (boolean) documentSnapshot.getData()
                                        .get(KEY_HABIT_PUBLIC_VISIBILITY);
                                //long listPosition = (long) documentSnapshot.getData().get(KEY_HABIT_LIST_POSITION);
                                ArrayList<String> weekdays = (ArrayList<String>) documentSnapshot.getData()
                                        .get(KEY_HABIT_WEEKDAYS);

                                //Habit habit = new Habit(title, reason, hid, startDate, publicVisibility, listPosition, weekdays);
                                Habit habit = new Habit(title, reason, hid, startDate, publicVisibility, weekdays);
                                Log.d(TAG, habit.toString());
                                fillParameters(habit); // Fill in the parameters using the habit

                                // For Calendar Fragment to show proper date when pulled up
                                dateToStart = (String) documentSnapshot.getData().get("dateToStart");
                            } else {
                                Toast.makeText(AddHabitActivity.this, "Database Error, try again", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, e.toString());
                            Toast.makeText(AddHabitActivity.this, "Database Error, try again", Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        }

        // Finish button listener
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmptyParameters()) {
                    Toast.makeText(AddHabitActivity.this, R.string.fill_all_parameters_toast, Toast.LENGTH_LONG).show();
                } else if (!editing) { // Not editing, adding a new habit
                    Habit habit = new Habit();

                    habit.setTitle(titleEditText.getText().toString());
                    habit.setReason(reasonEditText.getText().toString());
                    habit.setDateToStart(dateToStart);
                    habit.setPublicVisibility(publicVisibility);
                    //habit.setListPosition(arraySize);
                    habit.setWeeklySchedule(schedule);

                    Map<String, Object> habitMap = new HashMap<>();
                    habitMap.put(KEY_HABIT_TITLE, habit.getTitle());
                    habitMap.put(KEY_HABIT_REASON, habit.getReason());
                    habitMap.put(KEY_HABIT_PUBLIC_VISIBILITY, habit.getPublicVisibility());
                    habitMap.put(KEY_HABIT_HID, habit.getHid());
                    habitMap.put(KEY_HABIT_DATE_TO_START, habit.getDateToStart());
                    //habitMap.put(KEY_HABIT_LIST_POSITION, habit.getListPosition());
                    habitMap.put(KEY_HABIT_WEEKDAYS, habit.weeklySchedule.getSchedule());

                    try{
                        db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS)
                                .document(habit.getHid()).set(habitMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "The data was submitted");
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, e.toString());
                                        Toast.makeText(AddHabitActivity.this, "Database Error, try again",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                        Toast.makeText(AddHabitActivity.this, "Database Error, try again",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else { // Editing a habit
                         // Grab hid from intent only when editing
                    providedHID = intent.getStringExtra("habitHID");

                    // habitMap used to update the contents of a habit document in the database
                    Map<String, Object> habitMap = new HashMap<>();
                    habitMap.put(KEY_HABIT_TITLE, titleEditText.getText().toString());
                    habitMap.put(KEY_HABIT_REASON, reasonEditText.getText().toString());
                    habitMap.put(KEY_HABIT_HID, providedHID);
                    habitMap.put(KEY_HABIT_PUBLIC_VISIBILITY, publicVisibility);
                    habitMap.put(KEY_HABIT_DATE_TO_START, dateToStart);
                    habitMap.put(KEY_HABIT_WEEKDAYS, schedule.getSchedule());

                    try{
                    db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS)
                            .document(providedHID).update(habitMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "The data was updated");
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.toString());
                                    Toast.makeText(AddHabitActivity.this, "Database Error, try again",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                        Toast.makeText(AddHabitActivity.this, "Database Error, try again",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }

                }

            }
        });


    }

    /**
     * Grabs the selected date from the AddHabitCalendarFragment
     * 
     * @param dateToStart The date provided in yyyy-MM-dd format from the calendar
     *                    fragment
     */
    @Override
    public void onConfirmPressed(String dateToStart) {
        this.dateToStart = dateToStart;
        dateToStartEditText.setText(this.dateToStart);
    }

    /**
     * Inserts the proper dateToStart in the bundle of the fragment returned in
     * order for the calendar widget to display the properly selected date
     * 
     * @return fragment with arguments in the bundle
     */
    public AddHabitCalendarFragment calendarFragment() {
        Bundle args = new Bundle();
        args.putString("dateToStart", dateToStart);

        AddHabitCalendarFragment fragment = new AddHabitCalendarFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Call when editing a habit, grabs the data from the database on that specific
     * habit and fills in the parameters (views) on the activity
     * 
     * @param habit The
     */
    public void fillParameters(Habit habit) {
        activityTitleEditText.setText(R.string.edit_habit_title);
        titleEditText.setText(habit.getTitle());
        reasonEditText.setText(habit.getReason());
        dateToStartEditText.setText(habit.getDateToStart());
        publicVisibilitySwitch.setChecked(habit.getPublicVisibility());

        // Set chips
        ArrayList<String> weekdays = habit.weeklySchedule.getSchedule();
        for (String day : weekdays) {
            switch (day) {
            case "Sunday":
                sundayChip.setChecked(true);
                schedule.addSunday();
                break;
            case "Monday":
                mondayChip.setChecked(true);
                schedule.addMonday();
                break;
            case "Tuesday":
                tuesdayChip.setChecked(true);
                schedule.addTuesday();
                break;
            case "Wednesday":
                wednesdayChip.setChecked(true);
                schedule.addWednesday();
                break;
            case "Thursday":
                thursdayChip.setChecked(true);
                schedule.addThursday();
                break;
            case "Friday":
                fridayChip.setChecked(true);
                schedule.addFriday();
                break;
            case "Saturday":
                saturdayChip.setChecked(true);
                schedule.addSaturday();
                break;
            }
        }
    }

    /**
     * Checks if one of the parameters that don't have a default value is not filled
     * in
     * 
     * @return true if there is an empty parameter, false otherwise
     */
    public boolean isEmptyParameters() {
        String title = titleEditText.getText().toString();
        String reason = reasonEditText.getText().toString();

        if(title.isEmpty()){
            titleEditText.setError("Empty Title");
        }
        if(reason.isEmpty()){
            reasonEditText.setError("Empty Reason");
        }
        if(schedule.checkAllFalse()){
            weekdaysTextView.setError("");
        }


        return title.isEmpty() || reason.isEmpty() || schedule.checkAllFalse();
    }

}
