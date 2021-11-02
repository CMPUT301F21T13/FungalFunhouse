package com.example.habittracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.crypto.Cipher;

/**
 * This is the activity used for adding new habits to the UserProfile habitlist
 * to be displayed in the Habits tab of HomeTabActivity
 */
public class AddHabitActivity extends AppCompatActivity implements AddHabitCalendarFragment.OnFragmentInteractionListener {

    //TODO(GLENN): Add all functionality for xml layout

    //Database and View declaration
    FirebaseFirestore db;
    private TextView activityTitleTextView;
    private TextView dateToStartTextView;
    private EditText titleEditText;
    private EditText habitEditText;
    private Chip sundayChip;
    private Chip mondayChip;
    private Chip tuesdayChip;
    private Chip wednesdayChip;
    private Chip thursdayChip;
    private Chip fridayChip;
    private Chip saturdayChip;
    private Button addStartDateButton;
    private Button finishButton;
    private FloatingActionButton backActionButton;

    //Variable declaration
    private String dateToStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habit_activity);

        activityTitleTextView = findViewById(R.id.habit_header_textview);
        dateToStartTextView = findViewById(R.id.habit_datetostart_textview);
        titleEditText = findViewById(R.id.habit_title_edittext);
        habitEditText = findViewById(R.id.habit_reason_edittext);
        sundayChip = findViewById(R.id.habit_sunday_chip);
        mondayChip = findViewById(R.id.habit_monday_chip);
        tuesdayChip = findViewById(R.id.habit_tuesday_chip);
        wednesdayChip = findViewById(R.id.habit_wednesday_chip);
        thursdayChip = findViewById(R.id.habit_thursday_chip);
        fridayChip = findViewById(R.id.habit_friday_chip);
        saturdayChip = findViewById(R.id.habit_saturday_chip);
        addStartDateButton = findViewById(R.id.habit_addstartdate_button);
        finishButton = findViewById(R.id.habit_finish_button);
        backActionButton = findViewById(R.id.habit_back_floatingbutton);

        db = FirebaseFirestore.getInstance();

        //Pulls up a fragment of a calendar to select start date
        addStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddHabitCalendarFragment().show(getSupportFragmentManager(), "SET_DATETOSTART");
            }
        });

    }

    @Override
    public void onConfirmPressed(String dateToStart) {
        this.dateToStart = dateToStart;
        dateToStartTextView.setText(this.dateToStart);
    }

}
