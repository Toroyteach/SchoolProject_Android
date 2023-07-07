package com.example.schoolproject.ui.cropsDistribution.ui.season.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;
import com.example.schoolproject.ui.cropsDistribution.ui.distribution.model.DistributionModel;
import com.example.schoolproject.ui.cropsDistribution.ui.distribution.model.DistributionRecyclerViewAdapter;

import java.util.List;

public class SeasonRecyclerViewAdpater extends RecyclerView.Adapter<SeasonRecyclerViewAdpater.ViewHolder> {
    private final List<SeasonModel> alertList;
    private final SeasonRecyclerViewAdpater.OnDeleteClickListener deleteClickListener;
    private final SeasonRecyclerViewAdpater.OnMarkAsReadClickListener markAsReadClickListener;

    public SeasonRecyclerViewAdpater(List<SeasonModel> alertList, SeasonRecyclerViewAdpater.OnDeleteClickListener deleteClickListener, SeasonRecyclerViewAdpater.OnMarkAsReadClickListener markAsReadClickListener) {
        this.alertList = alertList;
        this.deleteClickListener = deleteClickListener;
        this.markAsReadClickListener = markAsReadClickListener;
    }

    @NonNull
    @Override
    public SeasonRecyclerViewAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_recycler_item, parent, false);
        return new SeasonRecyclerViewAdpater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonRecyclerViewAdpater.ViewHolder holder, int position) {
        SeasonModel alertModel = alertList.get(position);

        holder.textViewSeasonName.setText(alertModel.getSeasonname());
        holder.textViewStartDate.setText(alertModel.getStartdate());
        holder.textViewEndDate.setText(alertModel.getEnddate());

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
        public TextView textViewSeasonName;
        public TextView textViewStartDate;
        public TextView textViewEndDate;
        public Button buttonUpdate;
        public Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSeasonName = itemView.findViewById(R.id.textViewSeasonName);
            textViewStartDate = itemView.findViewById(R.id.textViewSeasonStartDate);
            textViewEndDate = itemView.findViewById(R.id.textViewSeasonEndDate);
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
