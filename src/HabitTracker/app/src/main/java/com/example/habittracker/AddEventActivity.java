package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * An Activity for adding attributes to a Habit Event
 * For comments, photographs and location
 */
public class AddEventActivity extends AppCompatActivity {

    private String habitTitle;
    private String habitHid;
    private String usernameStr;
    private String currentDate;
    private Calendar calendar;
    private HabitEvent currentHabitEvent;

    private String COLLECTION_USERS = "users";
    private String COLLECTION_HABITS = "habits";
    private String COLLECTION_EVENTS = "habitEvents";
    private String TAG = "AddEventActivity";

    private Button finishButton;
    private TextView habitTitleTextView;
    private EditText commentEditText;
    private FirebaseFirestore db;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        db = FirebaseFirestore.getInstance();
        currentHabitEvent = new HabitEvent();

        try{
            habitHid = getIntent().getStringExtra("habit id");
            usernameStr = getIntent().getStringExtra("user");
            currentDate = getIntent().getStringExtra("date");
            Log.d(TAG, "The hid is " + habitHid);
            Log.d(TAG, "The username is " + usernameStr);
            Log.d(TAG, "The date is " + currentDate);

        } catch (NullPointerException e){
            Log.e("AddEventActivity: ", "Could not get 'habit id', 'user' or 'date' from bundle" + e);
        }

        //initialize variables
        habitTitleTextView = findViewById(R.id.add_event_title);
        finishButton = findViewById(R.id.add_event_finish_button);
        commentEditText = findViewById(R.id.add_event_comment_edittext);
        loadHabit(habitHid);

        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(currentDate);
            calendar.setTime(date);
            currentHabitEvent.setDate(calendar);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAddEvent(habitHid);
                Intent intent = new Intent(AddEventActivity.this, HomeTabActivity.class);
                intent.putExtra("user", usernameStr);
                startActivity(intent);
            }
        });



    }

    /**
     * Loads the current Habit and details needed
     * @param hid : the habit Id to be accessed in the database
     * @return
     */
    public String loadHabit(String hid){
        db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS).document(hid).get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to load the current habit");
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "Get Habit worked");
                habitTitle = (String) documentSnapshot.getData().get("title");
                habitTitleTextView.setText("Add Event to '" + habitTitle+"'");
            }
        });
        return habitTitle;
    }

    /**
     * Stores all added information to habit events
     * @param hid : The habit id to store in
     */
    public void finishAddEvent(String hid){
        habitTitleTextView = findViewById(R.id.add_event_title);
        if (!commentEditText.getText().equals("")){
            currentHabitEvent.setComment(commentEditText.getText().toString());
        }

        Serialization.addHabitEvent(usernameStr, habitHid, currentHabitEvent);
    }

}