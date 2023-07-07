package com.example.schoolproject.ui.cropsDistribution.ui.season;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproject.Auth.User;
import com.example.schoolproject.R;
import com.example.schoolproject.databinding.FragmentSeasonBinding;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.ui.cropsDistribution.ui.crops.model.CropModel;
import com.example.schoolproject.ui.cropsDistribution.ui.crops.model.CropsRecyclerViewAdapter;
import com.example.schoolproject.ui.cropsDistribution.ui.season.model.SeasonModel;
import com.example.schoolproject.ui.cropsDistribution.ui.season.model.SeasonRecyclerViewAdpater;
import com.example.schoolproject.ui.notification.utils.UserNotificationRecyclerViewAdapter;
import com.example.schoolproject.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeasonFragment extends Fragment {

    private FragmentSeasonBinding binding;

    private SeasonRecyclerViewAdpater adapter;
    private RecyclerView recyclerView;
    private List<SeasonModel> notificationList;
    private ProgressBar progressBar;

    private Button selectStartDateBtn;
    private Button selectEndDataBtn;
    private Button submitSeasonBtn;

    private TextView seasonStartDatetxt;
    private TextView seasonEndDatetxt;
    private EditText seasonNameEdt;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        binding = FragmentSeasonBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_season);

        recyclerView = root.findViewById(R.id.recyclerViewSeasons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationList = new ArrayList<>();
        adapter = new SeasonRecyclerViewAdpater(notificationList, this::onDeleteClick, this::onMarkAsReadClick);
        recyclerView.setAdapter(adapter);
        fetchSeasonData();

        seasonStartDatetxt = (TextView) root.findViewById(R.id.txtview_seasoneStartdate);
        seasonStartDatetxt.setText("");
        seasonEndDatetxt = (TextView) root.findViewById(R.id.txtview_seasonenddate);
        seasonEndDatetxt.setText("");
        seasonNameEdt = (EditText) root.findViewById(R.id.editTextSeasonName);

        selectStartDateBtn = (Button) root.findViewById(R.id.buttonSeasonStartDate);
        selectEndDataBtn = (Button) root.findViewById(R.id.buttonSeasonEndDate);
        submitSeasonBtn = (Button) root.findViewById(R.id.buttonSubmitSeason);

        submitSeasonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSeason();
            }
        });

        selectStartDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDateDialog();
            }
        });

        selectEndDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEndDateDialog();
            }
        });

        return root;
    }

    private void openEndDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String date = String.valueOf(i)+"-"+String.valueOf(i1)+"-"+String.valueOf(i2);
                seasonEndDatetxt.setText(date);
            }
        }, 2023, 1, 1);

        dialog.show();
    }

    private void openStartDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String date = String.valueOf(i)+"-"+String.valueOf(i1)+"-"+String.valueOf(i2);
                seasonStartDatetxt.setText(date);
            }
        }, 2023, 1, 1);

        dialog.show();
    }

    private void addSeason() {
        final String plantDate = seasonStartDatetxt.getText().toString().trim();
        final String harvestDate = seasonEndDatetxt.getText().toString().trim();
        final String cropName = seasonNameEdt.getText().toString().trim();

        if (TextUtils.isEmpty(plantDate)) {
            seasonStartDatetxt.requestFocus();
            Toast.makeText(getActivity(), "You need to choose a Season Start Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(harvestDate)) {
            seasonEndDatetxt.requestFocus();
            Toast.makeText(getActivity(), "You need to choose a Season End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(cropName)) {
            Toast.makeText(getActivity(), "Please enter Season Name", Toast.LENGTH_SHORT).show();
            seasonNameEdt.requestFocus();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date parsedDateA = dateFormat.parse(plantDate);
            Date parsedDateB = dateFormat.parse(harvestDate);

            if (parsedDateB != null && parsedDateB.after(parsedDateA)) {

                postSeasonData(cropName, plantDate, harvestDate);
            } else {

                Toast.makeText(getActivity(), "The Date of End Season cannot be before Start Date", Toast.LENGTH_LONG).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void postSeasonData(String seasonName, String startDate, String endDate) {
        // url to post our data
        progressBar.setVisibility(View.VISIBLE);

        seasonStartDatetxt.setText("");
        seasonEndDatetxt.setText("");
        seasonNameEdt.setText("");

        //get userId
        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final int userId = user.getId();

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CREATE_SEASON, new com.android.volley.Response.Listener<String>() {
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
                    fetchSeasonData();

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
                Toast.makeText(getActivity(), "Failed to create Crop Data ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("season_name", seasonName);
                params.put("start_date", startDate);
                params.put("end_date", endDate);
                params.put("user_id", String.valueOf(userId));

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void fetchSeasonData() {

        progressBar.setVisibility(View.VISIBLE);
        // Fetch JSON data from URL
        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final int userId = user.getId();

        String url = URLs.URL_GET_SEASON + "?id="+ userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse JSON data and populate the contactList

                        progressBar.setVisibility(View.GONE);
                        try {

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                JSONArray dataArray  = obj.getJSONArray("data");

                                notificationList.clear();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    int id = Integer.parseInt(dataobj.getString("id"));
                                    String season_name =  dataobj.getString("season_name");
                                    String start_date =  dataobj.getString("start_date");
                                    String end_date =  dataobj.getString("end_date");

                                    SeasonModel contact = new SeasonModel(id, season_name, start_date, end_date);
                                    notificationList.add(contact);
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                        }

                        // Notify the adapter that the data set has changed
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressBar.setVisibility(View.GONE);
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            Toast.makeText(getContext(), "You Do not have Any Notifications", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println(error.getMessage());
                        }
                    }
                });

        // Add the request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    private void onMarkAsReadClick(int i) {
    }

    private void onDeleteClick(int i) {
        showWarningDialog(i);
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

    private void deleteUserCustomAlert(int alertId) {
        // Create a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Specify the DELETE URL with the ID parameter
        SeasonModel deletedAlert = notificationList.get(alertId);
        String deleteUrl = URLs.URL_SEASON_DELETE + "?id=" + deletedAlert.getId();

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}