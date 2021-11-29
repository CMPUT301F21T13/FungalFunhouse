package com.example.habittracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * An Activity for adding attributes to a Habit Event
 * For comments, photographs and location
 */
public class AddEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    //initialize variables
    private String habitTitle;
    private String habitHid;
    private String usernameStr;
    private String currentDate;
    private Calendar calendar;
    private HabitEvent currentHabitEvent;

    //firestore references
    private String COLLECTION_USERS = "users";
    private String COLLECTION_HABITS = "habits";
    private String COLLECTION_EVENTS = "habitEvents";

    private String KEY_DONE = "done";
    private String KEY_LOCATION = "location";
    private String KEY_IMAGE = "image";
    private String KEY_COMMENT = "comment";
    private String TAG = "AddEventActivity";

    //xml layout variables
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

    //Google API services Variables
    GoogleApiAvailability googleApiAvailability;
    int resultCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HomeTheme);
        setContentView(R.layout.activity_add_event);

        googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);


        //If there is no Google play don't make the fragment
        if(resultCode == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.add_event_map);
            mapFragment.getMapAsync(this);
        }
        db = FirebaseFirestore.getInstance();
        currentHabitEvent = new HabitEvent();

        //Grab variables from intent
        try{
            habitHid = getIntent().getStringExtra("habit id");
            usernameStr = getIntent().getStringExtra("user");

        } catch (NullPointerException e){
            Log.e("AddEventActivity: ", "Could not get 'habit id', 'user' or 'date' from bundle" + e);
        }

        //Deals with different activites sending to AddEventActivity
        if(getIntent().getStringExtra("Flag") != null){
            //Edit Event and Add out of date Event
            currentDate = getIntent().getStringExtra("date");
        }else{
            //Add today's event
            currentDate = getIntent().getStringExtra("date");
            currentHabitEvent.setDone(true);
        }


        //initialize xml layout variables
        habitTitleTextView = findViewById(R.id.add_event_title);
        finishButton = findViewById(R.id.add_event_finish_button);
        commentEditText = findViewById(R.id.add_event_comment_edittext);
        photoButton = findViewById(R.id.add_event_photo_button);
        photoImageView = findViewById(R.id.add_event_image_imageview);
        mapsButton = findViewById(R.id.add_event_maps_button);
        mapsLayout = findViewById(R.id.add_event_placeholder_for_maps);


        //Load Variables
        loadHabit(habitHid);
        mapsLayout.setVisibility(View.GONE);

        //Load in Habit Event and it's attributes
        db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS)
                .document(habitHid).collection(COLLECTION_EVENTS).document(currentDate).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            currentHabitEvent.setDone((boolean)documentSnapshot.getData().get(KEY_DONE));
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
                                    if(resultCode == ConnectionResult.SUCCESS) {
                                        userMarker.setPosition(userPosition);
                                        mapsLayout.setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Habit Event failed to retrieve");
            }
        });


        //Set the current date to the passed in value
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(currentDate);
            calendar.setTime(date);
            currentHabitEvent.setDate(calendar);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //For startActivityOnResult of Camera and Maps Activities
        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    //Return Result of Camera
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    photoImageView.setImageBitmap(bitmap);
                    photoImageView.getLayoutParams().height = 400;
                    photoImageView.getLayoutParams().width = 300;

                    currentHabitEvent.setPhotograph(bitmap);
                } else if (result.getResultCode() == 56 && result.getData() != null) {
                    //Return Result of Maps
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

        //Sends user to Camera if permissions are granted
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(
                        AddEventActivity.this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                        activityLauncher.launch(intent);
                    } else {
                        Toast.makeText(AddEventActivity.this, "No Camera App Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                            Manifest.permission.CAMERA);
                }

            }
        });


        //Sends User to MapsActivity
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultCode == ConnectionResult.SUCCESS) {
                    Intent intent = new Intent(AddEventActivity.this, EventMapsActivity.class);
                    activityLauncher.launch(intent);
                }else{
                    Toast.makeText(AddEventActivity.this, "No Maps App Found", Toast.LENGTH_SHORT).show();

                }
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
     * Stores all added information to habit events
     * @param hid : The habit id to store in
     */
    public void finishAddEvent(String hid){
        habitTitleTextView = findViewById(R.id.add_event_title);
        if (!commentEditText.getText().equals("")){
            currentHabitEvent.setComment(commentEditText.getText().toString());
        }

        Serialization.writeHabitEvent(usernameStr, habitHid, currentHabitEvent);
    }


    /**
     * Initializes the google map inside this activity
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Edmonton and move the camera
        userMarker = googleMap.addMarker(new MarkerOptions().position(userPosition).title("You").draggable(true));
        userMarker.setTag(0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userPosition));

    }


    /**
     * Takes an inputted string and encodes it to be a base64 Bitmap
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

    /**
     * For asking a User for permission
     */
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

}