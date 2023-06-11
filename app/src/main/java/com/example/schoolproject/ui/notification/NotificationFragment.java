package com.example.schoolproject.ui.notification;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproject.Auth.User;
import com.example.schoolproject.R;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.ui.notification.utils.UserNotificationModel;
import com.example.schoolproject.ui.notification.utils.UserNotificationRecyclerViewAdapter;
import com.example.schoolproject.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationFragment extends Fragment {

    private NotificationFragment binding;

    //private ArrayList<UserNotificationModel> notificationList;
    private UserNotificationRecyclerViewAdapter adapter;

    private RelativeLayout animationRelative;
    AnimationDrawable animationDrawable;

    private RecyclerView recyclerView;
    private List<UserNotificationModel> notificationList;

    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the contactList and adapter

        recyclerView = view.findViewById(R.id.listViewNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationList = new ArrayList<>();
        adapter = new UserNotificationRecyclerViewAdapter(notificationList, this::onDeleteClick, this::onMarkAsReadClick);
        recyclerView.setAdapter(adapter);
        fetchNotificationsData();

        //Animation
        animationRelative = view.findViewById(R.id.fragmentLayoutNotificationGradientAnimation);
        animationDrawable = (AnimationDrawable) animationRelative.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3500);
        animationDrawable.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchNotificationsData(){
        // Fetch JSON data from URL
        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final int userId = user.getId();

        String url = URLs.URL_GET_NOTIFICATIONS + "?id="+ userId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parse JSON data and populate the contactList

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String alertName = jsonObject.getString("option_name");
                                String usermessage = jsonObject.getString("user_message");
                                String upperlimit = jsonObject.getString("max_value");
                                String lowerlimit = jsonObject.getString("min_value");
                                String notificationtime = jsonObject.getString("date_sent");
                                String notificationid = jsonObject.getString("notification_id");
                                String readAt = jsonObject.getString("read_at");

                                UserNotificationModel contact = new UserNotificationModel(alertName, usermessage, upperlimit, lowerlimit, notificationtime, notificationid, readAt);
                                notificationList.add(contact);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Notify the adapter that the data set has changed
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            Toast.makeText(getContext(), "You Do not have Any Notifications", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest);
    }

    public void onDeleteClick(int position) {
        showWarningDialog(position);
    }

    public void onMarkAsReadClick(int position) {
        markNotificationAsRead(position);
    }

    private void showWarningDialog(int alertId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to Delete this?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform the action here
                deleteUserNotification(alertId);
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

    private void deleteUserNotification(int alertId) {
        // Create a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Specify the DELETE URL with the ID parameter
        UserNotificationModel deletedNotification = notificationList.get(alertId);
        String deleteUrl = URLs.URL_DELETE_NOTIFICATION + "?id=" + deletedNotification.getNotificationId();

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

                            notificationList.remove(alertId);
                            // Notify the adapter about the change
                            adapter.notifyDataSetChanged();

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
                    }
                });

        // Add the StringRequest to the RequestQueue
        requestQueue.add(deleteRequest);
    }

    private void markNotificationAsRead(int alertId) {
        // Create a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Specify the DELETE URL with the ID parameter
        UserNotificationModel deletedNotification = notificationList.get(alertId);
        String deleteUrl = URLs.URL_MARK_NOTIFICATION_RED;

        // Create a StringRequest with the DELETE method
        StringRequest deleteRequest = new StringRequest(Request.Method.POST, deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            // on below line we are parsing the response
                            // to json object to extract data from it.
                            JSONObject respObj = new JSONObject(response);
                            String message = respObj.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                            notificationList.clear();
                            fetchNotificationsData();
                            adapter.notifyDataSetChanged();

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
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("id", String.valueOf(deletedNotification.getNotificationId()));

                // at last we are
                // returning our params.
                return params;
            }
        };

        // Add the StringRequest to the RequestQueue
        requestQueue.add(deleteRequest);
    }
}