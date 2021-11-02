package com.example.habittracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RequestAdapter extends ArrayAdapter<FollowRequest> {

        private ArrayList<FollowRequest> requests;
        private Context context;

    public RequestAdapter(Context context, ArrayList<FollowRequest> requests){
            super(context, 0, requests);
            this.context = context;
            this.requests = requests;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.profile_list_content_home, parent,false);
            }

            FollowRequest request = requests.get(position);

            TextView request_content = view.findViewById(R.id.username_text_grid);

            String request_text = "Follow request from: " + request.getSender().getUsername();
            request_content.setText(request_text);


            return view;
    }
}
