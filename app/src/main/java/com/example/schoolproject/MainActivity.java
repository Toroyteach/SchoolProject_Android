package com.example.schoolproject;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproject.Auth.LoginActivity;
import com.example.schoolproject.Auth.ProfileActivity;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.Auth.User;
import com.example.schoolproject.utils.LocationData;
import com.example.schoolproject.utils.RequestHandler;
import com.example.schoolproject.utils.URLs;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword, editTextPhone;
    RadioGroup radioGroupGender;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private ProgressBar progressBar;

    private String deviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        deviceToken = task.getResult();

                    }
                });

        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkNetworkStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phoneNumber = editTextPhone.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        } else if (password.length() < 8 || !containsSymbol(password)) {
            editTextPassword.setError("Password must be at least 8 characters long and contain at least one symbol");
            editTextPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11 || phoneNumber.length() > 12) {
            editTextPhone.setError("Enter a valid phone number");
            editTextPhone.requestFocus();
            return;
        }

        if (deviceToken == null) {
            System.out.println("The string is null");
            Toast.makeText(getApplicationContext(),"Error Getting Firebase Token Please Restart the Application" , Toast.LENGTH_LONG).show();

        }

        if (isNetworkConnected()) {

            getLocation(new LocationCallback() {
                @Override
                public void onLocationReceived(LocationData locationData) {
                    // Use the location data here
                    double latitude = locationData.getLatitude();
                    double longitude = locationData.getLongitude();
                    String locationName = "NotSet";

                    // Submit the details for user registration
                    submitSignUpDetails(username, email, password, phoneNumber, latitude, longitude, locationName, deviceToken);
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "No network connection.", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean containsSymbol(String password) {
        // Define the symbols that should be considered as valid symbols
        String symbols = "!@#$%^&*()_-+=<>?";

        // Iterate over each character in the password and check if it is a symbol
        for (char c : password.toCharArray()) {
            if (symbols.contains(String.valueOf(c))) {
                return true;
            }
        }

        return false;
    }

    private void checkNetworkStatus() {
        if (isNetworkConnected()) {
//            Toast.makeText(getApplicationContext(), "Network is connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No network connection. Internet Connection is Required", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // For devices running Android 10 (API level 29) or above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            // For devices running below Android 10
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
        }
    }

    private void getLocation(LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                String locationName = getLocationName(latitude, longitude);

                LocationData locationData = new LocationData(latitude, longitude, locationName);
                callback.onLocationReceived(locationData);
            } else {
                Toast.makeText(this, "Unable to retrieve location. Give Maps in On your Device Relavant Permissions.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getLocationName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String locationName = "";

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                locationName = address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationName;
    }

    public interface LocationCallback {
        void onLocationReceived(LocationData locationData);
    }

    private void submitSignUpDetails(String username, String email, String password, String phoneNumber, Double latitude, Double longitude, String locationName, String deviceTn){
        // url to post our data
        progressBar.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        //token

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_REGISTER, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                progressBar.setVisibility(View.GONE);

                try {

                    JSONObject obj = new JSONObject(response);
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        System.out.println(userJson.getInt("id")+userJson.getString("username"));

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("phone"),
                                userJson.getString("device_token"),
                                userJson.getString("status")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                    } else {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("username", username);
                params.put("password", password);
                params.put("longitude", String.valueOf(longitude));
                params.put("latitude", String.valueOf(latitude));
                params.put("email", email);
                params.put("phone", phoneNumber);
                params.put("device_token", deviceTn);
                params.put("location_name", locationName);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

}