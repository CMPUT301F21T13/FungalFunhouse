package com.example.habittracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * An Activity for adding attributes to a Habit Event
 * For comments, photographs and location
 */
public class AddEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String habitTitle;
    private String habitHid;
    private String usernameStr;
    private String currentDate;
    private Calendar calendar;
    private HabitEvent currentHabitEvent;

    private String COLLECTION_USERS = "users";
    private String COLLECTION_HABITS = "habits";
    private String COLLECTION_EVENTS = "habitEvents";

    private String KEY_DONE = "done";
    private String KEY_LOCATION = "location";
    private String KEY_IMAGE = "image";
    private String KEY_COMMENT = "comment";
    private String TAG = "AddEventActivity";

    private Button finishButton;
    private Button photoButton;
    private Button mapsButton;
    private TextView habitTitleTextView;
    private EditText commentEditText;
    private ImageView photoImageView;
    private LinearLayout mapsLayout;
    private FirebaseFirestore db;
    private SimpleDateFormat sdf;

    private ActivityResultLauncher<Intent> activityLauncher;
    private Marker userMarker;
    private LatLng userPosition = new LatLng( 53.5461, -113.4938);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HomeTheme);
        setContentView(R.layout.activity_add_event);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.add_event_map);
        mapFragment.getMapAsync(this);

        db = FirebaseFirestore.getInstance();
        currentHabitEvent = new HabitEvent();

        //TODO Register Flags for "Edit" or "Incoming Data"
        //TODO Edit from ShowEvents must include a date feature
        try{
            habitHid = getIntent().getStringExtra("habit id");
            usernameStr = getIntent().getStringExtra("user");

        } catch (NullPointerException e){
            Log.e("AddEventActivity: ", "Could not get 'habit id', 'user' or 'date' from bundle" + e);
        }

        if(getIntent().getStringExtra("Flag") != null){
            currentDate = getIntent().getStringExtra("date");
        }else{
            currentDate = getIntent().getStringExtra("date");
        }
        //initialize variables
        habitTitleTextView = findViewById(R.id.add_event_title);
        finishButton = findViewById(R.id.add_event_finish_button);
        commentEditText = findViewById(R.id.add_event_comment_edittext);
        photoButton = findViewById(R.id.daily_listview_photo_button);
        photoImageView = findViewById(R.id.add_event_image_imageview);
        mapsButton = findViewById(R.id.add_event_maps_button);
        mapsLayout = findViewById(R.id.add_event_placeholder_for_maps);


        //Load Variables
        loadHabit(habitHid);
        loadHabitEvent();
        mapsLayout.setVisibility(View.GONE);

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
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    photoImageView.setImageBitmap(bitmap);
                    photoImageView.getLayoutParams().height = 400;
                    photoImageView.getLayoutParams().width = 300;

                    currentHabitEvent.setPhotograph(bitmap);
                } else if (result.getResultCode() == 56 && result.getData() != null) {
                    Log.d(TAG, "LOCATION FOUND");
                    Bundle bundle = result.getData().getExtras();
                    Double latitude = Double.parseDouble(bundle.get("latitude").toString());
                    Double longitude = Double.parseDouble(bundle.get("longitude").toString());
                    userPosition = new LatLng(latitude, longitude);
                    Log.d(TAG, "lat: " + latitude + " lon: " + longitude);
                    currentHabitEvent.setLocation(userPosition);
                    mapsLayout.setVisibility(View.VISIBLE);
                    userMarker.setPosition(userPosition);
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
                activityLauncher.launch(intent);
            }
        });

        //Finishes the Add Event Activity by sending user back to HomeTab
        //and writing the habit event to the database;
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAddEvent(habitHid);
                Intent intent = new Intent();
                intent.putExtra("user", usernameStr);
                setResult(0, intent);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
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

                            //If there is an image, show it
                            if (documentSnapshot.getData().get(KEY_IMAGE) != null) {
                                Bitmap bm = StringToBitMap(documentSnapshot.getData().get(KEY_IMAGE).toString());
                                photoImageView.setImageBitmap(bm);
                                photoImageView.setVisibility(View.VISIBLE);
                                currentHabitEvent.setPhotograph(bm);
                                photoImageView.getLayoutParams().height = 400;
                                photoImageView.getLayoutParams().width = 300;
                            }

                            //If there is a location show it
                            if(documentSnapshot.getData().get(KEY_LOCATION) != null){
                                Map<String, Double> position = (Map<String, Double>) documentSnapshot.getData().get(KEY_LOCATION);
                                double latitude = position.get("latitude");
                                double longitude = position.get("longitude");
                                if(latitude != 0 && longitude != 0) {
                                    userPosition = new LatLng(latitude, longitude);
                                    currentHabitEvent.setLocation(userPosition);
                                    mapsLayout.setVisibility(View.VISIBLE);
                                    userMarker.setPosition(userPosition);
                                }
                            }

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
        }

        Serialization.writeHabitEvent(usernameStr, habitHid, currentHabitEvent);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Edmonton and move the camera
        userMarker = googleMap.addMarker(new MarkerOptions().position(userPosition).title("You").draggable(true));
        userMarker.setTag(0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userPosition));

    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}