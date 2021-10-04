package com.example.assignment6_hc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);

        Toolbar myToolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::onMapReady);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.menu_myLocation:
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }

                } );

                break;

            case R.id.menu_theme_dark:
                mapboxMap.setStyle(Style.DARK);
                break;

            case R.id.menu_theme_light:
                mapboxMap.setStyle(Style.LIGHT);
                break;

            case R.id.menu_theme_street:
                mapboxMap.setStyle(Style.MAPBOX_STREETS);
                break;

            case R.id.menu_fav_place_three:
                CameraPosition newYork = new CameraPosition.Builder().target(new LatLng(40.7484, -73.9857)).zoom(16).bearing(0).tilt(0).build();
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(newYork), 5000);

                break;

            case R.id.menu_fav_place_two:
                CameraPosition seoul = new CameraPosition.Builder().target(new LatLng(37.5512, 126.9882)).zoom(16).bearing(0).tilt(0).build();
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(seoul), 3000);
                break;

            case R.id.menu_fav_place_one:
                CameraPosition madrid = new CameraPosition.Builder().target(new LatLng(41.4036, 2.1744)).zoom(16).bearing(0).tilt(0).build();
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(madrid), 3000);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;

        favLocations();
        CameraPosition position = new CameraPosition.Builder().target(new LatLng(49.900243, -97.141401)).zoom(16).bearing(0).tilt(0).build();
        mapboxMap.setCameraPosition(position);
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
            }

        } );
    }


    private void favLocations(){
        List<FaveLocations> list = new ArrayList<FaveLocations>();
        FaveLocations newYork = new FaveLocations(new LatLng(40.7484, -73.9857), "Empire State Building", R.drawable.newyork);
        FaveLocations seoul = new FaveLocations(new LatLng(37.5512, 126.9882), "N Seoul Tower", R.drawable.seoul);
        FaveLocations madrid = new FaveLocations(new LatLng(41.4036, 2.1744), "La Sagrada Familia", R.drawable.mountain);

        list.add(newYork);
        list.add(seoul);
        list.add(madrid);

        for(int i = 0; i < list.size(); i++){
            IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
            com.mapbox.mapboxsdk.annotations.Icon icon;
            icon = iconFactory.fromResource(list.get(i).getImage());

            mapboxMap.addMarker(new MarkerOptions().position(list.get(i).getLocation()).setTitle(list.get(i).getTitle()).icon(icon));
        }


    }



    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(Style loadedMapStyle) {
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);

            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the options menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onExplanationNeeded(List<String> list) {
        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            finish();
        }

    }



    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}