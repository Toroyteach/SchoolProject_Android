package com.example.schoolproject.ui.home.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;
import com.example.schoolproject.ui.home.utils.models.HourlyWeatherDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class HourlyWeatherDataRecyclerViewAdapter  extends RecyclerView.Adapter<HourlyWeatherDataRecyclerViewAdapter.ViewHolder> {
    private final List<HourlyWeatherDataModel> hourlyList;
    private List<Integer> imageList;
    private Context context;

    public HourlyWeatherDataRecyclerViewAdapter(Context context, List<HourlyWeatherDataModel> hourlyList) {
        this.hourlyList = hourlyList;
        this.imageList = initializeImageList();
        this.context = context;
    }

    @NonNull
    @Override
    public HourlyWeatherDataRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_hourly_data_item, parent, false);
        return new HourlyWeatherDataRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherDataRecyclerViewAdapter.ViewHolder holder, int position) {
        HourlyWeatherDataModel hourlyWeatherDataModel = hourlyList.get(position);

        long timestamp = Long.parseLong(hourlyWeatherDataModel.getTimestamp());
        String formattedTime = convertUnixTimestamp(timestamp);
        holder.hourlytimview.setText(formattedTime);


        String temperature = String.valueOf(hourlyWeatherDataModel.getTemperature());
        holder.hourlytemperature.setText(temperature+"\u2103");


        int imageResource = getImageResource(hourlyWeatherDataModel.getIcon());
        holder.hourlyiconImageview.setImageResource(imageResource);

    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }

    private int getImageResource(String imageName) {
        String imagePreName = "icon"+imageName;
        int resourceId = context.getResources().getIdentifier(imagePreName, "drawable", context.getPackageName());
        if (resourceId == 0) {
            // If the image resource is not found, you can set a default image or handle the scenario as needed
            resourceId = R.drawable.ic_android;
        }
        return resourceId;
    }

    private List<Integer> initializeImageList() {
        List<Integer> list = new ArrayList<>();
        // Add the resource IDs of the images to the list
        list.add(R.drawable.icon01d);
        list.add(R.drawable.icon01n);
        list.add(R.drawable.icon02d);
        list.add(R.drawable.icon02n);
        list.add(R.drawable.icon03d);
        list.add(R.drawable.icon03n);
        list.add(R.drawable.icon04d);
        list.add(R.drawable.icon04n);
        list.add(R.drawable.icon09d);
        list.add(R.drawable.icon09n);
        list.add(R.drawable.icon10d);
        list.add(R.drawable.icon10n);
        list.add(R.drawable.icon11d);
        list.add(R.drawable.icon11n);
        list.add(R.drawable.icon13d);
        list.add(R.drawable.icon13n);
        list.add(R.drawable.icon50d);
        list.add(R.drawable.icon50n);
        // Add more images as needed

        return list;
    }

    private String convertUnixTimestamp(long unixTimestamp) {
        Date date = new Date(unixTimestamp * 1000L); // Convert Unix timestamp to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hourlytimview;
        TextView hourlytemperature;
        ImageView hourlyiconImageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hourlytimview = itemView.findViewById(R.id.hourly_time_txtview);
            hourlytemperature = itemView.findViewById(R.id.hourly_temperature_txtview);
            hourlyiconImageview = itemView.findViewById(R.id.hourly_icon_imgview);

        }
    }
}
