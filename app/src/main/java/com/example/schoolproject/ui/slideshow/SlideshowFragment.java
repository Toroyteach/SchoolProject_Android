package com.example.schoolproject.ui.slideshow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproject.Auth.User;
import com.example.schoolproject.R;
import com.example.schoolproject.databinding.FragmentSlideshowBinding;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.ui.slideshow.utils.AlertOptions;
import com.example.schoolproject.ui.slideshow.utils.UserCustomAlertAdapter;
import com.example.schoolproject.ui.slideshow.utils.UserCustomAlertModel;
import com.example.schoolproject.ui.slideshow.utils.UserCustomAlertRecyclerViewAdapter;
import com.example.schoolproject.utils.RequestHandler;
import com.example.schoolproject.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    private ArrayList<AlertOptions> alertOptionsArrayList;
    private ArrayList<String> alertNames = new ArrayList<String>();

    private RelativeLayout animationRelative;
    AnimationDrawable animationDrawable;

    private RecyclerView recyclerView;
    private UserCustomAlertRecyclerViewAdapter adapter;
    private List<UserCustomAlertModel> alertList;

    ArrayList<UserCustomAlertModel> userCustomAlertModels;

    ListView listView;

    Button addCustomAlert;
    EditText alertNameEDT;
    EditText upperLimitEDT;
    EditText lowerLimitEDT;
    EditText userMessageEDT;
    Spinner alertTypeSpinner;

    ProgressBar progressBar;

    String TAG = "SampleLogTag";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Edit etxt
        upperLimitEDT = (EditText) root.findViewById(R.id.edit_text_upper_limit);
        lowerLimitEDT = (EditText) root.findViewById(R.id.edit_text_lower_limit);
        userMessageEDT = (EditText) root.findViewById(R.id.edit_text_user_message);
        upperLimitEDT.setText("");
        lowerLimitEDT.setText("");
        userMessageEDT.setText("");

//        userCustomAlertModels = new ArrayList<>();

        // get the Add button
        addCustomAlert = (Button) root.findViewById(R.id.buttonAddUpdateCustomAlert);

        //Spinner
        alertTypeSpinner = (Spinner) root.findViewById(R.id.spinnerAlertType);

        //Progressbar
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        //recyclerview
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        alertList = new ArrayList<>();
        adapter = new UserCustomAlertRecyclerViewAdapter(alertList, this::onDeleteClick);
        recyclerView.setAdapter(adapter);
        fetchDataFromEndpoint();

        //animation
        animationRelative = root.findViewById(R.id.fragmentLayoutCrudGradientAnimation);
        animationDrawable = (AnimationDrawable) animationRelative.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3500);
        animationDrawable.start();

        addCustomAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCustomAlert();
            }
        });

        getSpinnerData();
        //getCustomAlert();


        return root;
    }

    public void getSpinnerData(){

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_OPTIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                alertOptionsArrayList = new ArrayList<>();
                                JSONArray dataArray  = obj.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    AlertOptions alertOptionModel = new AlertOptions();
                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    alertOptionModel.setAlertName(dataobj.getString("option_name"));
                                    alertOptionModel.setAlertId(dataobj.getInt("option_id"));

                                    alertOptionsArrayList.add(alertOptionModel);

                                }

                                alertNames.add("Choose an Alert Type");

                                for (int i = 0; i < alertOptionsArrayList.size(); i++){
                                    alertNames.add(alertOptionsArrayList.get(i).getAlertName().toString());
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, alertNames);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                alertTypeSpinner.setAdapter(spinnerArrayAdapter);
                                progressBar.setVisibility(View.GONE);
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
                        progressBar.setVisibility(View.GONE);
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            Toast.makeText(getContext(), "You Do not have Any Notifications", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        requestQueue.add(stringRequest);
    }

    public void getCustomAlert(){

        userCustomAlertModels = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_USERS_ALERT + "?id=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                alertOptionsArrayList = new ArrayList<>();
                                JSONArray dataArray  = obj.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    userCustomAlertModels.add(new UserCustomAlertModel(dataobj.getInt("id"),dataobj.getString("option_name"), dataobj.getString("user_message"), dataobj.getString("max_value"),dataobj.getString("min_value")));
                                }

                                UserCustomAlertAdapter userCustomAdapter = new UserCustomAlertAdapter(getActivity(), userCustomAlertModels);
                                listView.setAdapter(userCustomAdapter);
                                progressBar.setVisibility(View.GONE);


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
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    public void addCustomAlert(){
        final String selectedItem = (String) alertTypeSpinner.getSelectedItem();
        final String upperLimit = upperLimitEDT.getText().toString().trim();
        final String lowerLimit = lowerLimitEDT.getText().toString().trim();
        final String userMessage = userMessageEDT.getText().toString().trim();

        //first we will do the validations

        if (selectedItem.equals("Choose an Alert Type")) {
            alertTypeSpinner.requestFocus();
            Toast.makeText(getActivity(), "Must Choose an Alert Type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(upperLimit)) {
            upperLimitEDT.requestFocus();
            Toast.makeText(getActivity(), "Must Choose an Upper limit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(lowerLimit)) {
            lowerLimitEDT.requestFocus();
            Toast.makeText(getActivity(), "Must Choose an Lower limit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userMessage)) {
            Toast.makeText(getActivity(), "Please enter the Message to Notify you", Toast.LENGTH_SHORT).show();
            userMessageEDT.requestFocus();
            return;
        }

        double lowerLimitInt = Double.parseDouble(lowerLimit);
        double upperLimitInt = Double.parseDouble(upperLimit);

        if (upperLimitInt < lowerLimitInt) {
            upperLimitEDT.requestFocus();
            Toast.makeText(getActivity(), "Upper Limit must be Greater than Lower Limit", Toast.LENGTH_SHORT).show();
            return;
        }

        postDataUsingVolley(selectedItem, upperLimit, lowerLimit, userMessage);

    }

    private void postDataUsingVolley(String alertName, String upperRange, String lowerRange, String userMessage) {
        // url to post our data
        progressBar.setVisibility(View.VISIBLE);

        alertTypeSpinner.setSelection(0);
        upperLimitEDT.setText("");
        lowerLimitEDT.setText("");
        userMessageEDT.setText("");

        //get userId
        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final int userId = user.getId();

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        //unique string
        String noticeId = getUniqueString();

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CREATE_ALERT, new com.android.volley.Response.Listener<String>() {
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

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    fetchDataFromEndpoint();

                    //Call the method to refresh the list of custom alerts

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("user_id", String.valueOf(userId));
                params.put("option_id", alertName);
                params.put("notification_id", noticeId);
                params.put("max_value", upperRange);
                params.put("min_value", lowerRange);
                params.put("user_message", userMessage);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }



    private void fetchDataFromEndpoint() {

        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final int userId = user.getId();
        String url = URLs.URL_USERS_ALERT + "?id=" +userId;

        alertList.clear();

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                alertOptionsArrayList = new ArrayList<>();
                                JSONArray dataArray  = obj.getJSONArray("data");

                                alertList.clear();

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    String alertName = dataobj.getString("option_name");
                                    String userMessage = dataobj.getString("user_message");
                                    int Id = dataobj.getInt("id");
                                    String upperLimit = dataobj.getString("max_value");
                                    String lowerLimit = dataobj.getString("min_value");

                                    UserCustomAlertModel alertModel = new UserCustomAlertModel(Id, alertName, userMessage, upperLimit, lowerLimit);
                                    alertList.add(alertModel);
                                }

                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);


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
                        progressBar.setVisibility(View.GONE);
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            Toast.makeText(getContext(), "You Do not have Any Alerts", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    public void onDeleteClick(int position) {
        showWarningDialog(position);
    }

    private void showWarningDialog(int alertId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to Delete this?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform the action here
                deleteUserCustomAlert(alertId);
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

    private void deleteUserCustomAlert(int alertId){

        // Create a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Specify the DELETE URL with the ID parameter
        UserCustomAlertModel deletedAlert = alertList.get(alertId);
        String deleteUrl = URLs.URL_DELETE_ALERT + "?id=" + deletedAlert.getId();

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

                            alertList.remove(alertId);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static String getUniqueString(){
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder uniqueString = new StringBuilder();
        Random random = new Random();
        while(uniqueString.length() < 12){
            int index = random.nextInt(charSet.length());
            uniqueString.append(charSet.charAt(index));
        }
        return uniqueString.toString();
    }

}