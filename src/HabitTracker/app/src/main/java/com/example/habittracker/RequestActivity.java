package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * This Activity handles the "User Search function" and allows user to search
 * A list of all users by inputting a filter word for usernames
 * This Activity also allows the sending of follow requests
 * To the selected user
 */
public class RequestActivity extends AppCompatActivity {

    Button backButton;
    Button searchButton;
    Button enterButton;
    EditText usernameText;
    ListView userList;
    ArrayAdapter<Profile> UserAdapter;
    ArrayList<Profile> profileList;
    ArrayList<Profile> filteredList;
    UserProfile currentUser;
    UserProfile selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);

        //test features
        currentUser = new UserProfile("user1");
        UserProfile user2 = new UserProfile("user2");
        UserProfile user3 = new UserProfile("user3");

        profileList = new ArrayList<Profile>();
        profileList.add(currentUser);
        profileList.add(user2);
        profileList.add(user3);


        // set ListView to give us selected_item
        userList = findViewById(R.id.user_list);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUser = (UserProfile) userList.getItemAtPosition(i);
            }
        });

        // Searches profileList using text entered in usernameText
        usernameText = findViewById(R.id.username_input);
        searchButton = findViewById(R.id.search_button);
        userList = findViewById(R.id.user_list);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filteredList = filterUsers(profileList, usernameText.getText().toString());
                UserAdapter = new ProfileListAdapterGrid(RequestActivity.this, filteredList);
                userList.setAdapter(UserAdapter);
            }
        });

        // sends a follow request from currentUser to selectedUser (from Listview)
        enterButton = findViewById(R.id.enter_button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedUser.requestInbox.addRequest(new FollowRequest(currentUser, selectedUser));
                //set up dialog that says "request sent"
            }
        });

        // Sends User back to HometabActivity
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestActivity.this, HomeTabActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This function filters usernames using a user inputted word
     * @param profiles  ArrayList<Profile>: an initial ArrayList of Profiles to be sorted from
     * @param username_to_search String: the word to be filtered
     * @return ArrayList<Profiles>: the final sorted list
     */
    public ArrayList<Profile> filterUsers(ArrayList<Profile> profiles, String username_to_search){
        ArrayList<Profile> filtered_users = new ArrayList<Profile>();
        for(Profile i : profiles){
            if (i.getUsername().contains(username_to_search) && i != currentUser){
                filtered_users.add(i);
            }
        }
        return filtered_users;
    }//end filterUsers
}