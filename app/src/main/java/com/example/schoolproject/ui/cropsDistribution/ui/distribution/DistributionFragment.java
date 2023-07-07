package com.example.schoolproject.ui.cropsDistribution.ui.distribution;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.example.schoolproject.databinding.FragmentDistributionBinding;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.ui.alerts.utils.AlertOptions;
import com.example.schoolproject.ui.cropsDistribution.ui.crops.model.CropModel;
import com.example.schoolproject.ui.cropsDistribution.ui.crops.model.CropsRecyclerViewAdapter;
import com.example.schoolproject.ui.cropsDistribution.ui.distribution.model.DistributionModel;
import com.example.schoolproject.ui.cropsDistribution.ui.distribution.model.DistributionRecyclerViewAdapter;
import com.example.schoolproject.ui.cropsDistribution.ui.season.model.SeasonModel;
import com.example.schoolproject.ui.notification.utils.UserNotificationRecyclerViewAdapter;
import com.example.schoolproject.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DistributionFragment extends Fragment {

    private FragmentDistributionBinding binding;

    private DistributionRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<DistributionModel> notificationList;
    private ProgressBar progressBar;

    private EditText sizeEDT;
    private Spinner cropId;
    private Spinner seasonId;
    private Button submitDistBtn;

    private ArrayList<CropModel> cropsSpinnerList;
    private ArrayList<String> cropsNames = new ArrayList<String>();
    private ArrayList<SeasonModel> seasonSpinnerList;
    private ArrayList<String> seasonNames = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding = FragmentDistributionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_distribution);

        recyclerView = root.findViewById(R.id.recyclerViewDistributions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationList = new ArrayList<>();
        adapter = new DistributionRecyclerViewAdapter(notificationList, this::onDeleteClick, this::onMarkAsReadClick);
        recyclerView.setAdapter(adapter);
        fetchDistributionData();

        cropId = (Spinner) root.findViewById(R.id.spinnerCropIdD);
        seasonId = (Spinner) root.findViewById(R.id.spinnerSeasonIdD);
        sizeEDT = (EditText) root.findViewById(R.id.editTextdistributionSizeD);
        sizeEDT.setText("0");
        submitDistBtn = (Button) root.findViewById(R.id.buttonSubmitDistribution);

        submitDistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDistribution();
            }
        });

        getSpinnerCropsData();
        getSpinnerSeasonData();

        return root;
    }

    private void getSpinnerCropsData() {
        progressBar.setVisibility(View.VISIBLE);

        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final int userId = user.getId();
        String url = URLs.URL_GET_CROP + "?id="+ userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                cropsSpinnerList = new ArrayList<>();
                                JSONArray dataArray  = obj.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {


                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    int id = Integer.parseInt(dataobj.getString("id"));
                                    String cropname =  dataobj.getString("crop_name");
                                    String plantingdate =  dataobj.getString("planting_date");
                                    String harvestingdate =  dataobj.getString("harvesting_date");

                                    CropModel contact = new CropModel(id, cropname, plantingdate, harvestingdate);

                                    cropsSpinnerList.add(contact);

                                }

                                cropsNames.add("Choose Crop Type");

                                for (int i = 0; i < cropsSpinnerList.size(); i++){
                                    cropsNames.add(cropsSpinnerList.get(i).getCropname().toString());
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cropsNames);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                cropId.setAdapter(spinnerArrayAdapter);
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
                            Toast.makeText(getContext(), "You Do not have Any Crops", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        requestQueue.add(stringRequest);
    }

    private void getSpinnerSeasonData() {
        progressBar.setVisibility(View.VISIBLE);

        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final int userId = user.getId();
        String url = URLs.URL_GET_SEASON + "?id="+ userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                seasonSpinnerList = new ArrayList<>();
                                JSONArray dataArray  = obj.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {


                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    int id = Integer.parseInt(dataobj.getString("id"));
                                    String season_name =  dataobj.getString("season_name");
                                    String start_date =  dataobj.getString("start_date");
                                    String end_date =  dataobj.getString("end_date");

                                    SeasonModel contact = new SeasonModel(id, season_name, start_date, end_date);

                                    seasonSpinnerList.add(contact);

                                }

                                seasonNames.add("Choose Season Type");

                                for (int i = 0; i < seasonSpinnerList.size(); i++){
                                    seasonNames.add(seasonSpinnerList.get(i).getSeasonname().toString());
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, seasonNames);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                seasonId.setAdapter(spinnerArrayAdapter);
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
                            Toast.makeText(getContext(), "You Do not have Any Season Options", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        requestQueue.add(stringRequest);
    }

    private void addDistribution() {
        final String cropSelected = (String) cropId.getSelectedItem();
        final String seasonSelected = (String) seasonId.getSelectedItem();
        final String size = sizeEDT.getText().toString().trim();

        if (cropSelected.equals("Choose Crop Type")) {
            cropId.requestFocus();
            Toast.makeText(getActivity(), "Must Choose Crop", Toast.LENGTH_SHORT).show();
            return;
        }

        if (seasonSelected.equals("Choose Season Type")) {
            seasonId.requestFocus();
            Toast.makeText(getActivity(), "Must Choose Season", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(size) || Integer.parseInt(size) <= 0) {
            sizeEDT.requestFocus();
            Toast.makeText(getActivity(), "Must Enter Size of Land", Toast.LENGTH_SHORT).show();
            return;
        }

        postDistributionData(cropSelected, seasonSelected, size);
    }

    private void postDistributionData(String cropid, String seasonid, String size) {
        // url to post our data
        progressBar.setVisibility(View.VISIBLE);

        cropId.setSelection(0);
        seasonId.setSelection(0);
        sizeEDT.setText("0");

        //get userId
        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final int userId = user.getId();

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CREATE_DISTRIBUTION, new com.android.volley.Response.Listener<String>() {
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
                    fetchDistributionData();

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
                Toast.makeText(getActivity(), "Failed to create Distribution Data ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("crop_id", cropid);
                params.put("season_id", seasonid);
                params.put("quantity", size);
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

    private void fetchDistributionData() {

        progressBar.setVisibility(View.GONE);
        // Fetch JSON data from URL
        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final int userId = user.getId();

        String url = URLs.URL_GET_DISTRIBUTION + "?id="+ userId;

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
                                    String crop_name =  dataobj.getString("crop_name");
                                    String season_name =  dataobj.getString("season_name");
                                    String size =  dataobj.getString("size");

                                    DistributionModel contact = new DistributionModel(id, crop_name, season_name, size);
                                    notificationList.add(contact);
                                }

                            } else {

                                Toast.makeText(getContext(), "You Do not have Any Distribution Record", Toast.LENGTH_LONG).show();

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
        DistributionModel deletedAlert = notificationList.get(alertId);
        String deleteUrl = URLs.URL_DISTRIBUTION_DELETE + "?id=" + deletedAlert.getId();

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