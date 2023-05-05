package com.example.papapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    private ArrayList<String> announcementList;

    public AnnouncementAdapter(ArrayList<String> announcementList) {
        this.announcementList = announcementList;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item, parent, false);
        return new AnnouncementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        String announcement = announcementList.get(position);
        String[] announcementDetails = announcement.split("\n");
        holder.titleTextView.setText(announcementDetails[1].substring(7));
        holder.descriptionTextView.setText(announcementDetails[4].substring(9));
        holder.dateTextView.setText(announcementDetails[0].substring(6));
        holder.authorTextView.setText(announcementDetails[2].substring(8));
        holder.linkTextView.setText(announcementDetails[5].substring(8));
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView dateTextView;
        public TextView authorTextView;
        public TextView linkTextView;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            linkTextView = itemView.findViewById(R.id.linkTextView);
        }
    }
}
