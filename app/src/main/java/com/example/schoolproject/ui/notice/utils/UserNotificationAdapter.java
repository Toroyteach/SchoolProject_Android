package com.example.schoolproject.ui.notice.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.schoolproject.R;
import com.example.schoolproject.ui.slideshow.utils.UserCustomAlertAdapter;
import com.example.schoolproject.ui.slideshow.utils.UserCustomAlertModel;
import com.example.schoolproject.ui.notice.utils.UserNotificationModel;

import java.util.ArrayList;

public class UserNotificationAdapter extends ArrayAdapter<UserNotificationModel> {

    private ArrayList<UserNotificationModel> dataSet;
    Context mContext;
    private int lastPosition = -1;

    String TAG = "SampleLogTag";

    // View lookup cache
    private static class ViewHolder {
        TextView txtAlertName;
        TextView tvUpperLimit;
        TextView tvLowerLimit;
        TextView tvUserMessage;
        TextView tvDateSent;
    }

    public UserNotificationAdapter(Context context, ArrayList<UserNotificationModel> notifications) {
        super(context, 0, notifications);
        this.dataSet = notifications;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        UserNotificationModel notificationModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        UserNotificationAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new UserNotificationAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.notification_item, parent, false);

            viewHolder.txtAlertName = (TextView) convertView.findViewById(R.id.notification_alert_name);
            viewHolder.tvUpperLimit = (TextView) convertView.findViewById(R.id.notification_upper_limit);
            viewHolder.tvLowerLimit = (TextView) convertView.findViewById(R.id.notification_lower_limit);
            viewHolder.tvUserMessage = (TextView) convertView.findViewById(R.id.notification_user_message);
            viewHolder.tvDateSent = (TextView) convertView.findViewById(R.id.notification_date_send);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserNotificationAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtAlertName.setText(notificationModel.getAlertName());
        viewHolder.tvUpperLimit.setText(notificationModel.getUpperLimit());
        viewHolder.tvLowerLimit.setText(notificationModel.getLowerLimit());
        viewHolder.tvUserMessage.setText(notificationModel.getUserMessage());
        viewHolder.tvDateSent.setText(notificationModel.getNotification_timestamp());


        // viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;

    }

}
