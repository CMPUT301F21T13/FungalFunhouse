package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.example.habittracker.databinding.ActivityEventMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityEventMapsBinding binding;
    private Button enterMapsButton;

    private LatLng userPosition = new LatLng( 53.5461, -113.4938);
    private Marker userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        enterMapsButton = findViewById(R.id.maps_enter_button);

        if(getIntent().getFlags() == Intent.FLAG_ACTIVITY_NO_USER_ACTION){
            enterMapsButton.setVisibility(View.GONE);

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        enterMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("latitude", userLocation.getPosition().latitude);
                intent.putExtra("longitude", userLocation.getPosition().longitude);
                setResult(56, intent);
                finish();
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //settings
        UiSettings settings = mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);

        // Add a marker in Edmonton and move the camera
        userLocation = mMap.addMarker(new MarkerOptions().position(userPosition).title("Edmonton").draggable(true));
        userLocation.setTag(0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userPosition));
    }
}