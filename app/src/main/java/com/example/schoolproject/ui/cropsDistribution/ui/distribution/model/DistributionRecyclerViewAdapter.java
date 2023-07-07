package com.example.schoolproject.ui.cropsDistribution.ui.distribution.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;
import com.example.schoolproject.ui.cropsDistribution.ui.crops.model.CropModel;
import com.example.schoolproject.ui.cropsDistribution.ui.crops.model.CropsRecyclerViewAdapter;

import java.util.List;

public class DistributionRecyclerViewAdapter extends RecyclerView.Adapter<DistributionRecyclerViewAdapter.ViewHolder> {
    private final List<DistributionModel> alertList;
    private final DistributionRecyclerViewAdapter.OnDeleteClickListener deleteClickListener;
    private final DistributionRecyclerViewAdapter.OnMarkAsReadClickListener markAsReadClickListener;

    public DistributionRecyclerViewAdapter(List<DistributionModel> alertList, DistributionRecyclerViewAdapter.OnDeleteClickListener deleteClickListener, DistributionRecyclerViewAdapter.OnMarkAsReadClickListener markAsReadClickListener) {
        this.alertList = alertList;
        this.deleteClickListener = deleteClickListener;
        this.markAsReadClickListener = markAsReadClickListener;
    }

    @NonNull
    @Override
    public DistributionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distribution_recycler_item, parent, false);
        return new DistributionRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributionRecyclerViewAdapter.ViewHolder holder, int position) {
        DistributionModel alertModel = alertList.get(position);

        holder.textViewCropId.setText(alertModel.getCropid());
        holder.textViewSeasonId.setText(alertModel.getSeasonid());
        holder.textViewSize.setText(alertModel.getSize());

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
        public TextView textViewCropId;
        public TextView textViewSeasonId;
        public TextView textViewSize;
        public Button buttonUpdate;
        public Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCropId = itemView.findViewById(R.id.textViewDistributionCropName);
            textViewSeasonId = itemView.findViewById(R.id.textViewDistributionSeasonName);
            textViewSize = itemView.findViewById(R.id.textViewDistributionSize);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteSeasonSingleItem);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnMarkAsReadClickListener {
        void onMarkAsReadClick(int position);
    }
}
