package com.example.schoolproject.ui.home.utils.models;

public class HourlyWeatherDataModel {

    int weatherId;
    int timestamp;
    double temperature;
    double windSpeed;
    double rainfall;
    double uvi;
    double pop;
    String weather;
    String description;
    String icon;

    public HourlyWeatherDataModel(int weatherId, int timestamp, double temperature, double windSpeed, double rainfall, double uvi, double pop, String weather, String description, String icon) {
        this.weatherId = weatherId;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.rainfall = rainfall;
        this.uvi = uvi;
        this.pop = pop;
        this.weather = weather;
        this.description = description;
        this.icon = icon;
    }

    public HourlyWeatherDataModel(int timestamp, double temperature, String icon) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.icon = icon;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public String getTimestamp() {
        return String.valueOf(timestamp);
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getRainfall() {
        return rainfall;
    }

    public double getUvi() {
        return uvi;
    }

    public double getPop() {
        return pop;
    }

    public String getWeather() {
        return weather;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
