package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the activity used for adding new habits to the UserProfile habitlist
 * to be displayed in the Habits tab of HomeTabActivity
 */
public class AddHabitActivity extends AppCompatActivity implements AddHabitCalendarFragment.OnFragmentInteractionListener {

    //Database and View declaration
    Serialization db;
    private TextView activityTitleTextView;
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
    private FloatingActionButton backActionButton;

    //Variable declaration
    private String dateToStart;
    private boolean publicVisibility;
    private String usernameStr;
    private boolean editing;
    private String providedHabitTitle;
    private WeeklySchedule schedule;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habit_activity);

        activityTitleTextView = findViewById(R.id.habit_header_textview);
        dateToStartEditText = findViewById(R.id.habit_datetostart_editText);
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
        backActionButton = findViewById(R.id.habit_back_floatingbutton);

        db = Serialization.getInstance();

        Intent intent = getIntent();
        //usernameStr = intent.getStringExtra("user"); //grabs the current user

        usernameStr = "mockUser";

        editing = intent.getBooleanExtra("editing", false);
        if(editing) {
            activityTitleTextView.setText(R.string.edit_habit_title);
        }


        dateToStart = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        schedule = new WeeklySchedule();

        //Pulls up a fragment of a calendar to select start date
        dateToStartEditText.setText(dateToStart);//default value
        addStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddHabitCalendarFragment().show(getSupportFragmentManager(), "SET_DATETOSTART");
            }
        });

        //Public visibility default is true
        publicVisibility = true;
        publicVisibilitySwitch.setChecked(true);
        publicVisibilitySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicVisibility = !publicVisibility;
            }
        });

        //Listeners for all the chips
        sundayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(schedule.checkSunday()){
                    schedule.removeSunday();
                } else {
                    schedule.addSunday();
                }
            }
        });
        mondayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(schedule.checkMonday()) {
                    schedule.removeMonday();
                } else {
                    schedule.addMonday();
                }
            }
        });
        tuesdayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(schedule.checkTuesday()) {
                    schedule.removeTuesday();
                } else {
                    schedule.addTuesday();
                }
            }
        });
        wednesdayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(schedule.checkWednesday()) {
                    schedule.removeWednesday();
                } else {
                    schedule.addWednesday();
                }
            }
        });
        thursdayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(schedule.checkThursday()) {
                    schedule.removeThursday();
                } else {
                    schedule.addThursday();
                }
            }
        });
        fridayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(schedule.checkFriday()) {
                    schedule.removeFriday();
                } else {
                    schedule.addFriday();
                }
            }
        });
        saturdayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(schedule.checkSaturday()) {
                    schedule.removeSaturday();
                } else {
                    schedule.addSaturday();
                }
            }
        });

        //TODO(GLENN): create fill parameter method to fill in the parameters from the selected habit

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isEmptyParameters()){
                    Toast.makeText(AddHabitActivity.this, "Fill all parameters", Toast.LENGTH_LONG).show();
                }
                else if( !editing ) { //Not editing, adding a new habit
                    Habit habit = new Habit();

                    habit.setTitle(titleEditText.getText().toString());
                    habit.setReason(reasonEditText.getText().toString());
                    habit.setDateToStart(dateToStart);
                    habit.setPublicVisibility(publicVisibility);
                    habit.setWeeklySchedule(schedule);

                    if(db.addHabit(usernameStr, habit)){
                        finish();
                    }
                    else {
                        Toast.makeText(AddHabitActivity.this, "Database Error, try again", Toast.LENGTH_LONG).show();
                    }

                }
                else { //Editing a habit
                    providedHabitTitle = intent.getStringExtra("habitTitle");

                }


            }
        });

        backActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onConfirmPressed(String dateToStart) {
        this.dateToStart = dateToStart;
        dateToStartEditText.setText(this.dateToStart);
    }

    public void fillParameters(Habit habit) {

    }

    /**
     * Checks if one of the parameters that don't have a default value is not filled in
     * @return true if there is an empty parameter, false otherwise
     */
    public boolean isEmptyParameters() {
        String title = titleEditText.getText().toString();
        String reason = reasonEditText.getText().toString();

        return title.isEmpty() || reason.isEmpty() || schedule.checkAllFalse();
    }

}
