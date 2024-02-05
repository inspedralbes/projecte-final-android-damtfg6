package com.example.recuerdate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            // Log o mensaje de depuración indicando que los permisos no están otorgados
            Log.d("Ubicación", "Permisos de ubicación no otorgados");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // Log o mensaje de depuración indicando que los permisos están otorgados
            Log.d("Ubicación", "Permisos de ubicación otorgados");
            // Resto del código para solicitar actualizaciones de ubicación
            fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // Intervalo en milisegundos para recibir actualizaciones de ubicación
        locationRequest.setFastestInterval(5000); // Intervalo más rápido en milisegundos
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
}
