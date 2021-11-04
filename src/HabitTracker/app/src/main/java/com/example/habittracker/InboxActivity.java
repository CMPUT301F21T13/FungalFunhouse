package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    ArrayList<FollowRequest> requestDataList;
    ArrayAdapter<FollowRequest> requestAdapter;
    UserProfile currentUser;
    String currentUsername;
    FollowRequest selectedRequest;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_inbox);


        try{
            currentUsername = getIntent().getStringExtra("user");
        } catch (NullPointerException e){
            Log.e("InboxActivity: ", "Could not get 'user' from bundle" + e);
        }

        //initialize variables
        requestList = findViewById(R.id.request_list);
        acceptButton = findViewById(R.id.accept_button);
        denyButton = findViewById(R.id.deny_button);
        backButton = findViewById(R.id.back_button);
        acceptButton.setVisibility(View.GONE);
        denyButton.setVisibility(View.GONE);
        requestDataList = new ArrayList<>();
        loadDataInList();

        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRequest = requestAdapter.getItem(i);
                acceptButton.setVisibility(View.VISIBLE);
                denyButton.setVisibility(View.VISIBLE);
            }
        });

        acceptButton.setOnClickListener(view -> {
            if(selectedRequest != null) {
                currentUser.getInbox().acceptRequest(selectedRequest);
                requestAdapter.remove(selectedRequest);
            }
        });

        denyButton.setOnClickListener(view -> {
            if(selectedRequest != null) {
                currentUser.getInbox().denyRequest(selectedRequest);
                requestAdapter.remove(selectedRequest);
            }
        });
        //Sends the user back to HomeTabActivity
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(InboxActivity.this, HomeTabActivity.class);
            startActivity(intent);
        });

    }

    /**
     * This method is for loading in the FollowRequestInbox to be displayed by a ListView
     * If there are no active follow requests the method makes a Toast("No Follow Requests found")
     */
    private void loadDataInList(){
        db.collection("users").document(currentUsername).collection("followrequestinbox")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Toast.makeText(InboxActivity.this, "Document retrieval successful", Toast.LENGTH_SHORT).show();
                                String sender_db = d.getString("sender");
                                String target_db = d.getString("target");
                                requestDataList.add(new FollowRequest(sender_db, target_db));
                            }
                            requestAdapter = new RequestAdapter(InboxActivity.this, requestDataList);
                            requestList.setAdapter(requestAdapter);
                        }else{
                            Toast.makeText(InboxActivity.this, "No Follow Requests found", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InboxActivity.this, "Error, query failed", Toast.LENGTH_SHORT).show();
            }
        });


    }
}