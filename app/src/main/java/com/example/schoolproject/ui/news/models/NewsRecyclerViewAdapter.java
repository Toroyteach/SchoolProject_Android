package com.example.schoolproject.ui.news.models;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;
import com.example.schoolproject.utils.URLs;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {
    private final List<NewsModel> alertList;
    private final NewsRecyclerViewAdapter.OnReadMoreClickListener deleteClickListener;

    public NewsRecyclerViewAdapter(List<NewsModel> alertList, NewsRecyclerViewAdapter.OnReadMoreClickListener deleteClickListener) {
        this.alertList = alertList;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public NewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecyclerViewAdapter.ViewHolder holder, int position) {
        NewsModel alertModel = alertList.get(position);


        String title =  alertModel.getTitle();
        String body =  alertModel.getBody();
        String image =  alertModel.getImage();
        String created =  alertModel.getCreatedAt();

        Picasso.get().load(image).into(holder.newsImageView);

        holder.titleTextView.setText(title);
        holder.bodyTextView.setText(String.valueOf(body));
        holder.createdAtTextView.setText(created);


        holder.readMoreTextView.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onReadClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImageView;
        public TextView categoryTextView;
        public TextView titleTextView;
        public TextView bodyTextView;
        public TextView createdAtTextView;
        public TextView readMoreTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newsImageView = itemView.findViewById(R.id.newsThumbnailImageView);
            //categoryTextView = itemView.findViewById(R.id.categoryTextView);
            titleTextView = itemView.findViewById(R.id.newsTitleTextView);
            bodyTextView = itemView.findViewById(R.id.newsDetailTextView);
            createdAtTextView = itemView.findViewById(R.id.news_item_created_date);
            readMoreTextView = itemView.findViewById(R.id.news_item_read_more);

        }
    }

    public interface OnReadMoreClickListener {
        void onReadClick(int position);
    }

    public interface OnMarkAsReadClickListener {
        void onMarkAsReadClick(int position);
    }
}
