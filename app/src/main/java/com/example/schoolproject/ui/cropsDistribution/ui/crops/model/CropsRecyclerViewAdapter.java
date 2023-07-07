package com.example.schoolproject.ui.cropsDistribution.ui.crops.model;

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
import com.example.schoolproject.ui.notification.utils.UserNotificationModel;
import com.example.schoolproject.ui.notification.utils.UserNotificationRecyclerViewAdapter;

import java.util.List;
import java.util.Objects;

public class CropsRecyclerViewAdapter  extends RecyclerView.Adapter<CropsRecyclerViewAdapter.ViewHolder> {
    private final List<CropModel> alertList;
    private final CropsRecyclerViewAdapter.OnDeleteClickListener deleteClickListener;
    private final CropsRecyclerViewAdapter.OnMarkAsReadClickListener markAsReadClickListener;

    public CropsRecyclerViewAdapter(List<CropModel> alertList, CropsRecyclerViewAdapter.OnDeleteClickListener deleteClickListener, CropsRecyclerViewAdapter.OnMarkAsReadClickListener markAsReadClickListener) {
        this.alertList = alertList;
        this.deleteClickListener = deleteClickListener;
        this.markAsReadClickListener = markAsReadClickListener;
    }

    @NonNull
    @Override
    public CropsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crops_recycler_item, parent, false);
        return new CropsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropsRecyclerViewAdapter.ViewHolder holder, int position) {
        CropModel alertModel = alertList.get(position);

        holder.textViewCropName.setText(alertModel.getCropname());
        holder.textViewPlantingDate.setText(alertModel.getPlantingdate());
        holder.textViewHarvestingDate.setText(String.valueOf(alertModel.getHarvestingdate()));

        // Set an OnClickListener for the delete action

        holder.buttonDelete.setOnClickListener(v -> {
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
        public TextView textViewCropName;
        public TextView textViewPlantingDate;
        public TextView textViewHarvestingDate;
        public Button buttonUpdate;
        public Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCropName = itemView.findViewById(R.id.textViewCropName);
            textViewPlantingDate = itemView.findViewById(R.id.textViewPlantingDate);
            textViewHarvestingDate = itemView.findViewById(R.id.textViewHarvestingDate);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteSingleCropItem);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnMarkAsReadClickListener {
        void onMarkAsReadClick(int position);
    }
}
