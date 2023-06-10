package com.example.schoolproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.schoolproject.Auth.LoginActivity;
import com.example.schoolproject.services.SharedPrefManager;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private static final String PERMISSION_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSION_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check for location permission on app startup

                if (checkLocationPermissions()) {

                    if (SharedPrefManager.getInstance(SplashScreenActivity.this).isLoggedIn()) {

                        finish();
                        startActivity(new Intent(SplashScreenActivity.this, MenuActivity.class));

                    } else {

                        finish();
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));

                    }
                }

            }
        }, 3000);

    }

    private boolean checkLocationPermissions() {
        // Check if the app has permission to access fine location and coarse location
        if (ContextCompat.checkSelfPermission(this, PERMISSION_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, PERMISSION_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            // Permissions are granted
            return true;
        } else {
            // Permissions are not granted
            // Request permissions from the user
            requestLocationPermissions();
            return false;
        }
    }

    private void requestLocationPermissions() {
        // Request fine location and coarse location permissions
        ActivityCompat.requestPermissions(this,
                new String[]{PERMISSION_FINE_LOCATION, PERMISSION_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Check if the permission request is for location permissions
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions are granted, continue with your app logic
                if (SharedPrefManager.getInstance(SplashScreenActivity.this).isLoggedIn()) {
                    finish();
                    startActivity(new Intent(SplashScreenActivity.this, MenuActivity.class));
                } else {

                    finish();
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));

                }

            } else {
                // Permissions are not granted, display a message and finish the application
                Toast.makeText(this, "Location permission is required to use this app.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}