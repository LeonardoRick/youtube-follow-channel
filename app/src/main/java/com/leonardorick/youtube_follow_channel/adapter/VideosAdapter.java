package com.leonardorick.youtube_follow_channel.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leonardorick.youtube_follow_channel.R;
import com.leonardorick.youtube_follow_channel.model.youtubeAPI_data.YoutubeVideoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private List<YoutubeVideoItem> videosList;
    public VideosAdapter(List<YoutubeVideoItem> videosList ) { this.videosList = videosList; }
    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_item, parent, false);
        return new VideosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, int position) {

        YoutubeVideoItem video = videosList.get(position);
        Log.d(TAG, "onBindViewHolder: " + video.snippet.title);
        Picasso.get()
                .load(Uri.parse(video.snippet.thumbnails.high.url))
                .into(holder.thumb);
        holder.textVideoDesc.setText(video.snippet.title);
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    class VideosViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumb;
        private TextView textVideoDesc;
        public VideosViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            textVideoDesc = itemView.findViewById(R.id.videoTitle);
        }
    }
}