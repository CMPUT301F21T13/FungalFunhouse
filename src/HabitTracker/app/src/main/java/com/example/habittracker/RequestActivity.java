package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This Activity handles the "User Search function" and allows user to search
 * A list of all users by inputting a filter word for usernames
 * This Activity also allows the sending of follow requests
 * To the selected user
 */
public class RequestActivity extends AppCompatActivity {

    Button searchButton;
    Button enterButton;
    EditText usernameText;
    ListView userList;
    ArrayAdapter<Profile> userAdapter;
    ArrayList<Profile> userDataList;
    UserProfile currentUser;
    String currentUsername;
    UserProfile selectedUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HomeTheme);
        setContentView(R.layout.activity_request);

        try{
            currentUsername = getIntent().getStringExtra("user");
            currentUser = new UserProfile(currentUsername);
        } catch (NullPointerException e){
            Log.e("RequestActivity: ", "Could not get 'user' from bundle" + e);
        }


        // Initialize Variables
        usernameText = findViewById(R.id.username_input);
        searchButton = findViewById(R.id.search_button);
        enterButton = findViewById(R.id.enter_button);
        userList = findViewById(R.id.user_search_list);
        userDataList = new ArrayList<>();
        filterDataInList("");

        searchButton.setOnClickListener(view -> {
            if(usernameText.getText().toString() != null) {
                filterDataInList(usernameText.getText().toString());
            }else{
                filterDataInList("");
            }
        });

        userList.setOnItemClickListener((adapterView, view, i, l) -> selectedUser = (UserProfile) userList.getItemAtPosition(i));

        // sends a follow request from currentUser to selectedUser (from Listview)
        enterButton.setOnClickListener(view -> {
            //TODO: implement an addFollowRequest from currentUser to selectedUser
            FollowRequest request = new FollowRequest(currentUser.getUsername(), selectedUser.getUsername());
            selectedUser.getInbox().addRequest(request);
            Serialization.addRequest(request);
            Toast.makeText(RequestActivity.this, "Request sent", Toast.LENGTH_SHORT).show();
        });

    }

    /**
     * This method creates a filtered list of usernames that contain filtered_word
     * To be used in the ListView
     * @param filtered_text String: the word to filter usernames with
     */
    private void filterDataInList(@NonNull String filtered_text){
        userDataList.clear();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Request Activity", document.getId() + " => " + document.getData());
                                String username_db = document.getString("username");
                                if(username_db.contains(filtered_text) && !username_db.equals(currentUsername)){
                                    userDataList.add(new UserProfile(username_db));
                                }
                            }
                            userAdapter = new ProfileListAdapterGrid(RequestActivity.this,  userDataList);
                            userList.setAdapter(userAdapter);
                        } else {
                            Log.d("Request Activity", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }
}