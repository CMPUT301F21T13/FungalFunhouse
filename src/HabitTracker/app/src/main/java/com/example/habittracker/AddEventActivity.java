package com.example.habittracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private String KEY_COMMENT = "comment";
    private String TAG = "AddEventActivity";

    private Button finishButton;
    private Button photoButton;
    private Button mapsButton;
    private TextView habitTitleTextView;
    private EditText commentEditText;
    private ImageView photoImageView;
    private FirebaseFirestore db;
    private SimpleDateFormat sdf;

    private ActivityResultLauncher<Intent> activityLauncher;


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
        photoButton = findViewById(R.id.daily_listivew_photo_button);
        photoImageView = findViewById(R.id.add_event_image_imageview);
        mapsButton = findViewById(R.id.add_event_maps_button);


        //Load Variables
        loadHabit(habitHid);
        loadHabitEvent();

        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(currentDate);
            calendar.setTime(date);
            currentHabitEvent.setDate(calendar);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    photoImageView.setImageBitmap(bitmap);
                    photoImageView.getLayoutParams().height = 400;
                    photoImageView.getLayoutParams().width = 300;
                }
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    activityLauncher.launch(intent);
                }else{ Toast.makeText(AddEventActivity.this, "No Camera App Found", Toast.LENGTH_SHORT).show();}
                }
        });


        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEventActivity.this, EventMapsActivity.class);
                startActivity(intent);
            }
        });


        //Finishes the Add Event Activity by sending user back to HomeTab
        //and writing the habit event to the database;
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
     * Loads in a specific habit event instance
     */
    public void loadHabitEvent(){
        db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS)
                .document(habitHid).collection(COLLECTION_EVENTS).document(currentDate).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            commentEditText.setText(documentSnapshot.getData().get(KEY_COMMENT).toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    /**
     * Stores all added information to habit events
     * @param hid : The habit id to store in
     */
    public void finishAddEvent(String hid){
        habitTitleTextView = findViewById(R.id.add_event_title);
        if (!commentEditText.getText().equals("")){
            currentHabitEvent.setComment(commentEditText.getText().toString());
            currentHabitEvent.setDone(true);
            Bitmap bm = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
            currentHabitEvent.setPhotograph(bm);
        }

        Serialization.writeHabitEvent(usernameStr, habitHid, currentHabitEvent);
    }

}