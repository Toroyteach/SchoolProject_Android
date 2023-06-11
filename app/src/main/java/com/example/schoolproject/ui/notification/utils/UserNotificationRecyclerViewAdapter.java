package com.example.schoolproject.ui.notification.utils;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;

import java.util.List;
import java.util.Objects;

public class UserNotificationRecyclerViewAdapter extends RecyclerView.Adapter<UserNotificationRecyclerViewAdapter.ViewHolder> {
    private final List<UserNotificationModel> alertList;
    private final UserNotificationRecyclerViewAdapter.OnDeleteClickListener deleteClickListener;
    private final UserNotificationRecyclerViewAdapter.OnMarkAsReadClickListener markAsReadClickListener;

    public UserNotificationRecyclerViewAdapter(List<UserNotificationModel> alertList, UserNotificationRecyclerViewAdapter.OnDeleteClickListener deleteClickListener, UserNotificationRecyclerViewAdapter.OnMarkAsReadClickListener markAsReadClickListener) {
        this.alertList = alertList;
        this.deleteClickListener = deleteClickListener;
        this.markAsReadClickListener = markAsReadClickListener;
    }

    @NonNull
    @Override
    public UserNotificationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new UserNotificationRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserNotificationRecyclerViewAdapter.ViewHolder holder, int position) {
        UserNotificationModel alertModel = alertList.get(position);

        holder.alertNameTextView.setText(alertModel.getAlertName());
        holder.userMessageTextView.setText(alertModel.getUserMessage());
        holder.upperLimitTextView.setText(String.valueOf(alertModel.getUpperLimit()));
        holder.lowerLimitTextView.setText(String.valueOf(alertModel.getLowerLimit()));
        holder.tvDateSent.setText(String.valueOf(alertModel.getNotification_timestamp()));

        // Set an OnClickListener for the delete action
        holder.deleteImageView.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(position);
            }
        });

        String data = alertModel.getReadAt();

        if (Objects.equals(data, "null")) {
            // Set green background
            holder.notificationItm.setBackgroundColor(Color.parseColor("#86DD9C"));

            holder.markRead.setOnClickListener(v -> {
                if (markAsReadClickListener != null) {
                    markAsReadClickListener.onMarkAsReadClick(position);
                }
            });

        } else {
            // Set default background color
            holder.notificationItm.setBackgroundColor(Color.parseColor("#EFC7C7"));

            holder.markRead.setEnabled(false);
        }


    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout notificationItm;
        TextView alertNameTextView;
        TextView userMessageTextView;
        TextView upperLimitTextView;
        TextView lowerLimitTextView;
        TextView tvDateSent;
        Button deleteImageView;
        Button markRead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationItm = itemView.findViewById(R.id.notification_item_bg);
            alertNameTextView = itemView.findViewById(R.id.alert_type_notice);
            userMessageTextView = itemView.findViewById(R.id.user_message_notification_item);
            upperLimitTextView = itemView.findViewById(R.id.notification_upper_limit);
            lowerLimitTextView = itemView.findViewById(R.id.notification_lower_limit);
            tvDateSent = itemView.findViewById(R.id.notification_date_send);
            deleteImageView = itemView.findViewById(R.id.button_notifications_delete);
            markRead = itemView.findViewById(R.id.button_notifications_markasread);

        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnMarkAsReadClickListener {
        void onMarkAsReadClick(int position);
    }
}
