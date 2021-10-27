package com.example.habittracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a Fragment for the FRIENDS tab
 * that uses the xml file friends_fragment.xml
 */
public class FriendsFragment extends Fragment {
    public FriendsFragment(){
        super(R.layout.friends_fragment);
    }

    ListView friendsList;
    ArrayList<Profile> friendsDataList;
    ArrayAdapter<Profile> friendsAdapter;
    UserProfile currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        currentUser = (UserProfile) bundle.getParcelable("user");
        View view = inflater.inflate(R.layout.friends_fragment, container, false);

        friendsList = view.findViewById(R.id.friends_list);
        friendsDataList = new ArrayList<Profile>();
        friendsDataList.addAll((Collection<? extends Profile>) currentUser.getFollowing());
        Context context = getContext();
        friendsAdapter =  new ProfileAdapter(context, friendsDataList);
        friendsList.setAdapter(friendsAdapter);
        return view;

    }

    // Implement new Friends activity that shows all following
        // how do I show followers!!!!!

    // Selecting a user (from a listview) sends into new Activity
        // has to be new activity unless fragments are re-thought

    // selecting request leads to new activity
        // create new request inbox if none exists for user
        // followers are shown using general profileList
        // FollowRequestInbox.addRequest (request)

    // Selecting Mail leads to mew activity
        // must have a pre-existing request inbox to show anything
        // FollowerRequestInbox.acceptRequest or .denyRequest
}
