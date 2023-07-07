package com.example.schoolproject.ui.charts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproject.Auth.User;
import com.example.schoolproject.R;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.ui.cropsDistribution.ui.crops.model.CropModel;
import com.example.schoolproject.utils.URLs;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    private static final String TAG = ChartActivity.class.getSimpleName();

    private BarChart barchart1_quantitycropsperseason, barChart2_quantitydifferentcrops;
    private LineChart linechart1_trendsovertimepercrop, linechart2_plantharvesttrendsoverseason;
    private PieChart pieChart_cropsindifferentseason;

    private List<BarEntry> barChartEntries_1;
    private ArrayList<String> barChartLabels_1;
    private List<BarEntry> barChartEntries_2;
    private ArrayList<String> barChartLabels_2;

    private ArrayList<Entry> lineChartEntries_1;
    private ArrayList<String> lineChartLabels_1;
    private ArrayList<Entry> lineChartEntries_2;
    private ArrayList<String> lineChartLabels_2;

    private ArrayList<PieEntry> pieChartEntries;
    private ArrayList<String> pieChartLabels;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        progressBar = (ProgressBar) findViewById(R.id.graphs_proggressbar);

        barchart1_quantitycropsperseason = findViewById(R.id.barchart_1);
        barChart2_quantitydifferentcrops = findViewById(R.id.barchart_2);
        linechart1_trendsovertimepercrop = findViewById(R.id.ourLineChart_1);
        linechart2_plantharvesttrendsoverseason = findViewById(R.id.ourLineChart_2);
        pieChart_cropsindifferentseason = findViewById(R.id.ourPieChart);

        barChartEntries_1 = new ArrayList<>();
        barChartLabels_1 = new ArrayList<>();
        barChartEntries_2 = new ArrayList<>();
        barChartLabels_2 = new ArrayList<>();

        lineChartEntries_1 = new ArrayList<>();
        lineChartLabels_1 = new ArrayList<>();
        lineChartEntries_2 = new ArrayList<>();
        lineChartLabels_2 = new ArrayList<>();

        pieChartEntries = new ArrayList<>();
        pieChartLabels = new ArrayList<>();

        getDashboardData();

//        barchart1_quantitycropsperseason.setDrawBarShadow(false);
//        barchart1_quantitycropsperseason.setDrawValueAboveBar(true);
//        barchart1_quantitycropsperseason.getDescription().setEnabled(false);
//        // if more than 60 entries are displayed in the chart, no values will be drawn
//        barchart1_quantitycropsperseason.setMaxVisibleValueCount(60);
//        // scaling can now only be done on x- and y-axis separately
//        barchart1_quantitycropsperseason.setPinchZoom(false);
//        barchart1_quantitycropsperseason.setDrawGridBackground(false);
//
//        Legend l = barchart1_quantitycropsperseason.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setForm(Legend.LegendForm.SQUARE);
//        l.setFormSize(9f);
//        l.setTextSize(11f);
//        l.setXEntrySpace(4f);
//
//        setData(5, 100);
    }

    private void getDashboardData(){
        // Fetch JSON data from URL
        progressBar.setVisibility(View.VISIBLE);

        User user =  SharedPrefManager.getInstance(this).getUser();
        final int userId = user.getId();

        String url = URLs.URL_GET_DASHBOARD_GRAPH + "?id="+ userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse JSON data and populate the contactList
                        progressBar.setVisibility(View.GONE);

                        try {

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                JSONObject dataObject  = obj.getJSONObject("data");

                                JSONArray distributionQuantityOfCropsPerSeason = dataObject.getJSONArray("distributionQuantityOfCropsPerSeason_barchart1");
                                JSONArray distributionQuantityOfDifferentCrops = dataObject.getJSONArray("distributionQuantityOfDifferentCrops_barchart2");
                                JSONArray distributionTrendsOverTimeForSpecificCrops = dataObject.getJSONArray("distributionTrendsOverTimeForSpecificCrops_linechart1");
                                JSONArray plantingHarvestingTrendsOverDifferentSeasons = dataObject.getJSONArray("plantingHarvestingTrendsOverDifferentSeasons_linechart2");
                                JSONArray distributionOfCropsInDifferentSeasons = dataObject.getJSONArray("distributionOfCropsInDifferentSeasons_piechart");

                                // Process the data arrays and populate the respective ArrayLists
                                processBarChartData1(distributionQuantityOfCropsPerSeason);
                                processBarChartData2(distributionQuantityOfDifferentCrops);
                                processLineChartData1(distributionTrendsOverTimeForSpecificCrops);
                                processLineChartData2(plantingHarvestingTrendsOverDifferentSeasons);
                                processPieChartData(distributionOfCropsInDifferentSeasons);

                                // Check if ArrayLists are empty
                                if (!barChartEntries_1.isEmpty()) {
                                    setBarchart1_quantitycropsperseason();
                                }

                                if (!barChartEntries_2.isEmpty()) {
                                    setBarChart2_quantitydifferentcrops();
                                }

                                if (!lineChartEntries_1.isEmpty()) {
                                    setLinechart1_trendsovertimepercrop();
                                }

                                if (!lineChartEntries_2.isEmpty()) {
                                    setLinechart2_plantharvesttrendsoverseason();
                                }

                                if (!pieChartEntries.isEmpty()) {
                                    setPieChart_cropsindifferentseason();
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                            Toast.makeText(getApplicationContext(), "Error Retrieving your Graphs Data", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressBar.setVisibility(View.GONE);
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Error Retrieving your Graphs Data", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println(error.getMessage());
                        }
                    }
                });

        // Add the request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setData(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = (int) start; i < start + count; i++) {
            float val = (float) (Math.random() * (range + 1));

            if (Math.random() * 100 < 25) {
                values.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.star)));
            } else {
                values.add(new BarEntry(i, val));
            }
        }

        BarDataSet set1;

        if (barchart1_quantitycropsperseason.getData() != null && barchart1_quantitycropsperseason.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barchart1_quantitycropsperseason.getData().getDataSetByIndex(0);
            set1.setValues(values);
            barchart1_quantitycropsperseason.getData().notifyDataChanged();
            barchart1_quantitycropsperseason.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "The year 2021");

            set1.setDrawIcons(false);

            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

            List<GradientColor> gradientFills = new ArrayList<>();
            gradientFills.add(new GradientColor(startColor1, endColor1));
            gradientFills.add(new GradientColor(startColor2, endColor2));
            gradientFills.add(new GradientColor(startColor3, endColor3));
            gradientFills.add(new GradientColor(startColor4, endColor4));
            gradientFills.add(new GradientColor(startColor5, endColor5));

            set1.setGradientColors(gradientFills);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            barchart1_quantitycropsperseason.setData(data);
        }
    }

    private void setBarchart1_quantitycropsperseason(){
        BarDataSet barDataSet = new BarDataSet(barChartEntries_1, "Bar Chart 1");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);
        // Set labels on the x-axis
        barchart1_quantitycropsperseason.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barChartLabels_1));
        barchart1_quantitycropsperseason.setData(barData);
        barchart1_quantitycropsperseason.invalidate();
    }

    private void setBarChart2_quantitydifferentcrops(){
        BarDataSet barDataSet = new BarDataSet(barChartEntries_2, "Bar Chart 2");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);
        // Set labels on the x-axis
        barChart2_quantitydifferentcrops.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barChartLabels_2));
        barChart2_quantitydifferentcrops.setData(barData);
        barChart2_quantitydifferentcrops.invalidate();
    }

    private void setLinechart1_trendsovertimepercrop(){

        LineDataSet lineDataSet = new LineDataSet(lineChartEntries_1, "Line Chart 1");
        lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        LineData lineData = new LineData(lineDataSet);

        // Set labels on the x-axis
        linechart1_trendsovertimepercrop.getXAxis().setValueFormatter(new IndexAxisValueFormatter(lineChartLabels_1));
        linechart1_trendsovertimepercrop.setData(lineData);
        linechart1_trendsovertimepercrop.invalidate();
    }

    private void setLinechart2_plantharvesttrendsoverseason(){

        LineDataSet lineDataSet = new LineDataSet(lineChartEntries_2, "Line Chart 2");
        lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        LineData lineData = new LineData(lineDataSet);
        // Set labels on the x-axis
        linechart2_plantharvesttrendsoverseason.getXAxis().setValueFormatter(new IndexAxisValueFormatter(lineChartLabels_2));
        linechart2_plantharvesttrendsoverseason.setData(lineData);
        linechart2_plantharvesttrendsoverseason.invalidate();
    }

    private void setPieChart_cropsindifferentseason(){
        PieDataSet pieDataSet = new PieDataSet(pieChartEntries, "Pie Chart");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueFormatter(new PercentFormatter(pieChart_cropsindifferentseason));
        PieData pieData = new PieData(pieDataSet);

        pieChart_cropsindifferentseason.setData(pieData);
        pieChart_cropsindifferentseason.invalidate();
    }

    //PROCESS CHART DATA
    private void processBarChartData1(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String cropName = jsonObject.getString("crop_name");
            int quantity = jsonObject.getInt("total_quantity");

            // Add data to the Bar Chart ArrayLists
            barChartEntries_1.add(new BarEntry(i, quantity));
            barChartLabels_1.add(cropName);
        }
    }
    private void processBarChartData2(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String cropName = jsonObject.getString("crop_name");
            int quantity = jsonObject.getInt("total_quantity");

            // Add data to the Bar Chart ArrayLists
            barChartEntries_2.add(new BarEntry(i, quantity));
            barChartLabels_2.add(cropName);
        }
    }
    private void processLineChartData1(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String startDate = jsonObject.getString("start_date");
            String cropName = jsonObject.getString("crop_name");
            int quantity = jsonObject.getInt("total_quantity");

            // Add data to the Line Chart ArrayLists
            lineChartEntries_1.add(new Entry(i, quantity));
            lineChartLabels_1.add(startDate + " - " + cropName);
        }
    }
    private void processLineChartData2(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String cropName = jsonObject.getString("crop_name");
            int quantity = jsonObject.getInt("crop_count");

            // Add data to the Line Chart ArrayLists
            lineChartEntries_2.add(new Entry(i, quantity));
            lineChartLabels_2.add(cropName);
        }
    }
    private void processPieChartData(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String seasonName = jsonObject.getString("season_name");
            int quantity = jsonObject.getInt("quantity");

            // Add data to the Pie Chart ArrayLists
            pieChartEntries.add(new PieEntry(quantity, seasonName));
            pieChartLabels.add(seasonName);
        }
    }

}