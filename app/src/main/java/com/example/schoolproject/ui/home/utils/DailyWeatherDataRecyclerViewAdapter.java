package com.example.schoolproject.ui.home.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;
import com.example.schoolproject.ui.home.utils.models.DailyWeatherDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DailyWeatherDataRecyclerViewAdapter  extends RecyclerView.Adapter<DailyWeatherDataRecyclerViewAdapter.ViewHolder> {
    private final List<DailyWeatherDataModel> dailyList;

    public DailyWeatherDataRecyclerViewAdapter(List<DailyWeatherDataModel> dailyList) {
        this.dailyList = dailyList;
    }

    @NonNull
    @Override
    public DailyWeatherDataRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_daily_data_item, parent, false);
        return new DailyWeatherDataRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWeatherDataRecyclerViewAdapter.ViewHolder holder, int position) {
        DailyWeatherDataModel dailyWeatherDataModel = dailyList.get(position);

        long timestamp = Long.parseLong(dailyWeatherDataModel.getDatestamp());
        String formattedTime = convertUnixTimestampToDayOfWeek(timestamp);
        holder.dailydate.setText(formattedTime);


        String temperature = String.valueOf(dailyWeatherDataModel.getTemperature());
        String windspeed = String.valueOf(dailyWeatherDataModel.getWindSpeed());
        String uvi = String.valueOf(dailyWeatherDataModel.getUvi());
        String rainfall = String.valueOf(dailyWeatherDataModel.getRainfall());

        holder.dailytemperature.setText(temperature+"\u2103");
        holder.dailywindspeed.setText(windspeed+"m/s");
        holder.dailyuvi.setText(uvi);
        holder.dailyrain.setText(rainfall+"mm/h");
        holder.dailyweather.setText(dailyWeatherDataModel.getWeather());
        holder.dailydescription.setText(dailyWeatherDataModel.getDescription());

    }

    @Override
    public int getItemCount() {
        return dailyList.size();
    }

    private String convertUnixTimestampToDayOfWeek(long unixTimestamp) {
        Date date = new Date(unixTimestamp * 1000L); // Convert Unix timestamp to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dailytemperature;
        TextView dailywindspeed;
        TextView dailyuvi;
        TextView dailyrain;
        TextView dailyweather;
        TextView dailydescription;
        TextView dailydate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dailytemperature = itemView.findViewById(R.id.daily_data_temperature);
            dailywindspeed = itemView.findViewById(R.id.daily_data_windspeed);
            dailyuvi = itemView.findViewById(R.id.daily_data_uvi);
            dailyrain = itemView.findViewById(R.id.daily_data_rainfall);
            dailyweather = itemView.findViewById(R.id.daily_data_weather);
            dailydescription = itemView.findViewById(R.id.daily_data_description);
            dailydate = itemView.findViewById(R.id.daily_data_date);

        }
    }
}
