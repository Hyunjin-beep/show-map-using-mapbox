package com.example.assignment6_hc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Camera;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final String SOURCE_ID = "SOURCE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);

        Toolbar myToolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_myLocation:
                Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();

                if (lastKnownLocation != null){
                    CameraPosition position = new CameraPosition.Builder().target(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())).zoom(16).bearing(0).tilt(0).build();
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 5000);
                }
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
                CameraPosition newYork = new CameraPosition.Builder().target(new LatLng(40.7128, -74.0060)).zoom(16).bearing(0).tilt(0).build();
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(newYork), 5000);
                break;

            case R.id.menu_fav_place_two:
                CameraPosition seoul = new CameraPosition.Builder().target(new LatLng(37.5665, 126.9780)).zoom(16).bearing(0).tilt(0).build();
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(seoul), 3000);
                break;

            case R.id.menu_fav_place_one:
                CameraPosition belgrade = new CameraPosition.Builder().target(new LatLng(44.8125, 20.4612)).zoom(16).bearing(0).tilt(0).build();
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(belgrade), 3000);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void favLocations(){
        List<FaveLocations> list = new ArrayList<FaveLocations>();
        FaveLocations newYork = new FaveLocations(new LatLng(40.7128, -74.0060), "New York");
        FaveLocations seoul = new FaveLocations(new LatLng(37.5665, 126.9780), "New York");
        FaveLocations belgrade = new FaveLocations(new LatLng(44.8125, 20.4612), "New York");


        list.add(newYork);
        list.add(seoul);
        list.add(belgrade);

        for(int i = 0; i < list.size(); i++){
            mapboxMap.addMarker(new MarkerOptions().position(list.get(i).getLocation()).title(list.get(i).getTitle()));
        }


    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.7128, -74.0060))
                .title("Eiffel Tower"));
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                favLocations();
            }

        } );
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
        Toast.makeText(this, "string.user_location_permission_explanation,", Toast.LENGTH_SHORT).show();
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
            finish();
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