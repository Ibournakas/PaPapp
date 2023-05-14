package com.example.papapp.Announcement_Services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.papapp.R;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
    private List<Announcements> announcements;

    public AnnouncementAdapter(List<Announcements> announcements) {
        this.announcements = announcements;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Announcements announcement = announcements.get(position);
        holder.dateTextView.setText(announcement.getDate());
        holder.titleTextView.setText(announcement.getTitle());
        holder.categoryTextView.setText(announcement.getCategory());
        holder.authorTextView.setText(announcement.getAuthor());
        holder.contentTextView.setText(announcement.getContent());
    }
    public void addAnnouncements(List<Announcements> newAnnouncements) {
        int startPosition = announcements.size();
        announcements.addAll(newAnnouncements);
        notifyItemRangeInserted(startPosition, newAnnouncements.size());
    }


    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView titleTextView;
        public TextView categoryTextView;
        public TextView authorTextView;
        public TextView contentTextView;

        public ViewHolder(View view) {
            super(view);
            dateTextView = view.findViewById(R.id.dateTextView);
            titleTextView = view.findViewById(R.id.titleTextView);
            categoryTextView = view.findViewById(R.id.categoryTextView);
            authorTextView = view.findViewById(R.id.authorTextView);
            contentTextView = view.findViewById(R.id.contentTextView);
        }

    }
}
