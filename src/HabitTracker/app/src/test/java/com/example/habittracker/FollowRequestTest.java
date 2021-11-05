package com.example.habittracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FollowRequestTest {

    private FollowRequest mockFollowRequest(){
        FollowRequest mockRequest = new FollowRequest("User1", "User2");
        return mockRequest;
    }

    @Test
    public void testGetSender(){
        FollowRequest mockRequest = mockFollowRequest();
        String sender = mockRequest.getSender();
        assertEquals("User1", sender);
    }

    @Test
    public void testGetTarget(){
        FollowRequest mockRequest = mockFollowRequest();
        String target = mockRequest.getTarget();
        assertEquals("User2", target);
    }
}
