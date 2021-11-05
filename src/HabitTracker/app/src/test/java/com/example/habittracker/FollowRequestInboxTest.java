package com.example.habittracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a test method for FollowRequestInbox
 */
public class FollowRequestInboxTest {

    private FollowRequestInbox mockFollowInbox(){
        FollowRequestInbox mockInbox = new FollowRequestInbox(new UserProfile("testUser1"));
        return mockInbox;
    }

    /**
     * This method tests if a follow request once accepted
     * Adds the user as a Follower to the requested User
     */
    @Test
    public void acceptRequestTest(){
        //initialize test variables
        FollowRequestInbox mockInbox = mockFollowInbox();
        String sender_f = "testUser1";
        String target_f = "testUser2";
        boolean followerExists = false;
        FollowRequest acceptRequest = new FollowRequest(sender_f, target_f);
        int mockSize = mockInbox.getRequests().size();
        mockInbox.addRequest(acceptRequest);

        mockInbox.acceptRequest(acceptRequest);

        //Our request should be removed
        assertEquals(mockSize, mockInbox.getRequests().size());

        for(String follower : mockInbox.getOwner().getFollowers()){
            if(follower.equals(sender_f)){
                followerExists = true;
            }
        }
        assertTrue(followerExists);

    }

    /**
     * This method tests if once a follow request is rejected
     * The request is removed and a follower is not added
     */
    @Test
    public void rejectRequestTest(){
        //initialize test variables
        FollowRequestInbox mockInbox = mockFollowInbox();
        String sender_f = "testUser1";
        String target_f = "testUser2";
        boolean followerExists = false;
        FollowRequest acceptRequest = new FollowRequest(sender_f, target_f);
        int mockSize = mockInbox.getRequests().size();
        mockInbox.addRequest(acceptRequest);

        mockInbox.denyRequest(acceptRequest);

        //Our request should be removed
        assertEquals(mockSize, mockInbox.getRequests().size());

        for(String follower : mockInbox.getOwner().getFollowers()){
            if(follower.equals(sender_f)){
                followerExists = true;
            }
        }
        assertFalse(followerExists);
    }

}
