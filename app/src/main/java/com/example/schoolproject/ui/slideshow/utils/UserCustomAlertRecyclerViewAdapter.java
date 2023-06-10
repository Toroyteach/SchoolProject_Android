package com.example.schoolproject.ui.slideshow.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;

import java.util.List;

public class UserCustomAlertRecyclerViewAdapter extends RecyclerView.Adapter<UserCustomAlertRecyclerViewAdapter.ViewHolder> {
    private final List<UserCustomAlertModel> alertList;
    private final OnDeleteClickListener deleteClickListener;

    public UserCustomAlertRecyclerViewAdapter(List<UserCustomAlertModel> alertList, OnDeleteClickListener deleteClickListener) {
        this.alertList = alertList;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserCustomAlertModel alertModel = alertList.get(position);

        holder.alertNameTextView.setText(alertModel.getAlertName());
        holder.userMessageTextView.setText(alertModel.getUserMessage());
        holder.upperLimitTextView.setText(String.valueOf(alertModel.getUpperLimit()));
        holder.lowerLimitTextView.setText(String.valueOf(alertModel.getLowerLimit()));

        // Set an OnClickListener for the delete action
        holder.deleteImageView.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView alertNameTextView;
        TextView userMessageTextView;
        TextView upperLimitTextView;
        TextView lowerLimitTextView;
        Button deleteImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alertNameTextView = itemView.findViewById(R.id.alert_type);
            userMessageTextView = itemView.findViewById(R.id.user_message);
            upperLimitTextView = itemView.findViewById(R.id.upper_limit);
            lowerLimitTextView = itemView.findViewById(R.id.lower_limit);
            deleteImageView = itemView.findViewById(R.id.alert_Item_delete_BTN);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}
