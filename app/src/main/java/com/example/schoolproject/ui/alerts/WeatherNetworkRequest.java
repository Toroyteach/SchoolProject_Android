package com.example.schoolproject.ui.alerts;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

public class WeatherNetworkRequest extends AsyncTask<Void, Void, String> {

    private ProgressBar progressBar;

    public WeatherNetworkRequest(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Show spinner or progress dialog
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... params) {
        // Simulate sending data to the backend
        // Replace this with your actual implementation
        try {
            Thread.sleep(2000); // Simulating delay for sending data
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return a response or result message
        return "Data sent successfully!";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Hide spinner or progress dialog
        progressBar.setVisibility(View.GONE);

        // Show the result or perform any necessary action
        // You can update UI components or display a toast message, etc.
    }
}
