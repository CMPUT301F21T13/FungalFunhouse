package com.example.habittracker;

import androidx.fragment.app.Fragment;

/**
 * This is a Fragment for the FRIENDS tab
 * that uses the xml file friends_fragment.xml
 */
public class FriendsFragment extends Fragment {
    public FriendsFragment(){
        super(R.layout.friends_fragment);
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
