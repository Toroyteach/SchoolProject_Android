package com.example.schoolproject.ui.home;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.schoolproject.Auth.User;
import com.example.schoolproject.R;
import com.example.schoolproject.databinding.FragmentHomeBinding;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.ui.alerts.utils.UserCustomAlertModel;
import com.example.schoolproject.ui.home.utils.DailyWeatherDataRecyclerViewAdapter;
import com.example.schoolproject.ui.home.utils.HourlyWeatherDataRecyclerViewAdapter;
import com.example.schoolproject.ui.home.utils.models.DailyWeatherDataModel;
import com.example.schoolproject.ui.home.utils.models.HourlyWeatherDataModel;
import com.example.schoolproject.ui.notification.utils.UserNotificationModel;
import com.example.schoolproject.ui.notification.utils.UserNotificationRecyclerViewAdapter;
import com.example.schoolproject.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RelativeLayout animationRelative;
    AnimationDrawable animationDrawable;

    private TextView locationtxt;
    private TextView temperaturetxt;
    private TextView weathertxt;

    private ImageView imageView;
    private int[] imageResources = {R.drawable.raining, R.drawable.sunny, R.drawable.cloudy};
    private int currentImageIndex = 0;

    private HourlyWeatherDataRecyclerViewAdapter adapterh;
    private RecyclerView hourlyRecyclerView;
    private List<HourlyWeatherDataModel> hourlyList;

    private DailyWeatherDataRecyclerViewAdapter adapterd;
    private RecyclerView dailyRecyclerView;
    private List<DailyWeatherDataModel> dailyList;

    ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //animation
        animationRelative = root.findViewById(R.id.homefragmentAnimation);
        animationDrawable = (AnimationDrawable) animationRelative.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3500);
        animationDrawable.start();

        locationtxt = (TextView) root.findViewById(R.id.home_location_txtview);
        temperaturetxt = (TextView) root.findViewById(R.id.home_temperature_txtview);
        weathertxt = (TextView) root.findViewById(R.id.home_weather_tctview);

        progressBar = (ProgressBar) root.findViewById(R.id.home_proggressbach);

        imageView = root.findViewById(R.id.imageView_Gif);

        hourlyRecyclerView = root.findViewById(R.id.hourlyrecyclerView);
        hourlyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        hourlyList = new ArrayList<>();
        adapterh = new HourlyWeatherDataRecyclerViewAdapter(getContext(), hourlyList);
        hourlyRecyclerView.setAdapter(adapterh);

        dailyRecyclerView = root.findViewById(R.id.dailyrececlyerView);
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        dailyList = new ArrayList<>();
        adapterd = new DailyWeatherDataRecyclerViewAdapter(dailyList);
        dailyRecyclerView.setAdapter(adapterd);

        getActiveWeatherData();


        return root;
    }

    private void setLiveImage(String imageName) {
        int imageResource;

        // Find the resource ID for the given image name
        switch (imageName) {
            case "rain":
                imageResource = R.drawable.riaing_giphy;
                break;
            case "sun":
                imageResource = R.drawable.sunny_giphy;
                break;
            case "cloud":
                imageResource = R.drawable.cloudy_giphy;
                break;
            default:
                // If the image name is not recognized, set a default image
                imageResource = R.drawable.cloudy;
        }

        // Update the current image index based on the selected image
        for (int i = 0; i < imageResources.length; i++) {
            if (imageResources[i] == imageResource) {
                currentImageIndex = i;
                break;
            }
        }

        // Load the selected image into the ImageView
        Glide.with(requireContext()).load(imageResource).into(imageView);
    }

    private void getActiveWeatherData(){

        progressBar.setVisibility(View.VISIBLE);

        hourlyList.clear();
        dailyList.clear();

        // Fetch JSON data from URL
        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final int userId = user.getId();

        String url = URLs.URL_GET_ACTIVE_WEATHER_DATA + "?id="+ userId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseString) {
                        // Parse JSON data and populate the contactList
                        try {

                            //JSONArray jsonArray = new JSONArray(responseString);
                            JSONObject jsonObject = responseString.getJSONObject(0);

                            // Read data object
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            //setUpMainUiData(location, temperature, weather);

                            // Read hourly array
                            JSONArray hourlyArray = dataObject.getJSONArray("hourly");
                            // Iterate over hourly array
                            for (int i = 0; i < hourlyArray.length(); i++) {
                                JSONObject hourlyObject = hourlyArray.getJSONObject(i);
                                // Process hourly object
                                int timeStamp = Integer.parseInt(hourlyObject.getString("time_stamp"));
                                double temperature = Double.parseDouble(hourlyObject.getString("temperature"));
                                String icon = hourlyObject.getString("icon");

                                HourlyWeatherDataModel hourlyData = new HourlyWeatherDataModel(timeStamp, temperature, icon);
                                hourlyList.add(hourlyData);
                            }
                            adapterh.notifyDataSetChanged();


                            // Read current object
                            JSONObject currentObject = dataObject.getJSONObject("current");
                            // Process current object
                            String location = currentObject.getString("location");
                            String temperature_c = currentObject.getString("temperature");
                            String weather_c = currentObject.getString("weather");
                            setUpMainUiData(location, temperature_c, weather_c);

                            // Read daily array
                            JSONArray dailyArray = dataObject.getJSONArray("daily");
                            // Iterate over daily array
                            for (int i = 0; i < dailyArray.length(); i++) {
                                JSONObject dailyObject = dailyArray.getJSONObject(i);
                                // Process daily object

                                double pop = Double.parseDouble(dailyObject.getString("pop"));
                                int datestamp = Integer.parseInt(dailyObject.getString("date_stamp"));
                                double temperature = Double.parseDouble(dailyObject.getString("temperature"));
                                double windspeed = Double.parseDouble(dailyObject.getString("wind_speed"));
                                double uvi = Double.parseDouble(dailyObject.getString("uvi"));
                                double rainfal = Double.parseDouble(dailyObject.getString("rainfall"));
                                String weather = dailyObject.getString("weather");
                                String description = dailyObject.getString("description");


                                DailyWeatherDataModel alertModel = new DailyWeatherDataModel(datestamp, temperature, windspeed, rainfal, uvi, pop, weather, description);
                                dailyList.add(alertModel);
                            }

                            adapterd.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing exception
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getContext(), "Error Trying to get Data Please Restart Application", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest);
    }

    private void setUpMainUiData(String location, String temperature, String weather){

        //Image or The Gif
        String[] prefixes = {"rain", "sun", "cloud"};
        String matchedPrefix = findMatchingPrefix(weather, prefixes, 3);
        if (matchedPrefix != null) {
            setLiveImage(matchedPrefix);
        }

        locationtxt.setText(location);
        temperaturetxt.setText(temperature + "\u2103");
        weathertxt.setText(weather);

    }

    public static String findMatchingPrefix(String input, String[] prefixes, int accuracy) {
        for (String prefix : prefixes) {
            if (startsWithWithAccuracy(input, prefix, accuracy)) {
                return prefix;
            }
        }
        return null;
    }

    public static boolean startsWithWithAccuracy(String input, String prefix, int accuracy) {
        if (input.length() < accuracy) {
            return false;
        }

        String inputPrefix = input.substring(0, accuracy);
        return inputPrefix.startsWith(prefix);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}