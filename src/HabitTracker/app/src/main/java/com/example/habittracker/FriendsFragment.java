package com.example.habittracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is a Fragment for the FRIENDS tab
 * This allows a user to view all followed users and their habits, daily and events
 * Go into their inbox to control follow requests
 * And go to a user search fragment, where requests can be sent
 */
public class FriendsFragment extends Fragment {
    public FriendsFragment(){
        super(R.layout.friends_fragment);
    }

    ListView friendsList;
    ArrayAdapter<String> friendsAdapter;
    ArrayList<String> friendsDataList;
    UserProfile currentUser;
    String currentUsername;
    UserProfile followedUser;
    FloatingActionButton requestButton;
    FloatingActionButton mailButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //test for Fragment Arguments
        Bundle bundle = getArguments();
        try {
            String usernameStr = bundle.get("user").toString();
        } catch (NullPointerException e) {
            Log.e("HabitsFragment: ", "Could not get 'user' from bundle" + e);
        }

        currentUsername = bundle.getString("user");
        //Initialize Variables
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        friendsList = view.findViewById(R.id.friends_list);
        requestButton = view.findViewById(R.id.send_request_activity_button);
        mailButton = view.findViewById(R.id.mail_inbox_activity_button);
        friendsDataList = new ArrayList<>();
        loadDataInList();


        //if user clicked, show their habits within the HabitsTab
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: query the User and pass into hometab
                db.collection("COLLECTION_USER").document(friendsAdapter.getItem(i)); //username of selected Use
                ((HomeTabActivity) getActivity()).OpenHabitsFragment(true, followedUser);
            }
        });


        // Sends user to the RequestInboxActivity
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestActivity.class);
                intent.putExtra("user", currentUsername);
                getActivity().startActivity(intent);
            }
        });


        // Sends User to the MailActivity
        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InboxActivity.class);
                intent.putExtra("user", currentUsername);
                getActivity().startActivity(intent);
            }
        });
        return view;

    }

    /**
     * This method is for loading in the followings of a User to be displayed by a ListView
     * If there are no active followers the method makes a Toast("No followings found")
     */
    private void loadDataInList(){
        db.collection("users").document(currentUsername).collection("following")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Toast.makeText(getActivity(), "Document retrieval successful", Toast.LENGTH_SHORT).show();
                                String username_db = d.getString("username");
                                friendsDataList.add(username_db);
                            }
                            friendsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.user_content, friendsDataList);
                            friendsList.setAdapter(friendsAdapter);
                        }else{
                            Toast.makeText(getActivity(), "No followings found", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error, query failed", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
