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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;

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
    ArrayAdapter<Profile> friendsAdapter;
    UserProfile currentUser;
    UserProfile followedUser;
    FloatingActionButton requestButton;
    FloatingActionButton mailButton;

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

        currentUser = bundle.getParcelable("user");
        View view = inflater.inflate(R.layout.friends_fragment, container, false);

        //Create ListView for users
        if (currentUser.getFollowing() != null) {
            friendsList = view.findViewById(R.id.friends_list);
            Context context = getContext();
            friendsAdapter = new ProfileListAdapterGrid(context, currentUser.getFollowing());
            friendsList.setAdapter(friendsAdapter);
        }

        //if user clicked, show their habits within the HabitsTab
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                followedUser = (UserProfile) friendsAdapter.getItem(i);
                ((HomeTabActivity) getActivity()).OpenHabitsFragment(true, followedUser);
            }
        });


        // Sends user to the RequestInboxActivity
        requestButton = view.findViewById(R.id.send_request_activity_button);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestActivity.class);
                getActivity().startActivity(intent);
            }
        });


        // Sends User to the MailActivity
        mailButton = view.findViewById(R.id.mail_inbox_activity_button);
        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InboxActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return view;

    }
}
