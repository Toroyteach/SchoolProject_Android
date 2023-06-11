package com.example.schoolproject.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproject.Auth.LoginActivity;
import com.example.schoolproject.Auth.User;
import com.example.schoolproject.R;
import com.example.schoolproject.databinding.FragmentGalleryBinding;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.utils.LocationData;
import com.example.schoolproject.utils.URLs;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    TextView usernameTV, phoneTV, emailTV, createdNotification, receivedNotification;
    SwitchMaterial status;

    ProgressBar progressBar;

    private RelativeLayout animationRelative;
    AnimationDrawable animationDrawable;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = (ProgressBar) root.findViewById(R.id.progressBarupdateSwitch);

        animationRelative = root.findViewById(R.id.fragmentLayoutProfileGradientAnimation);
        animationDrawable = (AnimationDrawable) animationRelative.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3500);
        animationDrawable.start();


        usernameTV = (TextView) root.findViewById(R.id.username_edt);
        phoneTV = (TextView) root.findViewById(R.id.phone_edt);
        emailTV = (TextView) root.findViewById(R.id.maill_edt);
        status = (SwitchMaterial) root.findViewById(R.id.switcch_edt);

        createdNotification = (TextView) root.findViewById(R.id.created_notice_txt);
        receivedNotification = (TextView) root.findViewById(R.id.received_count_txt);
        getNotificationsCount();


        getUserDataFromPref();

        Button updateLocatinobtn = (Button) root.findViewById(R.id.location_update_btn);
        updateLocatinobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserlocationData();
            }
        });

        Button updatebtn = (Button) root.findViewById(R.id.update_btn);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
            }
        });

        Button logoutbtn = (Button) root.findViewById(R.id.logout_btn_profile);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                SharedPrefManager.getInstance(getActivity()).logout();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

//        Button deletebtn = (Button) root.findViewById(R.id.delete_btn);
//        deletebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //if user pressed on button register
//                //here we will register the user to server
//                deleteUserAccountAction();
//            }
//        });

        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                showConfirmationDialog(isChecked);
            }
        });

        return root;
    }

    private void updateUserlocationData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Location");
        builder.setMessage("Are you sure you want to Change your Location Details? This will also Delete your Alerts and Notifications.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prepareUpdateUserLocation();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Reset the switch to the previous state
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void prepareUpdateUserLocation(){

        if (isNetworkConnected()) {
            //Get user location data

            getLocation(new GalleryFragment.LocationCallback() {
                @Override
                public void onLocationReceived(LocationData locationData) {
                    // Use the location data here
                    double latitude = locationData.getLatitude();
                    double longitude = locationData.getLongitude();

                    // Submit the details for user registration
                    postNewUserLocationData(latitude, longitude);
                }
            });

        } else {
            Toast.makeText(getContext(), "No network connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void postNewUserLocationData(double lat, double lang) {

        // Create JSON object with the switch option
        progressBar.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        User user =  SharedPrefManager.getInstance(getContext()).getUser();
        int id = user.getId();

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_USER_LOCATION, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                progressBar.setVisibility(View.GONE);

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    String message = respObj.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("longitude", String.valueOf(lang));
                params.put("latitude", String.valueOf(lat));
                params.put("id", String.valueOf(id));

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void getUserDataFromPref() {
        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        usernameTV.setText(user.getUsername());
        phoneTV.setText(user.getPhone());
        emailTV.setText(user.getEmail());

        int value = Integer.parseInt(user.getStatus());

        if(value == 1){
            status.setChecked(true);
        } else {
            status.setChecked(false);
        }
    }

    private void deleteUserAccountAction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to Delete Your Account Plus Data?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform the action here

                User user =  SharedPrefManager.getInstance(getActivity()).getUser();

                deleteAccount(user.getId());
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancel the action or perform any other desired action
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void deleteAccount(int id) {
        //runn the method request to delete user account and there data
        // Create a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Specify the DELETE URL with the ID parameter
        String deleteUrl = URLs.URL_DELETE_USER_DATA + "?id=" + id;

        // Create a StringRequest with the DELETE method
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            // on below line we are parsing the response
                            // to json object to extract data from it.
                            JSONObject respObj = new JSONObject(response);
                            String message = respObj.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                            //Logout User
                            SharedPrefManager.getInstance(getActivity()).logout();
                            startActivity(new Intent(getActivity(), LoginActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(getContext(), "Error deleting data", Toast.LENGTH_SHORT).show();
                        // Additional error handling if needed
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            Toast.makeText(getContext(), "Error deleting data", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Add the StringRequest to the RequestQueue
        requestQueue.add(deleteRequest);
    }

    private void getNotificationsCount(){

        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        int id = user.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET_USER_COUNT_DATA + "?id="+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                String created = obj.getString("createdNotice")+" Created";
                                String received = obj.getString("receivedNotice")+" Received";
                                createdNotification.setText(created);
                                receivedNotification.setText(received);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        requestQueue.add(stringRequest);
    }

    private void showConfirmationDialog(final boolean isChecked) {


        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        int value = Integer.parseInt(user.getStatus());

        if(isChecked){

            if(value == 1){

                return;

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Update Notification Statu");
                builder.setMessage("Are you sure you want to Disable Receiving of Notifications?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        makeVolleyRequest(isChecked);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Reset the switch to the previous state
                        //status.setChecked(!isChecked);
                        getUserDataFromPref();
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

        } else {
            if(value == 1){

                makeVolleyRequest(isChecked);

            } else {
                return;
            }
        }
    }

    private void makeVolleyRequest(boolean isChecked) {

        String statusCheck = "0";

        if(isChecked){
            statusCheck = "1";
        }
        // Create JSON object with the switch option
        progressBar.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        User user =  SharedPrefManager.getInstance(getContext()).getUser();
        int id = user.getId();

        String finalStatusCheck = statusCheck;
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_USER_STATUS, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                progressBar.setVisibility(View.GONE);

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    String message = respObj.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                    JSONObject userObject = respObj.getJSONObject("user");
                    String status = userObject.getString("status");
                    //Call the method to refresh the list of custom alerts

                    SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
                    sharedPrefManager.updateUserStatus(status);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("status", finalStatusCheck);
                params.put("id", String.valueOf(id));

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);

    }

    private void getLocation(GalleryFragment.LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                String locationName = "NotSet";

                LocationData locationData = new LocationData(latitude, longitude, locationName);
                callback.onLocationReceived(locationData);
            } else {
                Toast.makeText(getContext(), "Unable to retrieve location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

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


    @Override
    public void onResume() {
        super.onResume();
        getUserDataFromPref();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    interface LocationCallback {
        void onLocationReceived(LocationData locationData);
    }
}