package com.example.schoolproject.ui.alerts.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproject.R;
import com.example.schoolproject.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserCustomAlertAdapter extends ArrayAdapter<UserCustomAlertModel> {

    private ArrayList<UserCustomAlertModel> dataSet;
    Context mContext;
    private int lastPosition = -1;

    private UserCustomAlertModel userCustomAlertModel;

    String TAG = "SampleLogTag";

    // View lookup cache
    private static class ViewHolder {
        TextView txtAlertName;
        TextView tvUpperLimit;
        TextView tvLowerLimit;
        TextView tvUserMessage;
        Button delete;
    }

    public UserCustomAlertAdapter(Context context, ArrayList<UserCustomAlertModel> customAlerts) {
        super(context, 0, customAlerts);
        this.dataSet = customAlerts;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        UserCustomAlertModel customAlertModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.alert_items, parent, false);

            viewHolder.txtAlertName = (TextView) convertView.findViewById(R.id.alert_type);
            viewHolder.tvUpperLimit = (TextView) convertView.findViewById(R.id.upper_limit);
            viewHolder.tvLowerLimit = (TextView) convertView.findViewById(R.id.lower_limit);
            viewHolder.tvUserMessage = (TextView) convertView.findViewById(R.id.user_message);

            viewHolder.delete = (Button) convertView.findViewById(R.id.alert_Item_delete_BTN);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtAlertName.setText(customAlertModel.getAlertName());
        viewHolder.tvUpperLimit.setText(customAlertModel.getUpperLimit());
        viewHolder.tvLowerLimit.setText(customAlertModel.getLowerLimit());
        viewHolder.tvUserMessage.setText(customAlertModel.getUserMessage());

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserCustomAlertModel object = getItem(position);
                showWarningDialog(object.getId());
            }
        });

        // viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;

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
        String deleteUrl = URLs.URL_DELETE_ALERT + "?id=" + alertId;

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

                            //Call the method to refresh the list of custom alerts
                            dataSet.remove(0);
                            dataSet.notify();


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


}