package com.example.schoolproject.ui.home.utils.models;

public class DailyWeatherDataModel {

    int weatherId;
    int datestamp;
    double temperature;
    double windSpeed;
    double rainfall;
    double uvi;
    double pop;
    String weather;
    String description;
    String icon;

    public DailyWeatherDataModel(int weatherId, int datestamp, double temperature, double windSpeed, double rainfall, double uvi, double pop, String weather, String description, String icon) {
        this.weatherId = weatherId;
        this.datestamp = datestamp;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.rainfall = rainfall;
        this.uvi = uvi;
        this.pop = pop;
        this.weather = weather;
        this.description = description;
        this.icon = icon;
    }

    public DailyWeatherDataModel(int datestamp, double temperature, double windSpeed, double rainfall, double uvi, double pop, String weather, String description) {
        this.datestamp = datestamp;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.rainfall = rainfall;
        this.uvi = uvi;
        this.pop = pop;
        this.weather = weather;
        this.description = description;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public String getDatestamp() {
        return String.valueOf(datestamp);
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
