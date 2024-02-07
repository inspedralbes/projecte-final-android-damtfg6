package com.example.recuerdate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocalitzacioFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gmap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_localitzacio, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Ubicación", "Permisos de ubicación no otorgados");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d("Ubicación", "Permisos de ubicación otorgados");
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);
        }

        // Implementación de los métodos de zoom y ubicación
        AppCompatImageButton btnZoomIn = getView().findViewById(R.id.btnZoomIn);
        AppCompatImageButton btnZoomOut = getView().findViewById(R.id.btnZoomOut);
        AppCompatImageButton btnCenterOnMyLocation = getView().findViewById(R.id.btnCenterOnMyLocation);

        btnZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomIn(v);
            }
        });

        btnZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOut(v);
            }
        });

        btnCenterOnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerOnMyLocation(v);
            }
        });
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null && locationResult.getLastLocation() != null) {
                Location location = locationResult.getLastLocation();
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                gmap.addMarker(new MarkerOptions().position(currentLocation).title("Estoy aquí"));
                gmap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void zoomIn(View view) {
        if (gmap != null) {
            gmap.animateCamera(CameraUpdateFactory.zoomIn());
        }
    }

    public void zoomOut(View view) {
        if (gmap != null) {
            gmap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void centerOnMyLocation(View view) {
        if (gmap != null && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                            }
                        }
                    });
        }
    }
}
