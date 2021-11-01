package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * An Activity for handling incoming follow requests
 * Grants the user the ability to accept or deny requests
 * Implements the user of FollowRequestInbox
 */
//modify later to include accept and reject buttons on the actual listview?
public class InboxActivity extends AppCompatActivity {

    Button backButton;
    Button acceptButton;
    Button denyButton;
    ListView requestList;
    ArrayAdapter<FollowRequest> requestAdapter;
    UserProfile currentUser;
    FollowRequest selectedRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_inbox);

        //For testing purposes [REPLACE AFTER DATABASE SERIALIZATION IS COMPLETE]
        currentUser = new UserProfile("user1");
        requestList = findViewById(R.id.request_list);
        requestAdapter = new RequestAdapter(this, currentUser.getRequestInbox().getRequests());
        requestList.setAdapter(requestAdapter);

        acceptButton = findViewById(R.id.accept_button);
        denyButton = findViewById(R.id.deny_button);
        if(requestAdapter.isEmpty()){
            acceptButton.setVisibility(View.INVISIBLE);
            denyButton.setVisibility(View.INVISIBLE);
        }

        requestList.setOnItemClickListener((adapterView, view, i, l) -> selectedRequest = requestAdapter.getItem(i));

        acceptButton.setOnClickListener(view -> {
            currentUser.getRequestInbox().acceptRequest(selectedRequest);
            requestAdapter.remove(selectedRequest);
        });

        denyButton.setOnClickListener(view -> {
            currentUser.getRequestInbox().denyRequest(selectedRequest);
            requestAdapter.remove(selectedRequest);
        });
        //Sends the user back to HomeTabActivity
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(InboxActivity.this, HomeTabActivity.class);
            startActivity(intent);
        });

    }
}